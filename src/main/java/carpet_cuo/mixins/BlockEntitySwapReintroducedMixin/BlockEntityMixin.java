package carpet_cuo.mixins.BlockEntitySwapReintroducedMixin;
//#if MC < 12101
//$$import carpet_cuo.utils.compat.DummyClass;
//$$import org.spongepowered.asm.mixin.Mixin;
//$$
//$$@Mixin(DummyClass.class)
//$$public abstract class BlockEntityMixin {}
//#elseif MC >= 12101
import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin {
    @Inject(
            method = "validateBlockState",
            at = @At("HEAD"),
            cancellable = true
    )
    private void skipValidateSupports(CallbackInfo ci){
        if (Carpet_CuOSettings.blockEntitySwapReintroduced)ci.cancel();
    }
}
//#endif