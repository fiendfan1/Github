package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public class TexturePackDefault extends TexturePackImplementation
{
    public TexturePackDefault()
    {
        super("default", "Default");
    }

    protected void func_77540_a()
    {
        this.field_77546_b = "The default look of Minecraft";
    }
}
