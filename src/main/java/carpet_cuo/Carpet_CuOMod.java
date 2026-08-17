package carpet_cuo;

import carpet_cuo.command.ChunkCommand;
import carpet_cuo.command.RulesCommand;
import carpet_cuo.rule.BlockDyeing.BlockDyeing;
import carpet_cuo.rule.CustomizeTheArrowOwner.CustomizeTheArrowOwner;
import carpet_cuo.rule.EntityHighLight.EntityHighLight;
import carpet_cuo.rule.OreBreeding.OreBreeding;
import carpet_cuo.rule.RightClickBlockUpdate.RightClickBlockUpdate;
import carpet_cuo.rule.RustingCopperManually.RustingCopperManually;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;

public class Carpet_CuOMod implements ModInitializer {
	public static final String MOD_ID = "carpet_cuo_addition";
	public static String version;
	@Override
	public void onInitialize() {
		version = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(RuntimeException::new).getMetadata().getVersion().getFriendlyString();
		RustingCopperManually.init();
		Carpet_CuOServer.init();
		BlockDyeing.init();
		RightClickBlockUpdate.init();
		EntityHighLight.init();
		OreBreeding.init();
		CustomizeTheArrowOwner.init();
		CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> {
				ChunkCommand.getInstance().register(commandDispatcher);
				RulesCommand.getInstance().register(commandDispatcher);
		});
	}
}