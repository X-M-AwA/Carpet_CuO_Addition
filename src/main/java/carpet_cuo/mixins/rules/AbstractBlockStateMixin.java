package carpet_cuo.mixins.rules;

import carpet_cuo.wheel.BlockHardnessModifiers;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class AbstractBlockStateMixin {
    @Shadow
    public abstract Block getBlock();

    @ModifyReturnValue(
            method = "getDestroySpeed",
            at = @At("RETURN")
    )
    public float getBlockHardness(float hardness) {
        return BlockHardnessModifiers.getHardness(this.getBlock(), hardness);
    }
}