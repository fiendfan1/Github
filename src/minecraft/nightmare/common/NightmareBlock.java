package nightmare.common;

import net.minecraft.src.Block;
import net.minecraft.src.Material;

public class NightmareBlock extends Block {
	public NightmareBlock(int par1,int par2, Material blockMaterial)
	{
	super(par1, par2, blockMaterial);
	}
	
	public String getTextureFile()
	{
		return "/Blocks/NightmareTextures.png";
	}
}
