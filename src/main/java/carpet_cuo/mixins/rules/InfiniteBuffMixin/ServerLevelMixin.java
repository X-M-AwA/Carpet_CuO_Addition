package carpet_cuo.mixins.rules.InfiniteBuffMixin;

import carpet_cuo.Carpet_CuOSettings;
import carpet_cuo.rule.InfiniteBuff.InfiniteBuff;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
    @Shadow
    public abstract List<ServerPlayer> players();

    @Shadow
    public abstract ServerLevel getLevel();

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/entity/PersistentEntitySectionManager;tick()V"
            )
    )
    private void tick(BooleanSupplier booleanSupplier, CallbackInfo ci) {
        if (Carpet_CuOSettings.infiniteBuff && this.getLevel().getLevelData().getGameTime() % 20L == 0L) {
            for (ServerPlayer player : this.players()) {
                new InfiniteBuff().tick(player);
            }
        }
    }
}
