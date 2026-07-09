package carpet_cuo.mixins.rules.HangingEntityDropInstantlyMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.core.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
//#if MC >= 260200
//$$ import net.minecraft.world.entity.EntityTypes;
//#endif
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.NeighborUpdater;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Predicate;

@Mixin(Level.class)
public abstract class LevelMixin {

    @Shadow
    @Nullable
    public abstract MinecraftServer getServer();

    @Shadow
    public abstract List<Entity> getEntities(@Nullable Entity entity, AABB aABB, Predicate<? super Entity> predicate);

    @Inject(
            method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
            )
    )
    private void setBlock(BlockPos blockPos, BlockState blockState, int i, int j, CallbackInfoReturnable<Boolean> cir) {
        if (Carpet_CuOSettings.hangingEntitiesDropInstantly) {
            Level level = (Level) (Object) this;
            if (level.isClientSide()) return;

            for (Direction direction : NeighborUpdater.UPDATE_ORDER) {
                BlockPos pos = blockPos.mutable().move(direction);
                AABB aabb = new AABB(pos);
                //#if MC <= 260102
                List<Entity> entities = this.getEntities(null, aabb, entity -> (entity instanceof HangingEntity));
                //#else
                //$$ List<Entity> entities = this.getEntities(null, aabb, entity -> (entity instanceof HangingEntity));
                //#endif
                for (Entity entity : entities) {
                    HangingEntity hangingEntity = (HangingEntity) entity;
                    if (!hangingEntity.survives()) {
                        //#if MC >= 12103
                        hangingEntity.kill((ServerLevel) level);
                        hangingEntity.dropItem((ServerLevel) level, entity);
                        //#else
                        //$$ hangingEntity.kill();
                        //$$ hangingEntity.dropItem(entity);
                        //#endif
                    }
                }
            }
        }
    }
}
