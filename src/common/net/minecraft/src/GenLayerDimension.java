package net.minecraft.src;

public abstract class GenLayerDimension extends GenLayer
{

	public GenLayerDimension(long l)
	{
		super(l);
	}

    public static GenLayer[] getGenLayers(long l, WorldProviderBase worldproviderbase)
    {
        GenLayer genlayer = new GenLayerIsland(1L);
        genlayer = new GenLayerFuzzyZoom(2000L, genlayer);
        genlayer = new GenLayerDimensionOcean1(1L, genlayer, worldproviderbase);
        genlayer = new GenLayerZoom(2001L, genlayer);
        genlayer = new GenLayerDimensionOcean1(2L, genlayer, worldproviderbase);
        genlayer = new GenLayerZoom(2002L, genlayer);
        genlayer = new GenLayerDimensionOcean1(3L, genlayer, worldproviderbase);
        genlayer = new GenLayerZoom(2003L, genlayer);
        genlayer = new GenLayerDimensionOcean1(4L, genlayer, worldproviderbase);
        byte byte0 = 4;
        GenLayer genlayer1 = genlayer;
        genlayer1 = GenLayerZoom.func_75915_a(1000L, genlayer1, 0);
		genlayer1 = new GenLayerDimensionOcean2(100L, genlayer1, worldproviderbase);
        genlayer1 = GenLayerZoom.func_75915_a(1000L, genlayer1, byte0 + 2);
		genlayer1 = new GenLayerDimensionRiver(1L, genlayer1, worldproviderbase);
        genlayer1 = new GenLayerSmooth(1000L, genlayer1);
        GenLayer genlayer2 = genlayer;
        genlayer2 = GenLayerZoom.func_75915_a(1000L, genlayer2, 0);
        genlayer2 = new GenLayerDimensionMajorBiome(200L, genlayer2, worldproviderbase);
        genlayer2 = GenLayerZoom.func_75915_a(1000L, genlayer2, 2);
		genlayer2 = new GenLayerDimensionMinorBiome(1000L, genlayer2, worldproviderbase);
		genlayer2 = new GenLayerDimensionMiscBiome(5L, genlayer2, worldproviderbase);
        for (int i = 0; i < byte0; i++)
        {
            genlayer2 = new GenLayerZoom(1000 + i, genlayer2);
            if (i == 0)
            {
                genlayer2 = new GenLayerDimensionOcean1(3L, genlayer2, worldproviderbase);
            }
            if (i == 1)
            {
                genlayer2 = new GenLayerDimensionBorderBiome(1000L, genlayer2, worldproviderbase);
            }
        }
        genlayer2 = new GenLayerSmooth(1000L, genlayer2);
        genlayer2 = new GenLayerDimensionWater(100L, genlayer2, genlayer1, worldproviderbase);
        GenLayerDimensionWater genlayerdimensionwater = ((GenLayerDimensionWater)(genlayer2));
        GenLayerVoronoiZoom genlayervoronoizoom = new GenLayerVoronoiZoom(10L, genlayer2);
        genlayer2.initWorldGenSeed(l);
        genlayervoronoizoom.initWorldGenSeed(l);
		GenLayer genlayer3 = (GenLayer)genlayer2;
        return (new GenLayer[] {genlayer3, genlayervoronoizoom, genlayerdimensionwater});
    }
	
}
