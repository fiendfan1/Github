package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.concurrent.Callable;
import net.minecraft.client.Minecraft;

@SideOnly(Side.CLIENT)
public class CallableClientProfiler implements Callable
{
    /** Gets skin of Minecraft player. */
    final Minecraft minecraftClientProfiler;

    public CallableClientProfiler(Minecraft par1Minecraft)
    {
        this.minecraftClientProfiler = par1Minecraft;
    }

    public String func_74499_a()
    {
        return this.minecraftClientProfiler.mcProfiler.profilingEnabled ? this.minecraftClientProfiler.mcProfiler.func_76322_c() : "N/A (disabled)";
    }

    public Object call()
    {
        return this.func_74499_a();
    }
}
