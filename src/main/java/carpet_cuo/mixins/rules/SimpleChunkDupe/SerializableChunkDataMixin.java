package carpet_cuo.mixins.rules.SimpleChunkDupe;

import carpet_cuo.Carpet_CuOSettings;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
//#if MC >= 12103
import net.minecraft.world.level.chunk.storage.SerializableChunkData;
//#else
//$$ import net.minecraft.world.level.chunk.storage.ChunkSerializer;
//#endif
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC >= 12103
@Mixin(SerializableChunkData.class)
//#else
//$$ @Mixin(ChunkSerializer.class)
//#endif
public abstract class SerializableChunkDataMixin {
    @Inject(
            //#if MC >= 12103
            method = "copyOf",
            //#else
            //$$ method = "write",
            at = @At(
                    value = "INVOKE",
                    //#if MC >= 12103
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
                    //#else
                    //$$ Lnet/minecraft/nbt/ListTag;add(Ljava/lang/Object;)Z,
                    //#endif
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
