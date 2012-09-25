package net.minecraft.src;

import java.util.List;
import net.minecraft.server.MinecraftServer;

public interface ISpecialTeleportation
{
	/**The ISpecialTeleportation interface can be
	 * implemented anywhere as a means of defining
	 * a teleportation mechanism. BlockPortalBase
	 * and ItemTeleporterBase are premade implementations
	 * of this interface; their triggers are collision
	 * with a block and right-clicking on an item,
	 * respectively. Modders can employ this interface
	 * directly to outline the details of any ingame
	 * event that causes teleportation to another dimension
	 * to occur.
	 *
	 * @see DimensionAPI (for the teleportation methods that can be called by this class)
	 */
	 
	/**Returns the WorldProvider of the dimension
	 * to which the player is teleported as a result
	 * of this event.
	 */
    public abstract WorldProviderBase getDimension();

	/**Returns the instance of Teleporter that is
	 * used; the objective of the Teleporter class is
	 * to locate existing portals and build new ones,
	 * generally acting as a guide for where exactly 
	 * the player should reappear after teleporting.
	 * (Note: this method is maintained alongside the
	 * one below for the sake of backward compatibility.) 
	 */
    public abstract Teleporter getTeleporter();
	
	/**Returns the instance of Teleporter that is
	 * used by this teleportation mechanism.
	 *
	 * @param i		the ID of the dimension from which the player is traveling
	 * @param j		the ID of the dimension to which the player is traveling
	 */
	public abstract Teleporter getTeleporter(int i, int j);

	/**@deprecated
	 * Since "Entering/Leaving the Nether/End"
	 * seems to have been replaced by the server's
	 * "Downloading Terrain" message, this method
	 * is no longer applicable for now.
	 */
    public abstract String getEnteringMessage();

	/**@deprecated
	 * Since "Entering/Leaving the Nether/End"
	 * seems to have been replaced by the server's
	 * "Downloading Terrain" message, this method
	 * is no longer applicable for now.
	 */
    public abstract String getLeavingMessage();

	/**Returns whether teleportation begins
	 * immediately when the event in question
	 * occurs, as opposed to after a delay.
	 */
    public abstract boolean isPortalImmediate();
	
	/**Return true if an ingame overlay should
	 * be rendered on top of the player's view
	 * when the player is about to teleport, as is
	 * the case with the Nether portal.
	 *
	 * @see Dimension API (for easily registering a recolored version of the Nether portal texture)
	 */
	public abstract boolean displayPortalOverlay();
	
	/**Returns the texture index of the overlay.
	 */
	public abstract int getOverlayTexture();
	
	/**Returns the path of the spritesheet that
	 * the overlay overrides; intended to be used
	 * by Forge's custom texture file functionality.
	 */
	public abstract String getOverlayTextureOverride();

	/**Returns a list of the IDs of the dimensions from
	 * which the player may legally teleport.
	 */
    public abstract List canTeleportFromDimension();

	/**The ID of the dimension to which the player
	 * returns when teleporting back from the
	 * destination realm.
	 */
    public abstract int returnsPlayerToDimension();

	/**The ratio of blocks traveled in the dimension
	 * to which the player returns compared to those
	 * traveled in this teleportation mechanism's 
	 * destination dimension. For example, the overworld-
	 * Nether ratio would be 8D.
	 */
    public abstract double getDistanceRatio();

	/**The delay (in 1/20 second ticks) experienced
	 * before the player can teleport.
	 */
    public abstract int getPortalDelay();

	/**Returns an achievement triggered upon entering
	 * the destination dimension for the first time.
	 */
    public abstract Achievement triggerAchievement();
	
	/**The following four methods are triggered
	 * directly before and after the actual teleportation
	 * occurs.
	 *
	 * @param minecraftserver	the current server instance
	 * @param entityplayermp	the player being teleported
	 */
	public abstract void beforeTeleportToDimension(MinecraftServer minecraftserver, EntityPlayerMP entityplayermp);
	
	public abstract void afterTeleportToDimension(MinecraftServer minecraftserver, EntityPlayerMP entityplayermp);
	
	public abstract void beforeTeleportFromDimension(MinecraftServer minecraftserver, EntityPlayerMP entityplayermp);
	
	public abstract void afterTeleportFromDimension(MinecraftServer minecraftserver, EntityPlayerMP entityplayermp);
	
}
