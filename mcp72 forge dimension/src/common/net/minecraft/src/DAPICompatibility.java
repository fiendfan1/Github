package net.minecraft.src;

import java.lang.reflect.Method;
import java.util.HashMap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.client.Minecraft;

public class DAPICompatibility
{

	/**This class is used internally by Dimension API
	 * as a means of establishing greater compatibility
	 * among it, ModLoader, ModLoaderMP, and Forge.
	 * Modders should not call any of this class's
	 * methods.
	 */
	 
	private static Object invokeMethod(String s, String s1, Class aclass[], Object obj, Object aobj[])
    {
        ClassLoader classloader = (net.minecraft.src.DAPICompatibility.class).getClassLoader();

        try
        {
            Class class1 = classloader.loadClass(s);

            if (class1 != null)
            {
				return invokeMethod(class1, s1, aclass, obj, aobj);
            }
        }
        catch (Exception exception)
		{
		}

        return null;
    }
	
	private static Object invokeMethod(Class class1, String s, Class aclass[], Object obj, Object aobj[])
    {
        try
        {
            Method method = class1.getMethod(s, aclass);
            return method.invoke(obj, aobj);
        }
        catch (Exception exception)
		{
		}

        return null;
	}

	public static final boolean modLoaderInstalled()
    {
        try
        {
            Class.forName("ModLoader");
        }
        catch (ClassNotFoundException classnotfoundexception)
        {
			try
			{
				Class.forName("net.minecraft.src.ModLoader");
			}
			catch(ClassNotFoundException classnotfoundexception1)
			{
				return false;
			}
        }

        return true;
    }

    public static final boolean forgeInstalled()
    {
        try
        {
            Class.forName("mod_MinecraftForge");
        }
        catch (ClassNotFoundException classnotfoundexception)
        {
			try
			{
				Class.forName("net.minecraft.src.mod_MinecraftForge");
			}
			catch(ClassNotFoundException classnotfoundexception1)
			{
				return false;
			}
        }

        return true;
    }

	/**SERVER**/
    public static void minecraftServer_registerCommandsToModLoader(MinecraftServer minecraftserver)
    {
		ClassLoader classloader = (net.minecraft.src.DAPICompatibility.class).getClassLoader();
        Class class1 = null;
		try
        {
            class1 = classloader.loadClass("ModLoader");
        }
        catch (ClassNotFoundException e)
		{
			try
			{
				class1 = classloader.loadClass("net.minecraft.src.ModLoader");
			}
			catch(ClassNotFoundException e1)
			{
			}
		}
		if(class1 == null)
		{
			return;
		}
        invokeMethod(class1, "registerCommands", new Class[]
                {
                    net.minecraft.server.MinecraftServer.class
                }, null, new Object[]
                {
                    minecraftserver
                });
    }
	
	public static WorldProvider worldProvider_getForgeDimension(int i)
	{
		return (WorldProvider)invokeMethod("net.minecraft.src.forge.DimensionManager", "createProviderFor", new Class[]
		{
			java.lang.Integer.class
		}, null, new Object[]
		{
			i
		});
	}

    public static WorldChunkManager worldType_getChunkManager(WorldType worldtype, World world)
    {
		if(worldType_chunkManagerMap.containsKey(worldtype))
		{
			return worldType_chunkManagerMap.get(worldtype);
		}
        WorldChunkManager worldchunkmanager = (WorldChunkManager)invokeMethod(net.minecraft.src.WorldType.class, "getChunkManager", new Class[]
                {
                    net.minecraft.src.World.class
                }, worldtype, new Object[]
                {
                    world
                });
        WorldChunkManager worldchunkmanager1 =  ((WorldChunkManager)(worldchunkmanager != null ? worldchunkmanager : worldtype == WorldType.FLAT ? new WorldChunkManagerHell(BiomeGenBase.plains, 0.5F, 0.5F) : new WorldChunkManager(world)));
		worldType_chunkManagerMap.put(worldtype, worldchunkmanager1);
		return worldchunkmanager1;
	}

