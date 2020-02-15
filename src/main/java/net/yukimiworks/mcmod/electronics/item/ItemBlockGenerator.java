package net.yukimiworks.mcmod.electronics.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.yukimiworks.mcmod.electronics.Electronics;
import net.yukimiworks.mcmod.electronics.TranslationsKeys;
import net.yukimiworks.mcmod.electronics.tileentity.TileEntityGenerator;
import net.yukimiworks.mcmod.electronics.util.Utils;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockGenerator extends ItemBlock {

    public ItemBlockGenerator() {
        super(Electronics.Blocks.GENERATOR);

        this.setMaxStackSize(1);
        this.setRegistryName(Electronics.MOD_ID, "generator");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        tooltip.add(Utils.getTranslatedUnformattedText(TranslationsKeys.TRANSLATION_KEY_GENERATOR_OUTPUT, Utils.formattedNumber(TileEntityGenerator.OUTPUT)));

        int currentEnergy;
        if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("Electronics Data")) {
            currentEnergy = stack.getTagCompound().getCompoundTag("Electronics Data").getInteger("Current Energy");
        } else {
            currentEnergy = 0;
        }
        tooltip.add(Utils.getTranslatedUnformattedText(TranslationsKeys.TRANSLATION_KEY_GENERATOR_CURRENT, Utils.formattedNumber(currentEnergy)));
        tooltip.add(Utils.getTranslatedUnformattedText(TranslationsKeys.TRANSLATION_KEY_GENERATOR_MAX, Utils.formattedNumber(TileEntityGenerator.MAX_ENERGY)));
    }

    public void setInventory(NBTTagList nbtTags, Object... data) {
        if (data[0] instanceof ItemStack) {
            ItemStack itemStack = (ItemStack) data[0];
            if (itemStack.getTagCompound() == null) {
                itemStack.setTagCompound(new NBTTagCompound());
            }
            if (!itemStack.getTagCompound().hasKey("Electronics Data")) {
                itemStack.getTagCompound().setTag("Electronics Data", new NBTTagCompound());
            }

            itemStack.getTagCompound().getCompoundTag("Electronics Data").setTag("Items", nbtTags);
        }
    }

    public NBTTagList getInventory(Object... data) {
        if (data[0] instanceof ItemStack) {
            ItemStack itemStack = (ItemStack) data[0];
            if (itemStack.getTagCompound() == null) {
                return new NBTTagList();
            }

            if (!itemStack.getTagCompound().hasKey("Electronics Data")) {
                return new NBTTagList();
            }

            return itemStack.getTagCompound().getCompoundTag("Electronics Data").getTagList("Items", Constants.NBT.TAG_COMPOUND);
        }

        return null;
    }

    public void setCurrentEnergy(int currentEnergy, Object... data) {
        if (data[0] instanceof ItemStack) {
            ItemStack itemStack = (ItemStack) data[0];
            if (itemStack.getTagCompound() == null) {
                itemStack.setTagCompound(new NBTTagCompound());
            }
            if (!itemStack.getTagCompound().hasKey("Electronics Data")) {
                itemStack.getTagCompound().setTag("Electronics Data", new NBTTagCompound());
            }

            itemStack.getTagCompound().getCompoundTag("Electronics Data").setInteger("Current Energy", currentEnergy);
        }
    }

    public int getCurrentEnergy(Object... data) {
        if (data[0] instanceof ItemStack) {
            ItemStack itemStack = (ItemStack) data[0];
            if (itemStack.getTagCompound() == null) {
                return 0;
            }

            if (!itemStack.getTagCompound().hasKey("Electronics Data")) {
                return 0;
            }

            return itemStack.getTagCompound().getCompoundTag("Electronics Data").getInteger("Current Energy");
        }

        return 0;
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        if (super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState)) {
            TileEntityGenerator tileEntityGenerator = (TileEntityGenerator) world.getTileEntity(pos);
            tileEntityGenerator.setInventory(this.getInventory(stack));
            tileEntityGenerator.setCurrentEnergy(this.getCurrentEnergy(stack));
            return true;
        }

        return false;
    }
}
