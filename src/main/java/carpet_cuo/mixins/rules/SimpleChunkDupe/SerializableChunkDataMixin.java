package carpet_cuo.mixins.rules.SimpleChunkDupe;

import carpet_cuo.Carpet_CuOSettings;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.storage.SerializableChunkData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SerializableChunkData.class)
public abstract class SerializableChunkDataMixin<T> {
    @Inject(
            method = "copyOf",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
                    ordinal = 1
            )
    )
    private static void boom(ServerLevel serverLevel, ChunkAccess chunkAccess, CallbackInfoReturnable<SerializableChunkData> cir, @Local CompoundTag compoundTag) {
        if (Carpet_CuOSettings.simpleChunkDupe && compoundTag.getString("CustomName").equals("\"ChunkDupe\"") && compoundTag.getString("id").equals("minecraft:shulker_box")) {
            try {
                throw new OutOfMemoryError();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }
}
