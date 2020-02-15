package net.yukimiworks.mcmod.electronics.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.yukimiworks.mcmod.electronics.Electronics;
import net.yukimiworks.mcmod.electronics.TranslationsKeys;
import net.yukimiworks.mcmod.electronics.tileentity.TileEntityGenerator;
import net.yukimiworks.mcmod.electronics.util.Utils;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBattery extends Item {

	public static final int MAX_CAPACITY = 2000;

	public static final int INCREASE = 10;

	public ItemBattery() {
		super();

		this.setRegistryName(Electronics.MOD_ID, "battery");
		this.setTranslationKey("battery");
		this.setCreativeTab(Electronics.ELECTRONICS_CREATIVE_TAB);
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		this.setMaxDamage(MAX_CAPACITY);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (tab == this.getCreativeTab()) {
			items.add(new ItemStack(this, 1, 0));
			items.add(new ItemStack(this, 1, MAX_CAPACITY));
		}
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);

		tooltip.add(Utils.getTranslatedUnformattedText(TranslationsKeys.TRANSLATION_KEY_BATTERY_CURRENT, Utils.formattedNumber(stack.getMaxDamage() - stack.getItemDamage())));
		tooltip.add(Utils.getTranslatedUnformattedText(TranslationsKeys.TRANSLATION_KEY_BATTERY_MAX, Utils.formattedNumber(stack.getMaxDamage())));
	}
}
