package carpet_cuo.mixins.rules.TripwireScheduleTickFixMixin;

//#if MC == 260200
import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.level.block.TripWireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TripWireBlock.class)
public class TripWireBlockMixin {
    @ModifyArg(
            method = "checkPressed(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Ljava/util/List;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;scheduleTick(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;I)V",
                    ordinal = 1
            ),
            index = 2
    )
    private int checkPressed(int par3) {
        if (Carpet_CuOSettings.tripwireScheduleTickFix) return 1;
        return par3;
    }
}
//#else
//$$ import carpet_cuo.utils.compat.DummyClass;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$
//$$ @Mixin(DummyClass.class)
//$$ public class TripWireBlockMixin {
//$$ }
//#endif
