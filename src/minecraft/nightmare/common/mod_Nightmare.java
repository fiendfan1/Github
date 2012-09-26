package nightmare.common;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.DimensionAPI;
import net.minecraft.src.EnumCreatureType;
import net.minecraft.src.Item;
import net.minecraft.src.ModLoader;
import net.minecraftforge.client.MinecraftForgeClient;
import nightmare.common.blocks.BlockBed;
import nightmare.common.blocks.BlockNightmareGrass;
import nightmare.common.blocks.BlockNightmareLeaf;
import nightmare.common.blocks.BlockNightmareLog;
import nightmare.common.blocks.BlockNightmareStone;
import nightmare.common.blocks.BlockPortalNightmare;
import nightmare.common.entities.EntityBiped;
import nightmare.common.items.ItemNightmare;
import nightmare.common.items.ItemTeleportStick;
import nightmare.common.world.BiomeGenNightmare;
import nightmare.common.world.WorldProviderNightmare;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "Nightmare_Mod", name = "Nightmare Mod", version = "1.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)

public class mod_Nightmare
{
	public static final Block nightmareGrass = new BlockNightmareGrass(230, 0).setHardness(5F).setResistance(5F).setBlockName("NightMareGrass");
	public static final Item nightmareTeleportStick = new ItemTeleportStick(235).setIconIndex(0).setItemName("TPStick").setTabToDisplayOn(CreativeTabs.tabMisc);
	public static final Item nightmareItem = new ItemNightmare(236).setIconIndex(0).setItemName("TPtick").setTabToDisplayOn(CreativeTabs.tabMisc);
	public static final Block nightmareBed = new BlockBed(234).setHardness(5F).setResistance(5F).setBlockName("NightMareBed").setCreativeTab(CreativeTabs.tabBlock);
	public static final Block nightmareStone = new BlockNightmareStone(231, ModLoader.addOverride("/terrain.png", "/Blocks/nightmareStone.png")).setHardness(5F).setResistance(5F).setBlockName("NightMareStone");
	public static final Block nightmareLog = new BlockNightmareLog(232, 3).setHardness(5F).setResistance(5F).setBlockName("NightMareLog").setCreativeTab(CreativeTabs.tabBlock);
	public static final Block nightmareLeaf = new BlockNightmareLeaf(233, 52).setHardness(5F).setResistance(5F).setBlockName("NightMareLog").setCreativeTab(CreativeTabs.tabBlock);
	public static final Block nightmarePortal = new BlockPortalNightmare(216).setHardness(0F).setResistance(0F).setBlockName("NightMarePortal").setCreativeTab(CreativeTabs.tabBlock);
	
	
	public static int grassTop;
	public static int grassSide;
	public static int spiderHeadBlock;
	public static int spiderHeadSide;
	public static int spiderHeadBlockLight;
	public static int spiderHeadSideLight;
	public static int logTop;
	public static int logSide;
	public static int Doublingden;
	public static int forbiddenGrassBottom;
	
	
	
	@Init
	public void load(FMLInitializationEvent event)
	{
		MinecraftForgeClient.preloadTexture("/Blocks/NightmareTextures.png");
		addNames();
		addRecipes();
		registerBlocks();
		registerEntities();
		addSpawns();
		DimensionAPI.registerDimension(new WorldProviderNightmare());
	}
	
	public void addNames()
	{
		LanguageRegistry.addName(nightmareTeleportStick, "Nightmare Teleport Stick");
		LanguageRegistry.addName(nightmareItem, "Nightmare Item");
		LanguageRegistry.addName(nightmareBed, "Nightmare Bed");
		LanguageRegistry.addName(nightmareGrass, "Nightmare Grass");
		LanguageRegistry.addName(nightmarePortal, "Nightmare Portal");
		LanguageRegistry.addName(nightmareStone, "Nightmare Stone");
		LanguageRegistry.addName(nightmareLog, "Nightmare Log");
		LanguageRegistry.addName(nightmareLeaf, "Nightmare Leaf");
	}
	
	public void addRecipes()
	{
		
	}
	
	public void registerBlocks()
	{
		GameRegistry.registerBlock(nightmareGrass);
		GameRegistry.registerBlock(nightmareBed);
		GameRegistry.registerBlock(nightmarePortal);
		GameRegistry.registerBlock(nightmareStone);
		GameRegistry.registerBlock(nightmareLog);
		GameRegistry.registerBlock(nightmareLeaf);
	}
	
	public void registerEntities()
	{
		ModLoader.registerEntityID(EntityBiped.class, "BipedGuy", ModLoader.getUniqueEntityId());
	}
	
	public void addSpawns()
	{
		ModLoader.addSpawn("BipedGuy", 1, 1, 5, EnumCreatureType.monster, new BiomeGenBase[]{
				BiomeGenNightmare.ForbiddenForest
		});
	}
}
