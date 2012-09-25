package nightmare.common;
import java.util.Random;

import net.minecraft.src.*;

public class BlockNightmareGrass extends Block{

	public BlockNightmareGrass(int par1, int par2) 
	{
		super(par1, par2, Material.grass);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}
	
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (par1World.isRemote)
        {
            return;
        }

        if (par1World.getBlockLightValue(par2, par3 + 1, par4) < 4 && Block.lightOpacity[par1World.getBlockId(par2, par3 + 1, par4)] > 2)
        {
            par1World.setBlockWithNotify(par2, par3, par4, mod_Nightmare.nightmareGrass.blockID);
        }
        else if (par1World.getBlockLightValue(par2, par3 + 1, par4) >= 9)
        {
            for (int i = 0; i < 45; i++)
            {
                int j = (par2 + par5Random.nextInt(3)) - 1;
                int k = (par3 + par5Random.nextInt(5)) - 3;
                int l = (par4 + par5Random.nextInt(3)) - 1;
                int i1 = par1World.getBlockId(j, k + 1, l);

                if (par1World.getBlockId(j, k, l) == Block.grass.blockID && par1World.getBlockLightValue(j, k + 1, l) >= 4 && Block.lightOpacity[i1] <= 2)
                {
                    par1World.setBlockWithNotify(j, k, l, mod_Nightmare.nightmareGrass.blockID);
                }
            }
        }
    }
   
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        super.randomDisplayTick(par1World, par2, par3, par4, par5Random);

        if (par5Random.nextInt(75) == 0)
        {
            par1World.spawnParticle("depthsuspend", (float)par2 + par5Random.nextFloat(), (float)par3 + 1.1F, (float)par4 + par5Random.nextFloat(), 20.0D, 20.0D, 20.0D);
        }
    }

    public int getBlockTextureFromSideAndMetadata (int i, int j)
    {
    	
    	if (i == 0)
    		
    		return mod_Nightmare.grassSide;
    	if (i == 1)
    		
    		return mod_Nightmare.grassTop;
    	
    	if (i == 2)
    		
    		return mod_Nightmare.grassSide;
    	if (i == 3)
    		
    		return mod_Nightmare.grassSide;
    	if (i == 4)
	
    		return mod_Nightmare.grassSide;
    	if (i == 5)
	
    		return mod_Nightmare.grassSide;
   
    	if (j ==1)
    	{
    		return 166;
    	}
    
    		return j != 2 ? 20 : 177;
    }

}
