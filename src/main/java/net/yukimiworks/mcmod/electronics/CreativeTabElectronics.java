package net.yukimiworks.mcmod.electronics;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.yukimiworks.mcmod.electronics.Electronics;

public class CreativeTabElectronics extends CreativeTabs {

	public CreativeTabElectronics() {
		super("tabElectronics");
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(Electronics.Blocks.GENERATOR);
	}
}
