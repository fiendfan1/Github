package net.minecraft.src;

import java.util.*;

public class WorldChunkManagerCustom extends WorldChunkManager
{
    private GenLayer genBiomes;
    private GenLayer biomeIndexLayer;
    private BiomeCache biomeCache;
    private List biomesToSpawnIn;

    protected WorldChunkManagerCustom(WorldProviderBase worldproviderbase)
    {
        biomeCache = new BiomeCache(this);
        biomesToSpawnIn = new ArrayList();
		if(worldproviderbase.getBiomesToSpawnIn() != null)
		{
			biomesToSpawnIn = worldproviderbase.getBiomesToSpawnIn();
		}
    }

    public WorldChunkManagerCustom(long par1, WorldType par3WorldType, WorldProviderBase worldproviderbase)
    {
        this(worldproviderbase);
        GenLayer agenlayer[] = GenLayerDimension.getGenLayers(par1, worldproviderbase);
        genBiomes = agenlayer[0];
        biomeIndexLayer = agenlayer[1];
    }

    public WorldChunkManagerCustom(World par1World, WorldProviderBase worldproviderbase)
    {
        this(par1World.getSeed(), par1World.getWorldInfo().getTerrainType(), worldproviderbase);
    }

    public List getBiomesToSpawnIn()
    {
        return biomesToSpawnIn;
    }

    public BiomeGenBase getBiomeGenAt(int par1, int par2)
    {
        return biomeCache.getBiomeGenAt(par1, par2);
    }

    public float[] getRainfall(float par1ArrayOfFloat[], int par2, int par3, int par4, int par5)
    {
        IntCache.resetIntCache();

        if (par1ArrayOfFloat == null || par1ArrayOfFloat.length < par4 * par5)
        {
            par1ArrayOfFloat = new float[par4 * par5];
        }

        int ai[] = biomeIndexLayer.getInts(par2, par3, par4, par5);

        for (int i = 0; i < par4 * par5; i++)
        {
            float f = (float)BiomeGenBase.biomeList[ai[i]].getIntRainfall() / 65536F;

            if (f > 1.0F)
            {
                f = 1.0F;
            }

            par1ArrayOfFloat[i] = f;
        }

        return par1ArrayOfFloat;
    }

    public float getTemperatureAtHeight(float par1, int par2)
    {
        return par1;
    }

    public float[] getTemperatures(float par1ArrayOfFloat[], int par2, int par3, int par4, int par5)
    {
        IntCache.resetIntCache();

        if (par1ArrayOfFloat == null || par1ArrayOfFloat.length < par4 * par5)
        {
            par1ArrayOfFloat = new float[par4 * par5];
        }

        int ai[] = biomeIndexLayer.getInts(par2, par3, par4, par5);

        for (int i = 0; i < par4 * par5; i++)
        {
            float f = (float)BiomeGenBase.biomeList[ai[i]].getIntTemperature() / 65536F;

            if (f > 1.0F)
            {
                f = 1.0F;
            }

            par1ArrayOfFloat[i] = f;
        }

        return par1ArrayOfFloat;
    }

    public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase par1ArrayOfBiomeGenBase[], int par2, int par3, int par4, int par5)
    {
        IntCache.resetIntCache();

        if (par1ArrayOfBiomeGenBase == null || par1ArrayOfBiomeGenBase.length < par4 * par5)
        {
            par1ArrayOfBiomeGenBase = new BiomeGenBase[par4 * par5];
        }

        int ai[] = genBiomes.getInts(par2, par3, par4, par5);

        for (int i = 0; i < par4 * par5; i++)
        {
            par1ArrayOfBiomeGenBase[i] = BiomeGenBase.biomeList[ai[i]];
        }

        return par1ArrayOfBiomeGenBase;
    }

    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase par1ArrayOfBiomeGenBase[], int par2, int par3, int par4, int par5)
    {
        return getBiomeGenAt(par1ArrayOfBiomeGenBase, par2, par3, par4, par5, true);
    }

    public BiomeGenBase[] getBiomeGenAt(BiomeGenBase par1ArrayOfBiomeGenBase[], int par2, int par3, int par4, int par5, boolean par6)
    {
        IntCache.resetIntCache();

        if (par1ArrayOfBiomeGenBase == null || par1ArrayOfBiomeGenBase.length < par4 * par5)
        {
            par1ArrayOfBiomeGenBase = new BiomeGenBase[par4 * par5];
        }

        if (par6 && par4 == 16 && par5 == 16 && (par2 & 0xf) == 0 && (par3 & 0xf) == 0)
        {
            BiomeGenBase abiomegenbase[] = biomeCache.getCachedBiomes(par2, par3);
            System.arraycopy(abiomegenbase, 0, par1ArrayOfBiomeGenBase, 0, par4 * par5);
            return par1ArrayOfBiomeGenBase;
        }

        int ai[] = biomeIndexLayer.getInts(par2, par3, par4, par5);

        for (int i = 0; i < par4 * par5; i++)
        {
            par1ArrayOfBiomeGenBase[i] = BiomeGenBase.biomeList[ai[i]];
        }

        return par1ArrayOfBiomeGenBase;
    }
	
    public boolean areBiomesViable(int par1, int par2, int par3, List par4List)
    {
        int i = par1 - par3 >> 2;
        int j = par2 - par3 >> 2;
        int k = par1 + par3 >> 2;
        int l = par2 + par3 >> 2;
        int i1 = (k - i) + 1;
        int j1 = (l - j) + 1;
        int ai[] = genBiomes.getInts(i, j, i1, j1);

        for (int k1 = 0; k1 < i1 * j1; k1++)
        {
            BiomeGenBase biomegenbase = BiomeGenBase.biomeList[ai[k1]];

            if (!par4List.contains(biomegenbase))
            {
                return false;
            }
        }

        return true;
    }

    public ChunkPosition findBiomePosition(int par1, int par2, int par3, List par4List, Random par5Random)
    {
        int i = par1 - par3 >> 2;
        int j = par2 - par3 >> 2;
        int k = par1 + par3 >> 2;
        int l = par2 + par3 >> 2;
        int i1 = (k - i) + 1;
        int j1 = (l - j) + 1;
        int ai[] = genBiomes.getInts(i, j, i1, j1);
        ChunkPosition chunkposition = null;
        int k1 = 0;

        for (int l1 = 0; l1 < ai.length; l1++)
        {
            int i2 = i + l1 % i1 << 2;
            int j2 = j + l1 / i1 << 2;
            BiomeGenBase biomegenbase = BiomeGenBase.biomeList[ai[l1]];

            if (par4List.contains(biomegenbase) && (chunkposition == null || par5Random.nextInt(k1 + 1) == 0))
            {
                chunkposition = new ChunkPosition(i2, 0, j2);
                k1++;
            }
        }

        return chunkposition;
    }

    public void cleanupCache()
    {
        biomeCache.cleanupCache();
    }
}
