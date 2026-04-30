package carpet_cuo.mixins.InfinitelyVaultMixin;
//#if MC < 12100
//$$import carpet_cuo.utils.compat.DummyClass;
//$$import org.spongepowered.asm.mixin.Mixin;
//$$
//$$@Mixin(DummyClass.class)
//$$public abstract class VaultServerDateMixin {}
//#elseif MC >=12100
import carpet_cuo.Carpet_CuOSettings;
import java.util.Set;
import java.util.UUID;
import net.minecraft.world.level.block.entity.vault.VaultServerData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VaultServerData.class)
public abstract class VaultServerDateMixin {
    @Shadow
    @Final
    private Set<UUID> rewardedPlayers;

    @Inject(
            method = "addToRewardedPlayers",
            at = @At("HEAD")
    )
    private void cleanRewardedPlayersSet(CallbackInfo ci){
        if (!Carpet_CuOSettings.infinitelyVault)return;
        this.rewardedPlayers.clear();
        ci.cancel();
    }
}
//#endif
