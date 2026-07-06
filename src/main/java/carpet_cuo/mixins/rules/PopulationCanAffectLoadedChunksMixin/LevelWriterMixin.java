package carpet_cuo.mixins.rules.PopulationCanAffectLoadedChunksMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelWriter.class)
public interface LevelWriterMixin extends LevelWriter {
    @Inject(
            method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
            at = @At("RETURN"),
            cancellable = true
    )
    private void setBlock(BlockPos blockPos, BlockState blockState, int i, CallbackInfoReturnable<Boolean> cir) {
        if (Carpet_CuOSettings.populationCanAffectLoadedChunks) {
            WorldGenLevel worldGenLevel = (WorldGenLevel) this;
            try (BulkSectionAccess bulkSectionAccess = new BulkSectionAccess(worldGenLevel)) {
                LevelChunkSection levelChunkSection = bulkSectionAccess.getSection(blockPos);
                if (levelChunkSection != null) {
                    int x = SectionPos.sectionRelative(blockPos.getX());
                    int y = SectionPos.sectionRelative(blockPos.getY());
                    int z = SectionPos.sectionRelative(blockPos.getZ());
                    levelChunkSection.setBlockState(x, y, z, blockState, false);
                    cir.cancel();
                }
            }
        }
    }
}
