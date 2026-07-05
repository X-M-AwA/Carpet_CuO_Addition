package carpet_cuo.mixins.rules.EntityHighLightMixin;

import carpet_cuo.Carpet_CuOSettings;
import carpet_cuo.rule.IEntityColor;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
@Implements(@Interface(iface = IEntityColor.class, prefix = "color$"))
public abstract class EntityMixin implements IEntityColor {

    @Unique
    private int highlightColor = 0x000000;

    public int color$getHighlightColor() {
        return highlightColor;
    }

    public void color$setHighlightColor(int color) {
        this.highlightColor = color;
    }

    @ModifyReturnValue(
            method = "getTeamColor",
            at = @At("RETURN")
    )
    private int setColor(int original) {
        if (Carpet_CuOSettings.entityHighLight) {
            Entity entity = (Entity) (Object) this;
            return ((IEntityColor) entity).getHighlightColor();
        }
        return original;
    }
}
