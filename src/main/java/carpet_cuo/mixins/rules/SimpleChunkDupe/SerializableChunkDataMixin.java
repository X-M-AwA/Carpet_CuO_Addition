package carpet_cuo.mixins.rules.SimpleChunkDupe;

import carpet_cuo.Carpet_CuOSettings;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
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

import java.util.ArrayList;

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
            //#endif
            at = @At(
                    value = "INVOKE",
                    //#if MC >= 12103
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
                    //#else
                    //$$  target = "Lnet/minecraft/nbt/ListTag;add(Ljava/lang/Object;)Z",
                    //#endif
                    ordinal = 1
            )
    )
    private static void boom(ServerLevel serverLevel, ChunkAccess chunkAccess, CallbackInfoReturnable<CompoundTag> cir,
                             //#if MC >= 12103
                             @Local CompoundTag compoundTag3
                             //#else
                             //$$ @Local(ordinal = 1) CompoundTag compoundTag3
                             //#endif
    ) {
        if (Carpet_CuOSettings.simpleChunkDupe && compoundTag3.getString("CustomName").equals("\"ChunkDupe\"") && compoundTag3.getString("id").equals("minecraft:shulker_box")) {
            throw new RuntimeException();
        }
    }
}
