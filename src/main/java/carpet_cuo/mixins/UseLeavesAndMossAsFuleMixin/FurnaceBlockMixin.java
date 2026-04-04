package carpet_cuo.mixins.UseLeavesAndMossAsFuleMixin;

import carpet_cuo.rule.UseLeavesAndMossAsFuel;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FurnaceBlock.class)
public abstract class FurnaceBlockMixin {
    @Inject(
            method = "openContainer",
            at = @At("HEAD")
    )
    private void setFuel(Level world, BlockPos blockPos, Player player, CallbackInfo ci){
        UseLeavesAndMossAsFuel.registerFuels(world);
    }
}
