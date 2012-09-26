package nightmare.common.blocks;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import nightmare.common.NightmareBlock;
import nightmare.common.mod_Nightmare;

public class BlockNightmareLog extends NightmareBlock{
	public BlockNightmareLog(int par1,int par2)
    {
        super(par1, par2, Material.wood);
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
    public int getBlockTextureFromSide(int i)  
    {
    	if(i == 0)
    		return this.blockIndexInTexture +1;
		if(i == 1)
			return this.blockIndexInTexture+1;
		else
			return this.blockIndexInTexture;
    }
}