    public static IChunkProvider worldType_getChunkGenerator(WorldType worldtype, World world)
    {
        IChunkProvider ichunkprovider = (IChunkProvider)invokeMethod(net.minecraft.src.WorldType.class, "getChunkGenerator", new Class[]
                {
                    net.minecraft.src.World.class
                }, worldtype, new Object[]
                {
                    world
                });
        return ((IChunkProvider)(ichunkprovider != null ? ichunkprovider : worldtype == WorldType.FLAT ? new ChunkProviderFlat(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled()) : new ChunkProviderGenerate(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled())));
    }

    public static int worldType_getSeaLevel(WorldType worldtype, World world)
    {
		if(worldType_seaLevelMap.containsKey(worldtype))
		{
			return worldType_seaLevelMap.get(worldtype);
		}
        Integer integer = (Integer)invokeMethod(net.minecraft.src.WorldType.class, "getSeaLevel", new Class[]
                {
                    net.minecraft.src.World.class
                }, worldtype, new Object[]
                {
                    world
                });
        Integer integer1 = integer != null ? integer.intValue() : worldtype == WorldType.FLAT ? 4 : 64;
		worldType_seaLevelMap.put(worldtype, integer1);
		return integer1;
	}

	/**CLIENT**/
    public static boolean worldType_hasVoidParticles(WorldType worldtype, boolean flag)
    {
		if(worldType_voidParticleMap.containsKey(worldtype))
		{
			return worldType_voidParticleMap.get(worldtype);
		}
        Boolean boolean1 = (Boolean)invokeMethod(net.minecraft.src.WorldType.class, "hasVoidParticles", new Class[]
                {
                    java.lang.Boolean.class
                }, worldtype, new Object[]
                {
                    Boolean.valueOf(flag)
                });
        Boolean boolean2 = boolean1 != null ? boolean1.booleanValue() : worldtype != WorldType.FLAT && !flag;
		worldType_voidParticleMap.put(worldtype, boolean2);
		return boolean2;
	}

    public static double worldType_voidFadeMagnitude(WorldType worldtype)
    {
        Double double1 = (Double)invokeMethod(net.minecraft.src.WorldType.class, "voidFadeMagnitude", null, worldtype, null);
        Double double2 = double1 != null ? double1.doubleValue() : worldtype == WorldType.FLAT ? 1.0D : 0.03125D;
		worldType_voidFadeMap.put(worldtype, double2);
		return double2;
	}
	
	public static int guiIngame_getArmorValue(Minecraft minecraft)
	{
		int i = 0;
        for(int j = 0; j < minecraft.thePlayer.inventory.armorInventory.length; j++)
        {
            ItemStack itemstack = minecraft.thePlayer.inventory.armorInventory[j];
			ClassLoader classloader = (net.minecraft.src.DAPICompatibility.class).getClassLoader();
			Class class1 = null;
			try
			{
				class1 = classloader.loadClass("net.minecraft.src.ISpecialArmor");
			}
			catch(ClassNotFoundException e)
			{
				return minecraft.thePlayer.getTotalArmorValue();
			}
            if(itemstack != null && class1 != null && class1.isAssignableFrom(itemstack.getItem().getClass()))// && itemstack.getItem() instanceof ISpecialArmor)
            {
                i += (Integer)invokeMethod(class1, "getArmorDisplay", new Class[]
				{
					net.minecraft.src.EntityPlayerSP.class, net.minecraft.src.ItemStack.class, java.lang.Boolean.class
				}, itemstack.getItem(), new Object[]
				{
					minecraft.thePlayer, itemstack, j
				});
				//((ISpecialArmor)itemstack.getItem()).getArmorDisplay(minecraft.thePlayer, itemstack, j);
            } else
			if(itemstack != null && itemstack.getItem() instanceof ItemArmor)
            {
                i += ((ItemArmor)itemstack.getItem()).damageReduceAmount;
            }
        }
		return i;
	}
	
	private static HashMap<WorldType, WorldChunkManager> worldType_chunkManagerMap = new HashMap<WorldType, WorldChunkManager>();
	private static HashMap<WorldType, Integer> worldType_seaLevelMap = new HashMap<WorldType, Integer>();
	private static HashMap<WorldType, Boolean> worldType_voidParticleMap = new HashMap<WorldType, Boolean>();
	private static HashMap<WorldType, Double> worldType_voidFadeMap = new HashMap<WorldType, Double>();
	
}
