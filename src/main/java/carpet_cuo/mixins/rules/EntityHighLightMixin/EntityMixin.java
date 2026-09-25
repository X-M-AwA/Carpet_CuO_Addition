package carpet_cuo.mixins.rules.EntityHighLightMixin;

import carpet_cuo.Carpet_CuOSettings;
import carpet_cuo.rule.EntityHighLight.IEntityColor;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
@Implements(@Interface(iface = IEntityColor.class, prefix = "carpet_cuo$"))
public abstract class EntityMixin implements IEntityColor {

    @Unique
    private int highlightColor = 0x000000;

    public int carpet_cuo$getHighlightColor() {
        return this.highlightColor;
    }

    public void carpet_cuo$setHighlightColor(int color) {
        this.highlightColor = color;
    }

    @ModifyReturnValue(
            method = "getTeamColor",
            at = @At("RETURN")
    )
    private int setColor(int original) {
        if (Carpet_CuOSettings.entityHighLight) {
            return this.getHighlightColor();
        }
        return original;
    }
}
