package nightmare.common;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

public class BlockNightmareStone extends Block{

	public BlockNightmareStone(int par1, int par2) 
	{
		super(par1, par2, Material.rock);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}
	
}