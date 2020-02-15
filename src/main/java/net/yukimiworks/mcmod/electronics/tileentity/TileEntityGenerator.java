package net.yukimiworks.mcmod.electronics.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.yukimiworks.mcmod.electronics.Electronics;
import net.yukimiworks.mcmod.electronics.container.ContainerGenerator;
import net.yukimiworks.mcmod.electronics.item.ItemBattery;

import javax.annotation.Nullable;

public class TileEntityGenerator extends TileEntityLockable implements IInventory, ITickable {

	public static final String DISPLAY_NAME = "container.generator";
	public static final String TAG_NAME = "generator_inventory";
	public static final int SLOT_FUEL = 0;
	public static final int SLOT_BATTERY_CHARGER = 1;

	public static final int FIELD_BURN_TIME = 0;
	public static final int FIELD_CURRENT_ITEM_BURN_TIME = 1;
	public static final int FIELD_CURRENT_ENERGY = 2;

	public static final int OUTPUT = 10;
	public static final int MAX_ENERGY = 10000;

	private ItemStackHandler inventory = new ItemStackHandler(2);

	private int burnTime;
	private int currentItemBurnTime;

	private int output;
	private int maxEnergy;
	private int curEnergy;

	public TileEntityGenerator() {
		this.output = 10;
		this.maxEnergy = 10000;
		this.curEnergy = 0;
		this.burnTime = 0;
		this.currentItemBurnTime = 0;
	}

	@SideOnly(Side.CLIENT)
	public static boolean burnable(IInventory inventory) {
		return inventory.getField(FIELD_BURN_TIME) > 0;
	}

	public boolean isBurning() {
		return this.burnable() && !this.isFullCharged();
	}

	public boolean burnable() {
		return this.burnTime > 0;
	}

	public boolean isFullCharged() {
		return this.curEnergy >= this.maxEnergy;
	}

	public void setInventory(NBTTagList nbtTagList, Object... data) {
		if (nbtTagList == null || nbtTagList.isEmpty()) {
			return;
		}

		for (int i = 0; i < inventory.getSlots(); i++) {
			NBTTagCompound nbtTagCompound = nbtTagList.getCompoundTagAt(i);
			byte slotID = nbtTagCompound.getByte("Slot");
			if (slotID >= 0 && slotID < inventory.getSlots()) {
				inventory.setStackInSlot(slotID, new ItemStack(nbtTagCompound));
			}
		}
	}

	public NBTTagList getInventory(Object... data) {
		NBTTagList nbtTagList = new NBTTagList();
		for (int i = 0; i < inventory.getSlots(); i++) {
			if (!inventory.getStackInSlot(i).isEmpty()) {
				NBTTagCompound nbtTagCompound = new NBTTagCompound();
				nbtTagCompound.setByte("Slot", (byte) i);
				inventory.getStackInSlot(i).writeToNBT(nbtTagCompound);
				nbtTagList.appendTag(nbtTagCompound);
			}
		}

		return nbtTagList;
	}

	public void setCurrentEnergy(int currentEnergy, Object... data) {
		this.curEnergy = currentEnergy;
	}

	public int getCurrentEnergy() {
		return this.curEnergy;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		inventory.deserializeNBT(compound.getCompoundTag(TAG_NAME));
		this.curEnergy = compound.getInteger("Current Energy");
		this.burnTime = compound.getInteger("Burn Time");
		this.currentItemBurnTime = compound.getInteger("Current Burn Time");
		super.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag(TAG_NAME, inventory.serializeNBT());
		compound.setInteger("Current Energy", this.curEnergy);
		compound.setInteger("Burn Time", this.burnTime);
		compound.setInteger("Current Burn Time", this.currentItemBurnTime);
		return super.writeToNBT(compound);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) inventory;
		}

		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public void update() {

		boolean currentBurnState = this.isBurning();
		boolean isStateChanged = false;

		if (this.isBurning()) {
			--this.burnTime;
		}

		if (!this.world.isRemote) {

			ItemStack fuel = inventory.getStackInSlot(SLOT_FUEL);
			if (!fuel.isEmpty() && !this.burnable() && !this.isFullCharged()) {
				this.burnTime = TileEntityFurnace.getItemBurnTime(fuel);
				this.currentItemBurnTime = this.burnTime;

				if (this.isBurning()) {

					isStateChanged = true;
					if (!fuel.isEmpty()) {
						fuel.shrink(1);
						if (fuel.isEmpty()) {
							ItemStack containerItem = fuel.getItem().getContainerItem(fuel);
							this.inventory.setStackInSlot(SLOT_FUEL, containerItem);
						}
					}
				}
			}

			if (this.isBurning()) {
				this.curEnergy = MathHelper.clamp(this.curEnergy + this.output, 0, this.maxEnergy);
			}

			ItemStack battery = inventory.getStackInSlot(SLOT_BATTERY_CHARGER);
			if (!battery.isEmpty() && battery.getItem() instanceof ItemBattery && battery.getItemDamage() != 0) {
				battery.setItemDamage(MathHelper.clamp(battery.getItemDamage() - ItemBattery.INCREASE, 0, battery.getMaxDamage()));
				this.curEnergy = MathHelper.clamp(this.curEnergy - ItemBattery.INCREASE, 0, this.maxEnergy);
			}

			if (currentBurnState != this.isBurning()) {
				isStateChanged = true;
				Electronics.Blocks.GENERATOR.setState(this.isBurning(), this.world, this.pos);
			}
		}

		if (isStateChanged) {
			this.markDirty();
		}
	}

	@Override
	public String getName() {
		return DISPLAY_NAME;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int getSizeInventory() {
		return this.inventory.getSlots();
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < this.inventory.getSlots(); i++) {
			if (!this.getStackInSlot(i).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.inventory.getStackInSlot(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return this.inventory.extractItem(index, count, false);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return this.inventory.extractItem(index, 64, false);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.inventory.setStackInSlot(index, stack);
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {

		if (this.world.getTileEntity(this.pos) != this) {
			return false;
		} else {
			return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Override
	public void openInventory(EntityPlayer player) {

	}

	@Override
	public void closeInventory(EntityPlayer player) {

	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return this.inventory.isItemValid(index, stack);
	}

	@Override
	public int getField(int id) {
		switch (id) {
			case FIELD_BURN_TIME:
				return this.burnTime;
			case FIELD_CURRENT_ITEM_BURN_TIME:
				return this.currentItemBurnTime;
			case FIELD_CURRENT_ENERGY:
				return this.curEnergy;
			default:
				return 0;
		}
	}

	@Override
	public void setField(int id, int value) {
		switch (id) {
			case FIELD_BURN_TIME:
				this.burnTime = value;
				break;
			case FIELD_CURRENT_ITEM_BURN_TIME:
				this.currentItemBurnTime = value;
				break;
			case FIELD_CURRENT_ENERGY:
				this.curEnergy = value;
				break;
		}
	}

	@Override
	public int getFieldCount() {
		return 3;
	}

	@Override
	public void clear() {
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ContainerGenerator(playerInventory, this);
	}

	@Override
	public String getGuiID() {
		return "electronics:generator";
	}
}
