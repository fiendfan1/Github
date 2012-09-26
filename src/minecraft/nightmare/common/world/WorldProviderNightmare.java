package nightmare.common.world;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.WorldChunkManager;
import net.minecraft.src.WorldChunkManagerCustom;
import net.minecraft.src.WorldChunkManagerHell;
import net.minecraft.src.WorldProviderBase;

public class WorldProviderNightmare extends WorldProviderBase
{
        public WorldProviderNightmare()
        {
        }

        //required:
        public int getDimensionID()
        {
                return 20;
        }

        //not required:
public boolean renderClouds()
        {
                return true;
        }

        public boolean renderVoidFog()
        {
                return true;
        }

        public float setSunSize()
        {
                return 1.0F;
        }

        public float setMoonSize()
        {
                return 2.1F;
        }

        public String getSunTexture()
        {
                return null;
        }

        public String getMoonTexture()
        {
                return "/example/moonGreen.png";
        }

        public boolean renderStars()
        {
                return true;
        }

        public boolean darkenSkyDuringRain()
        {
                return true;
        }

        public String getRespawnMessage()
        {
                return "Apparently this dimension is too tough for you ;)";
        }

        //required by MC:
        public void registerWorldChunkManager()
        {
                worldChunkMgr = new WorldChunkManagerHell(BiomeGenNightmare.ForbiddenForest, 1.0F, 1.0F);
        }
        
        public IChunkProvider getChunkProvider()
        {
                return new WorldChunkManagerNightmare(worldObj, worldObj.getSeed(), false);
        }

        public boolean canRespawnHere()
        {

        	return false;

        }

        public float calculateCelestialAngle(long par1, float par3)
        {

        	return 0.5F;

        }
        
		@Override
		public String func_80007_l() {
			// TODO Auto-generated method stub
			return null;
		}
}