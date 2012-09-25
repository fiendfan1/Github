package nightmare.common;
import java.util.Random;

import net.minecraft.src.BaseMod;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.DimensionAPI;
import net.minecraft.src.EnumCreatureType;
import net.minecraft.src.ItemTeleporterBase;
import net.minecraft.src.ModLoader;
import net.minecraft.src.World;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.*;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "Nightmare_Mod", name = "Nightmare Mod", version = "1.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)

public class mod_Nightmare
{
	public static net.minecraft.src.Block nightmareGrass;
	public static net.minecraft.src.Block nightmareStone;
	public static net.minecraft.src.Block nightmareLog;
	public static net.minecraft.src.Block nightmareLeaf;
	public static net.minecraft.src.Block nightmareBed;
	public static BlockPortalNightmare nightmarePortal;
	public static net.minecraft.src.Item nightmareTeleportStick;
	public static net.minecraft.src.Item nightmareItem;
	
	
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
		nightmareGrass = new BlockNightmareGrass(230, 0).setHardness(5F).setResistance(5F).setBlockName("NightMareGrass");
		nightmareTeleportStick = new ItemTeleportStick(235).setItemName("TPStick").setTabToDisplayOn(CreativeTabs.tabMisc);
		nightmareItem = new ItemNightmare(236).setItemName("TPtick").setTabToDisplayOn(CreativeTabs.tabMisc);
		nightmareBed = new BlockBed(234).setHardness(5F).setResistance(5F).setBlockName("NightMareBed").setCreativeTab(CreativeTabs.tabBlock);
		nightmareStone = new BlockNightmareStone(231, ModLoader.addOverride("/terrain.png", "/Blocks/nightmareStone.png")).setHardness(5F).setResistance(5F).setBlockName("NightMareStone");
		nightmareLog = new BlockNightmareLog(232).setHardness(5F).setResistance(5F).setBlockName("NightMareLog").setCreativeTab(CreativeTabs.tabBlock);
		nightmareLeaf = new BlockNightmareLeaf(233, 52).setHardness(5F).setResistance(5F).setBlockName("NightMareLog").setCreativeTab(CreativeTabs.tabBlock);
		nightmarePortal = (BlockPortalNightmare) (new BlockPortalNightmare(216).setHardness(0F).setResistance(0F).setBlockName("NightMarePortal").setCreativeTab(CreativeTabs.tabBlock));
		
		
		GameRegistry.registerBlock(nightmareGrass);
		GameRegistry.registerBlock(nightmareBed);
		GameRegistry.registerBlock(nightmarePortal);
		GameRegistry.registerBlock(nightmareStone);
		GameRegistry.registerBlock(nightmareLog);
		GameRegistry.registerBlock(nightmareLeaf);
		
		
		LanguageRegistry.addName(nightmareTeleportStick, "Nightmare Teleport Stick");
		LanguageRegistry.addName(nightmareItem, "Nightmare Item");
		LanguageRegistry.addName(nightmareBed, "Nightmare Bed");
		LanguageRegistry.addName(nightmareGrass, "Nightmare Grass");
		LanguageRegistry.addName(nightmarePortal, "Nightmare Portal");
		LanguageRegistry.addName(nightmareStone, "Nightmare Stone");
		LanguageRegistry.addName(nightmareLog, "Nightmare Log");
		LanguageRegistry.addName(nightmareLeaf, "Nightmare Leaf");
		
		
		logTop = ModLoader.addOverride("/terrain.png", "/Blocks/nightmareLogTop.png");
		logSide = ModLoader.addOverride("/terrain.png", "/Blocks/nightmareLogSide.png");
		grassTop = ModLoader.addOverride("/terrain.png", "/Blocks/nightmareGrassTop.png");
		grassSide = ModLoader.addOverride("/terrain.png", "/Blocks/nightmareGrassSide.png");
		forbiddenGrassBottom = ModLoader.addOverride("/terrain.png", "/Blocks/nightmareGrassBottom.png");
		
		
		ModLoader.registerEntityID(EntityBiped.class, "BipedGuy", ModLoader.getUniqueEntityId());
		ModLoader.addSpawn("BipedGuy", 1, 1, 5, EnumCreatureType.monster, new BiomeGenBase[]{
				BiomeGenNightmare.ForbiddenForest
		});
		
		
		DimensionAPI.registerDimension(new WorldProviderNightmare());
	}
	
}
