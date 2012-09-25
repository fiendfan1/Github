package nightmare.common;

import java.util.Random;

import net.minecraft.src.*;

public class BlockNightmareLog extends Block{
	protected BlockNightmareLog(int par1)
    {
        super(par1, Material.wood);
        blockIndexInTexture = 20;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random par1Random)
    {
        return 1;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return mod_Nightmare.nightmareLog.blockID;
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6)
    {
        super.harvestBlock(par1World, par2EntityPlayer, par3, par4, par5, par6);
    }

    /**
     * Called whenever the block is removed.
     */
    public void onBlockRemoval(World par1World, int par2, int par3, int par4)
    {
        byte byte0 = 4;
        int i = byte0 + 1;

        if (par1World.checkChunksExist(par2 - i, par3 - i, par4 - i, par2 + i, par3 + i, par4 + i))
        {
            for (int j = -byte0; j <= byte0; j++)
            {
                for (int k = -byte0; k <= byte0; k++)
                {
                    for (int l = -byte0; l <= byte0; l++)
                    {
                        int i1 = par1World.getBlockId(par2 + j, par3 + k, par4 + l);

                        if (i1 != Block.leaves.blockID)
                        {
                            continue;
                        }

                        int j1 = par1World.getBlockMetadata(par2 + j, par3 + k, par4 + l);

                        if ((j1 & 8) == 0)
                        {
                            par1World.setBlockMetadata(par2 + j, par3 + k, par4 + l, j1 | 8);
                        }
                    }
                }
            }
        }
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int i, int j)  
    {
    	if(i == 0)
			return mod_Nightmare.logTop; //This is the bottom of the log
		if(i == 1)
			return mod_Nightmare.logTop; //This is the top
		if(i == 2)
			return mod_Nightmare.logSide; //These are the sides
		if(i == 3)
			return mod_Nightmare.logSide;
		if(i == 4)
			return mod_Nightmare.logSide;
		if(i == 5)
			return mod_Nightmare.logSide;
		
        if(j == 1)
        {
            return 116;
        }
        return j != 2 ? 20 : 117;
    }
    
    protected int damageDropped(int par1)
    {
        return par1;
    }
}
