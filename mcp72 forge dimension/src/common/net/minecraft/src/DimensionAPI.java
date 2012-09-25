package net.minecraft.src;

import java.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class DimensionAPI
{
    public static HashMap<WorldProviderBase, Integer> dimensionMap = new HashMap<WorldProviderBase, Integer>();
	public static HashMap<Integer, Integer> numberMap = new HashMap<Integer, Integer>();
	public static HashMap<Integer, IItemUse[]> itemUseMap = new HashMap<Integer, IItemUse[]>();
	public static HashMap<Integer, IItemRightClick[]> itemRightClickMap = new HashMap<Integer, IItemRightClick[]>();
	public static HashMap<Integer, IBlockAdded[]> blockAddedMap = new HashMap<Integer, IBlockAdded[]>();
	public static HashMap<Integer, IBlockRemoval[]> blockRemovalMap = new HashMap<Integer, IBlockRemoval[]>();
	public static ArrayList<IRenderOverlay> overlayList = new ArrayList<IRenderOverlay>();
	public static int timeInPortal = 0;
	public static ISpecialTeleportation currentPortal;
	public static int numberOfDimensions = 3;
	private static WorldClient currentWorld = null;

    public DimensionAPI()
    {
    }

	/**All custom dimensions must be registered in
	 * order to work properly. Preferably they should
	 * be registered when the game loads, using
	 * ModLoader or Forge to achieve this. Obviously
	 * all dimension require a WorldProvider, which is
	 * basically the central class of the dimension's
	 * code.
	 *
	 * @param worldproviderbase		the dimension's WorldProvider
	 * @see WorldProviderBase (to build the basics of a dimension)
	 */
    public static void registerDimension(WorldProviderBase worldproviderbase)
    {	
		int i = worldproviderbase.getDimensionID();
        dimensionMap.put(worldproviderbase, Integer.valueOf(i));
        int j = numberOfDimensions;
        numberMap.put(Integer.valueOf(i), Integer.valueOf(j));
        numberOfDimensions++;
		System.out.println("Registering dimension " + worldproviderbase.getClass().getSimpleName() 
			+ " with ID " + i + " at world slot " + j + ".");
    }
	
	/**This method executes the countdown between
	 * the player's entering the portal and his or
	 * her teleportation. Portals that have no delay
	 * skip straight to teleportation.
	 *
	 * @param worldproviderbase		the dimension's WorldProvider
	 * @param minecraftserver		the current server instance
	 * @param entity				the entity (player) to be teleported
	 * @param ispecialteleportation the teleportation mechanism currently being used
	 * @param blockportalbase		the portal block (if applicable) from which the player is teleporting
	 * @see ISpecialTeleportation, BlockPortalBase, ItemTeleporterBase (to trigger a teleportation event)
	 */
	public static void setInPortal(WorldProviderBase worldproviderbase, MinecraftServer minecraftserver, Entity entity, ISpecialTeleportation ispecialteleportation, BlockPortalBase blockportalbase)
    {
        EntityPlayerMP entityplayermp = (EntityPlayerMP)entity;
        int j = entityplayermp.dimension;
		if(blockportalbase != null && !isInPortal(entityplayermp, blockportalbase.blockID))
		{
			timeInPortal = 0;
			return;
		}
        if ((ispecialteleportation.canTeleportFromDimension().contains(Integer.valueOf(j)) || j == worldproviderbase.getDimensionID()))
        {
            if (ispecialteleportation.isPortalImmediate())
            {
                usePortal(worldproviderbase, minecraftserver, entityplayermp, ispecialteleportation);
            }
            else
            {
                timeInPortal++;
                if (timeInPortal == ispecialteleportation.getPortalDelay() && entityplayermp.timeUntilPortal <= 0)
                {
                    timeInPortal = 0;
					entityplayermp.timeUntilPortal = 10;
                    usePortal(worldproviderbase, minecraftserver, entityplayermp, ispecialteleportation);
                }
            }
        }
    }

	/**Detects whether a player is still within
	 * the bounds of a custom portal block.
	 *
	 * @param entityplayer		the player to be teleported
	 * @param i					the ID of the portal block
	 */
    public static boolean isInPortal(EntityPlayer entityplayer, int i)
    {
        int j = (int)Math.floor(entityplayer.posX);
        int k = (int)Math.floor(entityplayer.posY);
        int l = (int)Math.floor(entityplayer.posZ);
        if(entityplayer.worldObj.getBlockId(j, k, l) == i || entityplayer.worldObj.getBlockId(j, k - 1, l) == i)
		{
			return true;
		}
		return false;
    }

	/**This method is used to determine the player's
	 * "direction of travel," either toward a custom
	 * dimension or away from it.
	 *
	 * @params See the "setInPortal" method.
	 */
    public static void usePortal(WorldProviderBase worldproviderbase, MinecraftServer minecraftserver, EntityPlayerMP entityplayermp, ISpecialTeleportation ispecialteleportation)
    {
        int i = entityplayermp.dimension;
        if (entityplayermp.dimension != worldproviderbase.getDimensionID())
        {
            teleportToDimension(worldproviderbase, minecraftserver, entityplayermp, ispecialteleportation);
        }
        else if (entityplayermp.dimension == worldproviderbase.getDimensionID())
        {
            teleportFromDimension(worldproviderbase, minecraftserver, entityplayermp, ispecialteleportation);
        }
    }

	/**Used to make the initial trip to a modded
	 * dimension.
	 *
	 * @params See the "setInPortal" method.
	 */
    public static void teleportToDimension(WorldProviderBase worldproviderbase, MinecraftServer minecraftserver, EntityPlayerMP entityplayermp, ISpecialTeleportation ispecialteleportation)
    {
		ispecialteleportation.beforeTeleportToDimension(minecraftserver, entityplayermp);
        int oldDimension = entityplayermp.dimension;
		WorldServer worldserver = minecraftserver.worldServerForDimension(entityplayermp.dimension);
        entityplayermp.dimension = worldproviderbase.getDimensionID();
        WorldServer worldserver1 = minecraftserver.worldServerForDimension(worldproviderbase.getDimensionID());
        entityplayermp.serverForThisPlayer.sendPacketToPlayer(new Packet9Respawn((byte)entityplayermp.dimension, (byte)entityplayermp.worldObj.difficultySetting, worldserver1.getWorldInfo().getTerrainType(), worldserver1.getHeight(), entityplayermp.theItemInWorldManager.getGameType()));
        worldserver.removeEntity(entityplayermp);
        entityplayermp.isDead = false;
        double d = 1.0D / ispecialteleportation.getDistanceRatio();
        double d1 = entityplayermp.posX;
        double d2 = entityplayermp.posZ;
        d1 *= d;
        d2 *= d;
        entityplayermp.setLocationAndAngles(d1, entityplayermp.posY, d2, entityplayermp.rotationYaw, entityplayermp.rotationPitch);
        if (entityplayermp.isEntityAlive())
        {
            worldserver.updateEntityWithOptionalForce(entityplayermp, false);
            worldserver1.spawnEntityInWorld(entityplayermp);
            entityplayermp.setLocationAndAngles(entityplayermp.posX, entityplayermp.posY, entityplayermp.posZ, entityplayermp.rotationYaw, entityplayermp.rotationPitch);
            worldserver1.updateEntityWithOptionalForce(entityplayermp, false);
            worldserver1.theChunkProviderServer.loadChunkOnProvideRequest = true;
            ispecialteleportation.getTeleporter(oldDimension, entityplayermp.dimension).placeInPortal(worldserver1, entityplayermp);
            worldserver1.theChunkProviderServer.loadChunkOnProvideRequest = false;
        }
        ServerConfigurationManager serverconfigurationmanager = minecraftserver.getConfigurationManager();
        entityplayermp.setWorld(worldserver1);
		serverconfigurationmanager.func_72375_a(entityplayermp, worldserver);
        entityplayermp.serverForThisPlayer.setPlayerLocation(entityplayermp.posX, entityplayermp.posY, entityplayermp.posZ, entityplayermp.rotationYaw, entityplayermp.rotationPitch);
        entityplayermp.theItemInWorldManager.setWorld(worldserver1);
        serverconfigurationmanager.sendTimeAndRainingToPlayer(entityplayermp, worldserver1);
        serverconfigurationmanager.syncPlayerInventory(entityplayermp);
        if (ispecialteleportation.triggerAchievement() != null)
        {
            entityplayermp.addStat(ispecialteleportation.triggerAchievement(), 1);
        }
		PotionEffect potioneffect;
		for (Iterator iterator = entityplayermp.getActivePotionEffects().iterator(); iterator.hasNext(); entityplayermp.serverForThisPlayer.sendPacketToPlayer(new Packet41EntityEffect(entityplayermp.entityId, potioneffect)))
        {
            potioneffect = (PotionEffect)iterator.next();
        }
		ispecialteleportation.afterTeleportToDimension(minecraftserver, entityplayermp);
	}
	
	/**Used to make a trip back from the modded
	 * dimension.
	 *
	 * @params See the "setInPortal" method.
	 */
	public static void teleportFromDimension(WorldProviderBase worldproviderbase, MinecraftServer minecraftserver, EntityPlayerMP entityplayermp, ISpecialTeleportation ispecialteleportation)
    {
		ispecialteleportation.beforeTeleportFromDimension(minecraftserver, entityplayermp);
		int oldDimension = entityplayermp.dimension;
		int i = ispecialteleportation.returnsPlayerToDimension();
        WorldServer worldserver = minecraftserver.worldServerForDimension(worldproviderbase.getDimensionID());
        entityplayermp.dimension = i;
        WorldServer worldserver1 = minecraftserver.worldServerForDimension(i);
        entityplayermp.serverForThisPlayer.sendPacketToPlayer(new Packet9Respawn((byte)entityplayermp.dimension, (byte)entityplayermp.worldObj.difficultySetting, worldserver1.getWorldInfo().getTerrainType(), worldserver1.getHeight(), entityplayermp.theItemInWorldManager.getGameType()));
        worldserver.removeEntity(entityplayermp);
        entityplayermp.isDead = false;
		double d = ispecialteleportation.getDistanceRatio();
        double d1 = entityplayermp.posX;
        double d2 = entityplayermp.posZ;
        d1 *= d;
        d2 *= d;
        entityplayermp.setLocationAndAngles(d1, entityplayermp.posY, d2, entityplayermp.rotationYaw, entityplayermp.rotationPitch);
        if(entityplayermp.isEntityAlive())
        {
            worldserver.updateEntityWithOptionalForce(entityplayermp, false);
			worldserver1.spawnEntityInWorld(entityplayermp);
			entityplayermp.setLocationAndAngles(entityplayermp.posX, entityplayermp.posY, entityplayermp.posZ, entityplayermp.rotationYaw, entityplayermp.rotationPitch);
			worldserver1.updateEntityWithOptionalForce(entityplayermp, false);
			worldserver1.theChunkProviderServer.loadChunkOnProvideRequest = true;
			ispecialteleportation.getTeleporter(oldDimension, entityplayermp.dimension).placeInPortal(worldserver1, entityplayermp);
			worldserver1.theChunkProviderServer.loadChunkOnProvideRequest = false;
		}
        ServerConfigurationManager serverconfigurationmanager = minecraftserver.getConfigurationManager();
        entityplayermp.setWorld(worldserver1);
		serverconfigurationmanager.func_72375_a(entityplayermp, worldserver);
        entityplayermp.serverForThisPlayer.setPlayerLocation(entityplayermp.posX, entityplayermp.posY, entityplayermp.posZ, entityplayermp.rotationYaw, entityplayermp.rotationPitch);
        entityplayermp.theItemInWorldManager.setWorld(worldserver1);
        serverconfigurationmanager.sendTimeAndRainingToPlayer(entityplayermp, worldserver1);
        serverconfigurationmanager.syncPlayerInventory(entityplayermp);
		PotionEffect potioneffect;
		for (Iterator iterator = entityplayermp.getActivePotionEffects().iterator(); iterator.hasNext(); entityplayermp.serverForThisPlayer.sendPacketToPlayer(new Packet41EntityEffect(entityplayermp.entityId, potioneffect)))
        {
            potioneffect = (PotionEffect)iterator.next();
        }
		ispecialteleportation.afterTeleportFromDimension(minecraftserver, entityplayermp);
    }

	/**A utility method employed internally by the API.
	 *
	 * @param map		the map from which the key will be retrieved
	 * @param obj		the value corresponding to that key
	 */
    public static Object getKeyFromValue(Map map, Object obj)
    {
        for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();)
        {
            Object obj1 = iterator.next();
            if (map.get(obj1).equals(obj))
            {
                return obj1;
            }
        }

        return null;
    }

	/**Used to retrieve a modded dimension's WorldProvider
	 * by its ID number.
	 *
	 * @param i		the ID of the dimension
	 */
    public static WorldProvider getProviderByDimension(int i)
    {
        WorldProvider worldprovider = (WorldProvider)getKeyFromValue(dimensionMap, new Integer(i));
        return worldprovider;
    }
	
	/**Generates a custom-colored Nether portal-like texture.
	 *
	 * @param block		the block associated with this texture
	 * @param f			the red value of the texture
	 * @param f1		the green value
	 * @param f2		the blue value
	 * @param f3		the alpha value
	 */
	@SideOnly(Side.CLIENT)
	public static void registerPortalTexture(Block block, float f, float f1, float f2, float f3)
	{
		Minecraft.getMinecraft().renderEngine.registerTextureFX(new TextureCustomPortalFX(block.blockIndexInTexture, f, f1, f2, f3));
	}
	
	/**Registers an implementation of the IItemUse
	 * interface, which intercepts the right-clicking
	 * of a block with an item.
	 *
	 * @param i			the item's ID (shiftedIndex)
	 * @param iitemuse	the implementation of the IItemUse interface
	 */
	public static void addItemUseIntercept(int i, IItemUse iitemuse)
	{
		IItemUse[] array = itemUseMap.get(i);
		if(array == null)
		{
			IItemUse[] array1 = new IItemUse[1];
			array1[0] = iitemuse;
			itemUseMap.put(i, array1);
		} else
		{
			IItemUse[] array2 = new IItemUse[array.length + 1];
			for(int j = 0; j < array.length; j++)
			{
				array2[j] = array[j];
			}
			array2[array.length] = iitemuse;
			itemUseMap.put(i, array2);
		}
	}
	
	/**Registers an implementation of the IItemRightClick
	 * interface, which intercepts the right-clicking
	 * of an item when not used on a block.
	 *
	 * @param i					the item's ID (shiftedIndex)
	 * @param iitemrightclick	the implementation of the IItemRightClick interface
	 */
	public static void addItemRightClickIntercept(int i, IItemRightClick iitemrightclick)
	{
		IItemRightClick[] array = itemRightClickMap.get(i);
		if(array == null)
		{
			IItemRightClick[] array1 = new IItemRightClick[1];
			array1[0] = iitemrightclick;
			itemRightClickMap.put(i, array1);
		} else
		{
			IItemRightClick[] array2 = new IItemRightClick[array.length + 1];
			for(int j = 0; j < array.length; j++)
			{
				array2[j] = array[j];
			}
			array2[array.length] = iitemrightclick;
			itemRightClickMap.put(i, array2);
		}
	}
	
	/**Registers an implementation of the IBlockAdded
	 * interface, which intercepts the addition of a
	 * block to the world (whether through the player's
	 * placing of that block or other means).
	 *
	 * @param i				the block's ID (blockID)
	 * @param iblockadded	the implementation of the IBlockAdded interface
	 */
	public static void addBlockAddedIntercept(int i, IBlockAdded iblockadded)
	{
		IBlockAdded[] array = blockAddedMap.get(i);
		if(array == null)
		{
			IBlockAdded[] array1 = new IBlockAdded[1];
			array1[0] = iblockadded;
			blockAddedMap.put(i, array1);
		} else
		{
			IBlockAdded[] array2 = new IBlockAdded[array.length + 1];
			for(int j = 0; j < array.length; j++)
			{
				array2[j] = array[j];
			}
			array2[array.length] = iblockadded;
			blockAddedMap.put(i, array2);
		}
	}
	
	/**Registers an implementation of the IBlockRemoval
	 * interface, which intercepts the deletion of a
	 * block from the world (whether through the player's
	 * destruction of that block or other means).
	 *
	 * @param i				the block's ID (blockID)
	 * @param iblockremoval	the implementation of the IBlockRemoval interface
	 */
	public static void addBlockRemovalIntercept(int i, IBlockRemoval iblockremoval)
	{
		IBlockRemoval[] array = blockRemovalMap.get(i);
		if(array == null)
		{
			IBlockRemoval[] array1 = new IBlockRemoval[1];
			array1[0] = iblockremoval;
			blockRemovalMap.put(i, array1);
		} else
		{
			IBlockRemoval[] array2 = new IBlockRemoval[array.length + 1];
			for(int j = 0; j < array.length; j++)
			{
				array2[j] = array[j];
			}
			array2[array.length] = iblockremoval;
			blockRemovalMap.put(i, array2);
		}
	}
	
	/**Spawns a custom Nether portal particle-like effect.
	 *
	 * @param world		the current (client) world
	 * @param red		the red value of the particle
	 * @param green		the green value
	 * @param blue		the blue value
	 * @param posX		the starting position of the particle relative to the X axis...
	 * @param posY		... to the Y axis...
	 * @param posZ		... and to the Z axis
	 * @param motionX	the motion of the particle along the X axis...
	 * @param motionY	... the Y axis...
	 * @param motionZ	... and the Z axis
	 * @see BlockPortal for a good way to spawn these particles en masse.
	 */
	@SideOnly(Side.CLIENT)
	public static void spawnPortalParticle(World world, float red, float green, float blue, double posX, double posY, double posZ, double motionX, double motionY, double motionZ)
	{
		Minecraft.getMinecraft().effectRenderer.addEffect(new EntityCustomPortalFX(world, red, green, blue, posX, posY, posZ, motionZ, motionY, motionZ));
	}
	
	/**Registers an implementation of the IRenderOverlay
	 * interface, which renders an overlay atop the player's
	 * field of vision (like the pumpkin helmet effect)
	 * when certain conditions (defined by the modder) are
	 * met.
	 *
	 * @param irenderoverlay	the implementation of the IRenderOverlay interface
	 * @see GuiIngame (for examples of ingame overlays)
	 */
	@SideOnly(Side.CLIENT)
	public static void registerOverlay(IRenderOverlay irenderoverlay)
	{
		overlayList.add(irenderoverlay);
	}
	
	/**A method used internally by the API to switch
	 * between instances of the RenderGlobal class; should
	 * not be called by modders.
	 *
	 * @param minecraft		the current instance of the Minecraft client
	 */
	@SideOnly(Side.CLIENT)
	public static void renderTick(Minecraft minecraft)
	{
		currentWorld = minecraft.theWorld;
		if(currentWorld == null)
		{
			return;
		}
		if(currentWorld.provider instanceof WorldProviderBase)
		{
			WorldProviderBase worldproviderbase = ((WorldProviderBase)currentWorld.provider);
			if(!(minecraft.renderGlobal instanceof RenderGlobalCustom))
			{
				minecraft.renderGlobal = worldproviderbase.getGlobalRenderer(minecraft, minecraft.renderEngine);
				minecraft.renderGlobal.setWorldAndLoadRenderers(currentWorld);
			}
		} else
		if(minecraft.renderGlobal instanceof RenderGlobalCustom)
		{
			minecraft.renderGlobal = new RenderGlobal(minecraft, minecraft.renderEngine);
			minecraft.renderGlobal.setWorldAndLoadRenderers(currentWorld);
		}
	}
	
	/**Returns the full version designation of
	 * Dimension API.
	 */
	public static String getVersion()
	{
		return "Dimension API Client 1.6.1 for Minecraft 1.3.2";
	}

}
