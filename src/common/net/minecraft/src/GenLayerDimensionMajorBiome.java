package net.minecraft.src;

public class GenLayerDimensionMajorBiome extends GenLayer
{
    public GenLayerDimensionMajorBiome(long l, GenLayer genlayer, WorldProviderBase worldproviderbase)
    {
        super(l);
        parent = genlayer;
		worldProvider = worldproviderbase;
        allowedBiomes = worldproviderbase.setMajorBiomes();
    }

    public int[] getInts(int par1, int par2, int par3, int par4)
    {
        int ai[] = parent.getInts(par1, par2, par3, par4);
        int ai1[] = IntCache.getIntCache(par3 * par4);

        for (int i = 0; i < par4; i++)
        {
            for (int j = 0; j < par3; j++)
            {
                initChunkSeed(j + par1, i + par2);
                int k = ai[j + i * par3];

				BiomeGenBase ocean = worldProvider.setOceanBiomes(null, this);
                if (ocean != null && k == ocean.biomeID)
                {
                    ai1[j + i * par3] = ocean.biomeID;
                } else
                {
                    ai1[j + i * par3] = allowedBiomes[nextInt(allowedBiomes.length)].biomeID;
                }
            }
        }

        return ai1;
    }
	
	private WorldProviderBase worldProvider;
	private BiomeGenBase allowedBiomes[];
}
