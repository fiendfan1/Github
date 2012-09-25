package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.MinecraftServer;

public abstract class ItemTeleporterBase extends Item
	implements ISpecialTeleportation
{

	/**See ISpecialTeleportation for method descriptions.*/

    public ItemTeleporterBase(int i)
    {
        super(i);
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
		DimensionAPI.currentPortal = null;
        if (!world.isRemote)
        {
            DimensionAPI.setInPortal(getDimension(), MinecraftServer.getServer(), (EntityPlayerMP)entityplayer, this, null);
        }
		if (displayPortalOverlay())
		{
			DimensionAPI.currentPortal = this;
			entityplayer.setInPortal();
			if(entityplayer.timeInPortal >= 0.9F)
			{
				entityplayer.timeInPortal = 0.0F;
			}
		}
        return itemstack;
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
		return true;
	}
	
	public boolean displayPortalOverlay()
	{
		return false;
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
		return 0;
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
