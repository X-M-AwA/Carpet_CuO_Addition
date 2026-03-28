package carpet_cuo.mixins.BlockEntitySwapReintroducedMixin;
//#if MC < 12101
//$$import carpet_cuo.utils.compat.DummyClass;
//$$import org.spongepowered.asm.mixin.Mixin;
//$$
//$$@Mixin(DummyClass.class)
//$$public abstract class WorldChunkMixin {}
//#elseif MC >= 12101
import carpet_cuo.Carpet_CuOSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelChunk.class)
public abstract class WorldChunkMixin {
    @ModifyExpressionValue(
            method = "setBlockState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/entity/BlockEntity;isValidBlockState(Lnet/minecraft/world/level/block/state/BlockState;)Z"
            )
    )
    private boolean conditionalSupportsCheck(boolean original) {
        if (Carpet_CuOSettings.blockEntitySwapReintroduced) return true;
        else return original;
    }
}
//#endif