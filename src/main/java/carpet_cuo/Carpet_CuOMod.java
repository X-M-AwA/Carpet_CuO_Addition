package carpet_cuo;

import carpet_cuo.command.ChunkCommand;
import carpet_cuo.rule.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;

public class Carpet_CuOMod implements ModInitializer {
	public static final String MOD_ID = "carpet_cuo_addition";
	public static String version;
	@Override
	public void onInitialize() {
		version = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(RuntimeException::new).getMetadata().getVersion().getFriendlyString();
		Carpet_CuOServer.init();
		BlockDyeing.init();
		Update.init();
		EntityHighLight.init();
		OreBreeding.init();
		CustomizeTheArrowOwner.init();
		CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> ChunkCommand.getInstance().register(commandDispatcher));
	}
}