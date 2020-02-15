package net.yukimiworks.mcmod.electronics;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.yukimiworks.mcmod.electronics.container.ContainerGenerator;
import net.yukimiworks.mcmod.electronics.gui.GuiGeneratorContainer;
import net.yukimiworks.mcmod.electronics.tileentity.TileEntityGenerator;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {

	public static final int GENERATOR_CONTAINER = 1;

	@Nullable
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
			case GENERATOR_CONTAINER:
				return new ContainerGenerator(player.inventory, (TileEntityGenerator) world.getTileEntity(new BlockPos(x, y, z)));
		}

		return null;
	}

	@Nullable
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
			case GENERATOR_CONTAINER:
				return new GuiGeneratorContainer(player.inventory, (TileEntityGenerator) world.getTileEntity(new BlockPos(x, y, z)));
		}

		return null;
	}
}
