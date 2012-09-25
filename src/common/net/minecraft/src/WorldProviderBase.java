package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import java.util.List;

public abstract class WorldProviderBase extends WorldProvider
{
    public WorldProviderBase()
    {
        worldType = getDimensionID();
    }

	/**The ID of the dimension; select an ID that 
	 * is unlikely to be utilized by another modder.
	 */
    public abstract int getDimensionID();

	/**The following three booleans are basically
	 * self-explanatory.
	 */
    public boolean renderClouds()
    {
        return true;
    }

    public boolean renderVoidFog()
    {
        return true;
    }
	
	public boolean renderStars()
    {
        return true;
    }

	/**Can be used to render additional objects
	 * in the sky.
	 *
	 * @param f		the count of partial ticks used in rendering
	 */
    public void renderCelestialObjects(float f)
    {
    }

	/**Sets the size of the sun relative to its
	 * current size; a return of 2.0F will render
	 * a sun twice as large as the present one.
	 */
    public float setSunSize()
    {
        return 1.0F;
    }

	/**Sets the size of the moon relative to its
	 * current size; a return of 0.5F will render
	 * a moon half as large as the present one.
	 */
    public float setMoonSize()
    {
        return 1.0F;
    }

	/**Returns the path of a custom sun texture.
	 */
    public String getSunTexture()
    {
        return "/terrain/sun.png";
    }

	/**Returns the path of a custom moon texture.
	 */
    public String getMoonTexture()
    {
        return "/terrain/moon_phases.png";
    }

	/**Returns the brightness of the stars, which
	 * is normally determined by the time of day;
	 * can be used to render stars in the daylight
	 * by returning a constant value of 1.0F.
	 *
	 * @param world		the current client world
	 * @param f			the count of partial ticks used in rendering
	 */
    public float getStarBrightness(World world, float f)
    {
        return world.getStarBrightness(f);
    }

	/**Used potentially for special color effects
	 * in the sky, or for overriding normal biome
	 * sky color. Defining the sky color in a dimension's
	 * custom biomes is usually best.
	 *
	 * @param world		the current client world
	 * @param entity	the entity (player) that is viewing the sky
	 * @param f			the count of partial ticks used in rendering
	 */
    public Vec3/*D*/ getSkyColor(World world, Entity entity, float f)
    {
        return world.getSkyColor(entity, f);
    }

	/**Returning false will prevent the sky from
	 * darkening during a period of rainfall. Note:
	 * this method has been faulty in the past.
	 */
    public boolean darkenSkyDuringRain()
    {
        return true;
    }

	/**@deprecated
	 * For now at least, this method is no longer used.
	 */
    public String getRespawnMessage()
    {
        return "Respawning";
    }
	
	/**The ID of the dimension in which the player
	 * respawns after dying in this dimension.
	 */
	public int respawnInDimension()
	{
		return 0;
	}
	
	/**Returning true will skip normal sky rendering
	 * and render the End's sky in its place.
	 */
	public boolean renderEndSky()
	{
		return false;
	}

    public boolean getWorldHasNoSky()
    {
        return renderVoidFog();
    }
	
	/**Defines the name of the dimension's save
	 * folder; the default is the "DIM*dimension ID*"
	 * format of the Nether and End.
	 */
	public String getSaveFolderName()
	{
		return (new StringBuilder()).append("DIM").append(getDimensionID()).toString();
	}
	
	/**Associates a custom version of the RenderGlobal
	 * class with this dimension; returning
	 * RenderGlobalCustom enables all of the sky rendering
	 * hooks to work. Overriding any of its methods in a 
	 * subclass may disable some of these hooks.
	 */
	public RenderGlobalCustom getGlobalRenderer(Minecraft minecraft, RenderEngine renderengine)
	{
		return new RenderGlobalCustom(minecraft, renderengine);
	}
	
	/**Return true if the dimension is supposed to contain
	 * multiple biomes (like the overworld), and false if it 
	 * is supposed to possess only a single, universal biome
	 * (like the Nether).
	 */
	public boolean hasMultipleBiomes()
	{
		return false;
	}
	
	/**Returns an array of large, "major" biomes, such
	 * as forest, desert, or taiga in the overworld.
	 *
	 * @see GenLayerBiomes (for the array of biomes used in the overworld)
	 */
	public BiomeGenBase[] setMajorBiomes()
	{
		return null;
	}
	
