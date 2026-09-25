package carpet_cuo.mixins.rules.MaxUpdateDepthMixin;

//#if MC < 260300
import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Level.class)
public abstract class WorldMixin {
    @ModifyConstant(
            method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
            constant = @Constant(intValue = 512)
    )
    private int modifyMaxUpdateDepth(int constant){
        if (Carpet_CuOSettings.maxUpdateDepth != 512) return Carpet_CuOSettings.maxUpdateDepth;
        else return constant;
    }
}
//#else
//$$ import carpet_cuo.Carpet_CuOSettings;
//$$ import net.minecraft.world.level.LevelWriter;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.ModifyArg;
//$$
//$$ @Mixin(LevelWriter.class)
//$$ public interface WorldMixin {
//$$     @ModifyArg(
//$$             method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
//$$             at = @At(
//$$                     value = "INVOKE",
//$$                     target = "Lnet/minecraft/world/level/LevelWriter;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z"
//$$             ),
//$$             index = 3
//$$     )
//$$     private int modifyMaxUpdateDepth(int i){
//$$         if (Carpet_CuOSettings.maxUpdateDepth != 512) return Carpet_CuOSettings.maxUpdateDepth;
//$$         return i;
//$$     }
//$$ }
//#endif
