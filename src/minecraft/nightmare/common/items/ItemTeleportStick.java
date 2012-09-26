package nightmare.common.items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.DimensionAPI;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ISpecialTeleportation;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ItemTeleporterBase;
import net.minecraft.src.ModLoader;
import net.minecraft.src.Teleporter;
import net.minecraft.src.World;
import net.minecraft.src.WorldProviderBase;
import nightmare.client.GuiNightmare;

public class ItemTeleportStick extends Item{

	public ItemTeleportStick(int i) {
		super(i);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
		ModLoader.openGUI(par3EntityPlayer, new GuiNightmare());
		return par1ItemStack;
    }
	
	public String getTextureFile()
	{
		return "/Blocks/NightmareTextures.png";
	}
}