	/**Returns a "minor" biome based on conditions of
	 * modder's choosing. Examples of such biomes in
	 * the overworld include the hilly or mountainous areas 
	 * within larger biomes such as tundra or jungle, as
	 * well as the splotches of forest across the plains.
	 *
	 * @param biomegenbase	the "major" biome(s) on top of which this biome generates
	 * @param genlayer		the GenLayer generating these biomes; use to retrieve the 
	 *							genlayer.nextInt() method for random biome generation
	 *							instead of random.nextInt()
	 * @see GenLayerHills (for the overworld's minor biome generation)
	 */
	public BiomeGenBase setMinorBiomes(BiomeGenBase biomegenbase, GenLayer genlayer)
	{
		return null;
	}
	
	/**Returns a biome generated in the pattern of
	 * overworld oceans; note that this does not mean
	 * the biome generated is or must be an actual ocean 
	 * biome.
	 *
	 * @params See the "setMinorBiomes" method.
	 */
	public BiomeGenBase setOceanBiomes(BiomeGenBase biomegenbase, GenLayer genlayer)
	{
		return null;
	}
	
	/**Returns a biome generated in the pattern of
	 * overworld rivers; note that this does not mean
	 * the biome generated is or must be an actual river 
	 * biome.
	 *
	 * @params See the "setMinorBiomes" method.
	 */
	public BiomeGenBase setRiverBiomes(BiomeGenBase biomegenbase, GenLayer genlayer)
	{
		return null;
	}
	
	/**Returns a biome generated consistently along the
	 * border of two separate biomes.
	 *
	 * @param biomegenbase		the biome on one side of the border
	 * @param biomegenbase1		the biome on the other side of the border
	 * @param genlayer			See the "setMinorBiomes" method.
	 */
	public BiomeGenBase setBorderBiomes(BiomeGenBase biomegenbase, BiomeGenBase biomegenbase1, GenLayer genlayer)
	{
		return null;
	}
	
	/**Returns a biome that can be generated anywhere;
	 * only the rarity of the biome, not its generation
	 * overtop of other biomes, can be defined.
	 *
	 * @param genlayer		See the "setMinorBiomes" method.
	 */
	public BiomeGenBase setMiscellaneousBiomes(GenLayer genlayer)
	{
		return null;
	}
	
	/**Does not work with generic portals, but is
	 * included for possible future expansion.
	 */
	public List getBiomesToSpawnIn()
	{
		return null;
	}
	
	/**A vanilla method used to register the
	 * WorldChunkManager associated with this
	 * dimension. The WorldChunkManager is used
	 * primarily for conducting biome generation.
	 * A custom version of this class can be
	 * created, but the premade one is used to
	 * apply the biome gen code above. For dimensions
	 * with a single universal biomes, using
	 * WorldChunkManagerHell would probably suffice.
	 */
	public void registerWorldChunkManager()
    {
		if(hasMultipleBiomes())
		{
			worldChunkMgr = new WorldChunkManagerCustom(worldObj, this);
		} else
		{
			super.registerWorldChunkManager();
		}
	}
	
	/**Called before the player is respawned.
	 *
	 * @param minecraftserver		the current server instance
	 * @param entityplayermp		the player being teleported
	 */
	public void beforeRespawnTeleportation(MinecraftServer minecraftserver, EntityPlayerMP entityplayermp)
	{
	}
	
	/**Called after the player is respawned.
	 *
	 * @param minecraftserver		the current server instance
	 * @param entityplayermp		the player being teleported
	 */
	public void afterRespawnTeleportation(MinecraftServer minecraftserver, EntityPlayerMP entityplayermp)
	{
	}
	
	/**Can override a player's respawning completely
	 * after he or she has just died in this dimension;
	 * returns a new (living) instance of that player.
	 *
	 * @param minecraftserver		the current server instance
	 * @param serverconfigurationmanager the class from which this method is called and whose code it overrides
	 * @param entityplayermp		the player to respawn
	 * @param i						the ID of the dimension in which the player is supposed to respawn (zero)
	 * @param flag					true if the player has just won the game and is respawning from that instead of death
	 * @see ServerConfigurationManager (for the original respawning code)
	 */
	public EntityPlayerMP overrideRespawn(MinecraftServer minecraftserver, ServerConfigurationManager serverconfigurationmanager,
		EntityPlayerMP entityplayermp, int i, boolean flag)
	{
		return null;
	}

}
