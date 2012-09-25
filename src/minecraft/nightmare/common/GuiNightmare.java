package nightmare.common;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.ModLoader;

public class GuiNightmare extends GuiScreen
{
	public GuiNightmare()
	{
	}
	
	@Override
	public void initGui()
	{
		controlList.clear();
		controlList.add(new GuiButton(1, width / 2 + 2, height/ 2 + 20, 98, 20, "TestButton"));
		controlList.add(new GuiButton(2, width / 2 - 100, height/ 2 + 20, 98, 20, "TestButton 2"));
	}
	
	@Override
	public void actionPerformed(GuiButton guibutton)
	{
		if(guibutton.id == 1)
		{
			ModLoader.getMinecraftInstance();
		}
		
		if(guibutton.id == 2)
		{
			ModLoader.getMinecraftInstance().theWorld.setRainStrength(0F);
		}
	}
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
	@Override
	public void drawScreen(int i, int j, float f)
	{
		drawDefaultBackground();
		drawRect(20, 20, width - 20, height - 20, 0x60bb0000);
		drawCenteredString(fontRenderer, "Test", width / 2, 45, 0xffffff);
		super.drawScreen(i, j, f);
	}
}
