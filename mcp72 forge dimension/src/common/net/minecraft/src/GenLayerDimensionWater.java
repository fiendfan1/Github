package net.minecraft.src;

public class GenLayerDimensionWater extends GenLayer
{

    public GenLayerDimensionWater(long l, GenLayer genlayer, GenLayer genlayer1, WorldProviderBase worldproviderbase)
    {
        super(l);
        field_35512_b = genlayer;
        field_35513_c = genlayer1;
		worldProvider = worldproviderbase;
    }
	
    public void initWorldGenSeed(long par1)
    {
        field_35512_b.initWorldGenSeed(par1);
        field_35513_c.initWorldGenSeed(par1);
        super.initWorldGenSeed(par1);
    }

    public int[] getInts(int par1, int par2, int par3, int par4)
    {
        int ai[] = field_35512_b.getInts(par1, par2, par3, par4);
        int ai1[] = field_35513_c.getInts(par1, par2, par3, par4);
        int ai2[] = IntCache.getIntCache(par3 * par4);

        for (int i = 0; i < par3 * par4; i++)
        {
			BiomeGenBase ocean = worldProvider.setOceanBiomes(null, this);
            if (ocean != null && ai[i] == ocean.biomeID)
            {
                ai2[i] = ai[i];
                continue;
            }

            if (ai1[i] >= 0)
            {
				BiomeGenBase river = worldProvider.setRiverBiomes(BiomeGenBase.biomeList[ai[i]], this);
				if(river != null)
				{
					ai2[i] = river.biomeID;
				}
                else
                {
                    ai2[i] = ai1[i];
                }
            }
            else
            {
                ai2[i] = ai[i];
            }
        }

        return ai2;
    }
	
	private GenLayer field_35512_b;
    private GenLayer field_35513_c;
	private WorldProviderBase worldProvider;
}
