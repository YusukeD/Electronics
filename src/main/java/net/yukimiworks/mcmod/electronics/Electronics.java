package net.yukimiworks.mcmod.electronics;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import net.yukimiworks.mcmod.electronics.block.BlockGenerator;
import net.yukimiworks.mcmod.electronics.item.ItemBattery;
import net.yukimiworks.mcmod.electronics.item.ItemBlockGenerator;
import net.yukimiworks.mcmod.electronics.tileentity.TileEntityGenerator;
import org.apache.logging.log4j.Logger;


@Mod(modid = Electronics.MOD_ID, name = Electronics.MOD_NAME, version = Electronics.VERSION)
public class Electronics {

	public static final String MOD_ID = "electronics";
	public static final String MOD_NAME = "Electronics";
	public static final String VERSION = "2019.3-1.3.2";

	public static final CreativeTabs ELECTRONICS_CREATIVE_TAB = new CreativeTabElectronics();

	public static Logger LOG;

	/**
	 * This is the instance of your mod as created by Forge. It will never be null.
	 */
	@Mod.Instance(MOD_ID)
	public static Electronics INSTANCE;

	/**
	 * This is the first initialization event. Register tile entities here.
	 * The registry events below will have fired prior to entry to this method.
	 */
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		LOG = event.getModLog();

		GameRegistry.registerTileEntity(TileEntityGenerator.class, new ResourceLocation(MOD_ID, "container.generator"));

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
	}

	/**
	 * This is the second initialization event. Register custom recipes
	 */
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {

	}

	/**
	 * This is the final initialization event. Register actions from other mods here
	 */
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}

	/**
	 * Forge will automatically look up and bind blocks to the fields in this class
	 * based on their registry name.
	 */
	@GameRegistry.ObjectHolder(MOD_ID)
	public static class Blocks {
		public static final BlockGenerator GENERATOR = new BlockGenerator("generator", false);
		public static final BlockGenerator LIT_GENERATOR = (BlockGenerator) new BlockGenerator("lit_generator", true).setLightLevel(0.8F);
	}

	/**
	 * Forge will automatically look up and bind items to the fields in this class
	 * based on their registry name.
	 */
	@GameRegistry.ObjectHolder(MOD_ID)
	public static class Items {
		public static final ItemBlock GENERATOR = new ItemBlockGenerator();
		public static final Item BATTERY = new ItemBattery();
	}

	/**
	 * This is a special class that listens to registry events, to allow creation of mod blocks and items at the proper time.
	 */
	@Mod.EventBusSubscriber
	public static class ObjectRegistryHandler {
		/**
		 * Listen for the register event for creating custom items
		 */
		@SubscribeEvent
		public static void addItems(RegistryEvent.Register<Item> event) {
			IForgeRegistry<Item> registry = event.getRegistry();

			registry.register(Items.GENERATOR);
			registry.register(Items.BATTERY);
		}

		/**
		 * Listen for the register event for creating custom blocks
		 */
		@SubscribeEvent
		public static void addBlocks(RegistryEvent.Register<Block> event) {
			IForgeRegistry<Block> registry = event.getRegistry();

			registry.register(Blocks.GENERATOR);
			registry.register(Blocks.LIT_GENERATOR);
		}

		@SubscribeEvent
		public static void registerModels(ModelRegistryEvent event) {
			ModelLoader.setCustomModelResourceLocation(Items.BATTERY, 0, new ModelResourceLocation(new ResourceLocation(MOD_ID, "battery"), "inventory"));
			ModelLoader.setCustomModelResourceLocation(Items.GENERATOR, 0, new ModelResourceLocation(new ResourceLocation(MOD_ID , "generator"), "inventory"));
		}
	}
}
