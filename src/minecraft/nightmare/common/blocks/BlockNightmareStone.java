package nightmare.common.blocks;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;
import nightmare.common.NightmareBlock;

public class BlockNightmareStone extends NightmareBlock {

	public BlockNightmareStone(int par1, int par2) 
	{
		super(par1, par2, Material.rock);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}
}