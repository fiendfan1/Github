package nightmare.common;

import java.util.List;
import java.util.Random;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.WorldGenShrub;
import net.minecraft.src.WorldGenTrees;
import net.minecraft.src.WorldGenerator;

public class BiomeGenNightmare extends BiomeGenBase
{
    protected WorldGenerator bushGen;
	protected WorldGenerator sandGen;
    protected WorldGenerator hematiteGen;
    protected WorldGenerator meteoriteGen;
    protected WorldGenerator cobaltGen;
    protected WorldGenerator blazestoneGen;
    public static final BiomeGenBase ForbiddenForest = (new BiomeGenNightmare(24)).setBiomeName("FForest");
	   
    public BiomeGenNightmare(int par1)
    {
        super(par1);
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
       // this.spawnableCreatureList.add(new SpawnListEntry(net.minecraft.src.EntitySkeletonKnight.class, 2, 1, 2));
        //this.spawnableCreatureList.add(new SpawnListEntry(EntityAshCreeper.class, 5, 4, 4));

        //this.spawnableCreatureList.add(new SpawnListEntry(EntitySkeletonKnight.class, 5, 4, 4));

        
        // this.spawnableCreatureList.add(new SpawnListEntry(net.minecraft.src.EntityAshCreeper.class, 2, 3, 3));
       //this.bushGen = new WorldGenForbiddenBush();

       // this.sandGen = new WorldGenForbiddenSand(7, mod_ForbiddenForest.ForbiddenSand.blockID);

       // this.spawnableWaterCreatureList.clear();
  
       topBlock = (byte)mod_Nightmare.nightmareGrass.blockID;
        fillerBlock = (byte)mod_Nightmare.nightmareGrass.blockID;
       //biomeDecorator = new BiomeDecoratorForbidden(this);

        
       /* 
        this.blazestoneGen = new WorldGenForbidden2(mod_ForbiddenForest.FirestoneOre.blockID, 8);
        this.hematiteGen = new WorldGenForbidden2(mod_ForbiddenForest.HematiteOre.blockID, 8);
        this.meteoriteGen = new WorldGenForbidden2(mod_ForbiddenForest.MeteoriteOre.blockID, 7);
        this.cobaltGen = new WorldGenForbidden2(mod_ForbiddenForest.CobaltOre.blockID, 7);
        
       */ 
    }

    public WorldGenerator getRandomWorldGenForTrees(Random par1Random)
    {
        return (WorldGenerator)(par1Random.nextInt(10) == 0 ? this.worldGeneratorBigTree : (par1Random.nextInt(2) == 0 ? new WorldGenShrub(3, 0) : (par1Random.nextInt(3) == 0 ? new WorldGenNightmareTrees(false) : new WorldGenTrees(false, 4 + par1Random.nextInt(7), 3, 3, true))));
    }
    
    
    
    
}
