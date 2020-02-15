package net.yukimiworks.mcmod.electronics.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.yukimiworks.mcmod.electronics.Electronics;
import net.yukimiworks.mcmod.electronics.TranslationsKeys;
import net.yukimiworks.mcmod.electronics.container.ContainerGenerator;
import net.yukimiworks.mcmod.electronics.tileentity.TileEntityGenerator;
import net.yukimiworks.mcmod.electronics.util.Utils;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

public class GuiGeneratorContainer extends GuiContainer {

	private static final String TEXTURE_PATH = "textures/gui/container/generator.png";

	private final IInventory playerInventory;
	private final IInventory tileEntity;

	public GuiGeneratorContainer(IInventory playerInventory, IInventory tileEntity) {
		super(new ContainerGenerator(playerInventory, tileEntity));
		this.playerInventory = playerInventory;
		this.tileEntity = tileEntity;

		this.xSize = 176;
		this.ySize = 166;
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String title = this.tileEntity.getDisplayName().getUnformattedText();
		this.fontRenderer.drawString(title, this.xSize / 2 - this.fontRenderer.getStringWidth(title) / 2, 6, 4210752);
		this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);

		String outputText = Utils.getTranslatedUnformattedText(TranslationsKeys.TRANSLATION_KEY_GENERATOR_OUTPUT, Utils.formattedNumber(TileEntityGenerator.OUTPUT));
		this.fontRenderer.drawString(outputText, this.xSize - this.fontRenderer.getStringWidth(outputText) - 7, this.ySize - 96 + 2, 4210752);
//		Electronics.LOG.info("mouseX=[{}], mouseY=[{}]", mouseX, mouseY);

		if (Utils.isInRect(mouseX, mouseY, this.guiLeft + 81, this.guiTop + 16, this.guiLeft + 94, this.guiTop + 68)) {
			List<String> toolTipLine = Arrays.asList(
					Utils.getTranslatedUnformattedText(TranslationsKeys.TRANSLATION_KEY_GENERATOR_CURRENT,
							Utils.formattedNumber(this.tileEntity.getField(TileEntityGenerator.FIELD_CURRENT_ENERGY))),
					Utils.getTranslatedUnformattedText(TranslationsKeys.TRANSLATION_KEY_GENERATOR_MAX, NumberFormat.getNumberInstance().format(TileEntityGenerator.MAX_ENERGY)));
			this.drawHoveringText(toolTipLine, mouseX - this.guiLeft, mouseY - this.guiTop);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1, 1, 1, 1);
		this.mc.getTextureManager().bindTexture(new ResourceLocation(Electronics.MOD_ID, TEXTURE_PATH));
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

		if (TileEntityGenerator.burnable(this.tileEntity)) {
			int i = this.getBurningProgress(24);
			this.drawTexturedModalRect(this.guiLeft + 45, this.guiTop + 31 - i, 176, 13 - i, 13, 1 + i);
		}

		int l = this.getEnergyScaled(52);
		this.drawTexturedModalRect(this.guiLeft + 81, this.guiTop + 68 - l, 176, 66 - l, 14, l);
	}

	private int getBurningProgress(int pixels) {
		int i = this.tileEntity.getField(TileEntityGenerator.FIELD_BURN_TIME);
		int j = this.tileEntity.getField(TileEntityGenerator.FIELD_CURRENT_ITEM_BURN_TIME);

		return j != 0 && i != 0 ? i * pixels / j : 0;
	}

	private int getEnergyScaled(int pixels) {
		int i = this.tileEntity.getField(TileEntityGenerator.FIELD_CURRENT_ENERGY);
		int j = TileEntityGenerator.MAX_ENERGY;

		return j != 0 && i != 0 ? i * pixels / j : 0;
	}
}
