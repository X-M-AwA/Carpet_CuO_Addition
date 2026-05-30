package carpet_cuo.utils;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
//#if MC >= 12106
//$$ import carpet_cuo.Carpet_CuOServer;
//$$ import net.minecraft.util.ProblemReporter;
//$$ import net.minecraft.world.level.storage.TagValueInput;
//$$ import net.minecraft.world.level.storage.TagValueOutput;
//$$ import net.minecraft.world.level.storage.ValueInput;
//#endif
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NbtManager {
    public static void writeToEntity(Entity entity, CompoundTag data) {
        //#if MC < 12106
        entity.load(data);
        //#else
        //$$ ProblemReporter.ScopedCollector logging = new ProblemReporter.ScopedCollector(entity.problemPath(), Carpet_CuOServer.LOGGER);
        //$$ ValueInput nbtReadView = TagValueInput.create(logging, entity.registryAccess(), data);
        //$$ entity.load(nbtReadView);
        //#endif
    }

    public static CompoundTag readFromEntity(Entity entity, CompoundTag nbtCompound) {
        //#if MC < 12106
        entity.saveWithoutId(nbtCompound);
        //#else
        //$$ ProblemReporter.ScopedCollector logging = new ProblemReporter.ScopedCollector(entity.problemPath(), Carpet_CuOServer.LOGGER);
        //$$ TagValueOutput nbtWriteView2 = TagValueOutput.createWithContext(logging, entity.registryAccess());
        //$$ nbtWriteView2.buildResult().merge(nbtCompound);
        //$$ entity.saveWithoutId(nbtWriteView2);
        //$$ nbtCompound = nbtWriteView2.buildResult();
        //#endif
        return nbtCompound;
    }

    public static void writeNbtToBlockEntity(Level level, BlockEntity newBlock, CompoundTag nbt) {
        if (newBlock != null && nbt != null) {
            //#if MC <= 12004
            //$$ newBlock.load(nbt);
            //#elseif MC > 12004 && MC <= 12105
            newBlock.loadWithComponents(nbt, level.registryAccess());
            //#elseif MC > 12105
            //$$ try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(newBlock.problemPath(), LogUtils.getLogger())) {
            //$$    newBlock.loadWithComponents(TagValueInput.create(reporter, level.registryAccess(), nbt));
            //$$ }
            //#endif
        }
    }

    public static CompoundTag readNbtFromBlockEntity(Level level, BlockState state, BlockPos blockPos) {
        CompoundTag nbt;

        if (state.is(BlockTags.SHULKER_BOXES)) {
            BlockEntity oldBlock = level.getBlockEntity(blockPos);
            if (oldBlock != null) {
                //#if MC > 12004
                nbt = oldBlock.saveWithoutMetadata(level.registryAccess());
                //#else
                //$$ nbt = oldBlock.saveWithoutMetadata();
                //#endif
                return nbt;
            }
        }
        return null;
    }
}
