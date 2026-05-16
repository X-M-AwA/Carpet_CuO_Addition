package carpet_cuo.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
//#if MC >= 12106
//$$ import carpet_cuo.Carpet_CuOServer;
//$$ import net.minecraft.util.ProblemReporter;
//$$ import net.minecraft.world.level.storage.TagValueInput;
//$$ import net.minecraft.world.level.storage.TagValueOutput;
//$$ import net.minecraft.world.level.storage.ValueInput;
//#endif

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
}
