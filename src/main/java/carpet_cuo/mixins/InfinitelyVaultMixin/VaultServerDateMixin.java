package carpet_cuo.mixins.InfinitelyVaultMixin;
//#if MC < 12100
//$$import carpet_cuo.utils.compat.DummyClass;
//$$import org.spongepowered.asm.mixin.Mixin;
//$$
//$$@Mixin(DummyClass.class)
//$$public abstract class VaultServerDateMixin {}
//#elseif MC >=12100
import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.level.block.entity.vault.VaultServerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(VaultServerData.class)
public abstract class VaultServerDateMixin {
    @ModifyConstant(
            method = "addToRewardedPlayers",
            constant = @Constant(intValue = 128)
    )
    private int modifyRewardedPlayersSize(int original){
        if (Carpet_CuOSettings.infinitelyVault)return 0;
        else return original;
    }
}
//#endif
