package net.yukimiworks.mcmod.electronics.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.yukimiworks.mcmod.electronics.tileentity.TileEntityGenerator;

public class ContainerGenerator extends Container {

	private IInventory inventory;

	private int burnTime;
	private int currentItemBurnTime;
	private int curEnergy;

	public ContainerGenerator(IInventory playerInventory, final IInventory inventory) {
		this.inventory = inventory;

		// Fuel Input
		this.addSlotToContainer(new Slot(inventory, 0, 44, 34));

		// Energy Output
		this.addSlotToContainer(new Slot(inventory, 1, 116, 34));

		// Player Inventory
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(playerInventory, i * 9 + j + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		// Player Toolbox
		for (int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
		}
	}

	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		listener.sendAllWindowProperties(this, this.inventory);
	}

	/**
	 * Looks for changes made in the container, sends them to every listener.
	 */
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < this.listeners.size(); ++i) {
			IContainerListener containerListener = this.listeners.get(i);

			if (this.burnTime != this.inventory.getField(TileEntityGenerator.FIELD_BURN_TIME)) {
				containerListener.sendWindowProperty(this, TileEntityGenerator.FIELD_BURN_TIME,
						this.inventory.getField(TileEntityGenerator.FIELD_BURN_TIME));
			}

			if (this.currentItemBurnTime != this.inventory.getField(TileEntityGenerator.FIELD_CURRENT_ITEM_BURN_TIME)) {
				containerListener.sendWindowProperty(this, TileEntityGenerator.FIELD_CURRENT_ITEM_BURN_TIME,
						this.inventory.getField(TileEntityGenerator.FIELD_CURRENT_ITEM_BURN_TIME));
			}

			if (this.curEnergy != this.inventory.getField(TileEntityGenerator.FIELD_CURRENT_ENERGY)) {
				containerListener.sendWindowProperty(this, TileEntityGenerator.FIELD_CURRENT_ENERGY,
						this.inventory.getField(TileEntityGenerator.FIELD_CURRENT_ENERGY));
			}
		}

		this.burnTime = this.inventory.getField(TileEntityGenerator.FIELD_BURN_TIME);
		this.currentItemBurnTime = this.inventory.getField(TileEntityGenerator.FIELD_CURRENT_ITEM_BURN_TIME);
		this.curEnergy = this.inventory.getField(TileEntityGenerator.FIELD_CURRENT_ENERGY);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack slotStack = slot.getStack();
			itemStack = slotStack.copy();
			int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();
			if (index < containerSlots) {
				if (!this.mergeItemStack(slotStack, containerSlots, inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(slotStack, 0, containerSlots, false)) {
				return ItemStack.EMPTY;
			}
			if (slotStack.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
			if (slotStack.getCount() == itemStack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(player, slotStack);
		}
		return itemStack;
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		this.inventory.setField(id, data);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return !playerIn.isSpectator();
	}
}
