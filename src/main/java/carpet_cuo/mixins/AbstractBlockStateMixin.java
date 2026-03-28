package carpet_cuo.mixins;

import carpet_cuo.wheel.BlockHardnessModifiers;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class AbstractBlockStateMixin {
    @Shadow
    public abstract Block getBlock();
    @ModifyReturnValue(
            method = "getDestroySpeed",
            at = @At("RETURN")
    )
    public float getBlockHardness(float hardness, @Local(argsOnly = true) BlockGetter world, @Local(argsOnly = true) BlockPos pos) {
        Optional<Float> optional = BlockHardnessModifiers.getHardness(this.getBlock(), world, pos);
        return optional.orElse(hardness);
    }
}