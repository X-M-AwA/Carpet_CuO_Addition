package carpet_cuo.mixins.carpet;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.SettingsManager;
import carpet_cuo.Carpet_CuOSettings;
import carpet_cuo.mixins.rules.MaxChainDepthMixin.ChainRestrictedNeighborUpdaterAccessor;
import carpet_cuo.mixins.rules.MaxChainDepthMixin.LevelAccessor;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SettingsManager.class)
public abstract class SettingsManagerMixin {
    @Inject(
            method = "setRule",
            at = @At("RETURN")
    )
    private void setValue(CommandSourceStack source, CarpetRule<?> rule, String newValue, CallbackInfoReturnable<Integer> cir) {
        if (rule.name().equals("maxChainDepth")) {
            ServerLevel serverLevel = source.getLevel();
            ((ChainRestrictedNeighborUpdaterAccessor) ((LevelAccessor) serverLevel).carpet_cuo$getNeighborUpdater())
                    .carpet_cuo$setMaxChainedNeighborUpdates(Carpet_CuOSettings.maxChainDepth);
        }
    }
}
