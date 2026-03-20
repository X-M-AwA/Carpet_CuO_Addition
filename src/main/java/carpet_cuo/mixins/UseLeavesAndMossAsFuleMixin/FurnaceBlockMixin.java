package carpet_cuo.mixins.UseLeavesAndMossAsFuleMixin;

import carpet_cuo.rule.UseLeavesAndMossAsFuel;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FurnaceBlock.class)
public abstract class FurnaceBlockMixin {
    @Inject(
            method = "openScreen",
            at = @At(value = "HEAD")
    )
    private void setFuel(World world, BlockPos pos, PlayerEntity player, CallbackInfo ci){
        UseLeavesAndMossAsFuel.registerFuels();
    }
}
