package nightmare.common;
import net.minecraft.src.ItemTeleporterBase;
import net.minecraft.src.Teleporter;
import net.minecraft.src.WorldProviderBase;

public class ItemNightmare extends ItemTeleporterBase{

	public ItemNightmare(int i) {
		super(i);
	}

	@Override
	public WorldProviderBase getDimension() {
		WorldProviderBase wp = new WorldProviderNightmare();
		return wp;
	}

	@Override
	public Teleporter getTeleporter() {
		Teleporter tp = new NightmareTeleporter();
		return tp;
	}

}
