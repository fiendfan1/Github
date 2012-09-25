package net.minecraft.src;

public class GenLayerDimensionMinorBiome extends GenLayer
{
    public GenLayerDimensionMinorBiome(long l, GenLayer genlayer, WorldProviderBase worldproviderbase)
    {
        super(l);
        parent = genlayer;
		worldProvider = worldproviderbase;
    }

    public int[] getInts(int par1, int par2, int par3, int par4)
    {
        int ai[] = parent.getInts(par1 - 1, par2 - 1, par3 + 2, par4 + 2);
        int ai1[] = IntCache.getIntCache(par3 * par4);

        for (int i = 0; i < par4; i++)
        {
            for (int j = 0; j < par3; j++)
            {
                initChunkSeed(j + par1, i + par2);
                int k = ai[j + 1 + (i + 1) * (par3 + 2)];

                if (nextInt(3) == 0)
                {
                    int l = k;
					
					BiomeGenBase biome = worldProvider.setMinorBiomes(BiomeGenBase.biomeList[k], this);
					if(biome != null)
					{
						l = biome.biomeID;
					}
					
                    if (l != k)
                    {
                        int i1 = ai[j + 1 + ((i + 1) - 1) * (par3 + 2)];
                        int j1 = ai[j + 1 + 1 + (i + 1) * (par3 + 2)];
                        int k1 = ai[((j + 1) - 1) + (i + 1) * (par3 + 2)];
                        int l1 = ai[j + 1 + (i + 1 + 1) * (par3 + 2)];

                        if (i1 == k && j1 == k && k1 == k && l1 == k)
                        {
                            ai1[j + i * par3] = l;
                        }
                        else
                        {
                            ai1[j + i * par3] = k;
                        }
                    }
                    else
                    {
                        ai1[j + i * par3] = k;
                    }
                }
                else
                {
                    ai1[j + i * par3] = k;
                }
            }
        }

        return ai1;
    }
	
	private WorldProviderBase worldProvider;
}
