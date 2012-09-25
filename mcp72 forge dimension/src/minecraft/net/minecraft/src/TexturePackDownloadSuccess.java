package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.File;

@SideOnly(Side.CLIENT)
class TexturePackDownloadSuccess implements IDownloadSuccess
{
    final TexturePackList field_76171_a;

    TexturePackDownloadSuccess(TexturePackList par1TexturePackList)
    {
        this.field_76171_a = par1TexturePackList;
    }

    public void onSuccess(File par1File)
    {
        if (TexturePackList.func_77301_a(this.field_76171_a))
        {
            TexturePackList.func_77303_a(this.field_76171_a, new TexturePackCustom(TexturePackList.func_77291_a(this.field_76171_a, par1File), par1File));
            TexturePackList.getMinecraft(this.field_76171_a).func_71395_y();
        }
    }
}
