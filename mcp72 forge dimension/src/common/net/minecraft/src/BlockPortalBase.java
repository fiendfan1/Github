package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.MinecraftServer;

public abstract class BlockPortalBase extends Block
	implements ISpecialTeleportation
{

	/**See ISpecialTeleportation for method descriptions.*/

    public BlockPortalBase(int i, int j, Material material)
    {
        super(i, j, material);
    }

    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
        if (entity instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)entity;
            if (!world.isRemote)
            {
                DimensionAPI.setInPortal(getDimension(), MinecraftServer.getServer(), entity, this, this);
            }
			if (displayPortalOverlay() && DimensionAPI.isInPortal(entityplayer, blockID))
			{
				DimensionAPI.currentPortal = this;
				entityplayer.setInPortal();
				if(entityplayer.timeInPortal >= 0.9F)
				{
					entityplayer.timeInPortal = 0.0F;
				}
			}
        }
    }

    public abstract WorldProviderBase getDimension();

    public abstract Teleporter getTeleporter();

    public String getEnteringMessage()
	{
		return "";
	}

    public String getLeavingMessage()
	{
		return "";
	}
	
	public Teleporter getTeleporter(int i, int j)
	{
		return getTeleporter();
	}

    public boolean isPortalImmediate()
    {
        return false;
    }
	
	public boolean displayPortalOverlay()
	{
		return true;
	}

	public int getOverlayTexture()
	{
		return Block.portal.blockIndexInTexture;
	}
	
	public String getOverlayTextureOverride()
	{
		return "/terrain.png";
	}

    public List canTeleportFromDimension()
    {
        ArrayList arraylist = new ArrayList();
        arraylist.add(Integer.valueOf(0));
        return arraylist;
    }

    public int returnsPlayerToDimension()
    {
        return 0;
    }

    public double getDistanceRatio()
    {
        return 1.0D;
    }

    public int getPortalDelay()
    {
        return 120;
    }

    public Achievement triggerAchievement()
    {
        return null;
    }
	
	public void beforeTeleportToDimension(MinecraftServer minecraftserver, EntityPlayerMP entityplayermp)
	{
	}
	
	public void afterTeleportToDimension(MinecraftServer minecraftserver, EntityPlayerMP entityplayermp)
	{
	}
	
	public void beforeTeleportFromDimension(MinecraftServer minecraftserver, EntityPlayerMP entityplayermp)
	{
	}
	
	public void afterTeleportFromDimension(MinecraftServer minecraftserver, EntityPlayerMP entityplayermp)
	{
	}
}
