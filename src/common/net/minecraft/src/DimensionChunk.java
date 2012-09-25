package net.minecraft.src;

import java.lang.reflect.Field;
import java.util.Arrays;

public class DimensionChunk extends Chunk
{
    
	public DimensionChunk(World par1World, byte[] par2ArrayOfByte, int par3, int par4, boolean tallTerrain)
	{
		super(par1World, par3, par4);
        int var5 = par2ArrayOfByte.length / 256;

        for (int var6 = 0; var6 < 16; ++var6)
        {
            for (int var7 = 0; var7 < 16; ++var7)
            {
                for (int var8 = 0; var8 < var5; ++var8)
                {
                    int var9 = par2ArrayOfByte[var6 << 11 | var7 << 7 | var8] & 0xff;
					
					if(tallTerrain)
					{
						var9 = par2ArrayOfByte[var6 << 12 | var7 << 8 | var8] & 0xff;
					}

                    if (var9 != 0)
                    {
                        int var10 = var8 >> 4;

						try
						{
							Class c = net.minecraft.src.Chunk.class;
							Field f = c.getDeclaredField("storageArrays");
							f.setAccessible(true);
							ExtendedBlockStorage[] storage = net.minecraft.src.ExtendedBlockStorage[].class.cast(f.get(this));
							if(storage[var10] == null)
							{
								storage[var10] = new ExtendedBlockStorage(var10 << 4);
								f.set(this, storage);
							}
							ExtendedBlockStorage storage1 = storage[var10];
							storage1.setExtBlockID(var6, var8 & 15, var7, var9);
						}
						catch(NoSuchFieldException e)
						{
						}
						catch(IllegalAccessException e1)
						{
						}
					}
                }
            }
        }
	}
	
}
