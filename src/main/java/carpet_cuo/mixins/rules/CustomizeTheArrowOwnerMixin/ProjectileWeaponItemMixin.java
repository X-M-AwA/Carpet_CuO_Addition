package carpet_cuo.mixins.rules.CustomizeTheArrowOwnerMixin;

import carpet_cuo.Carpet_CuOSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//#if MC > 12004
import net.minecraft.core.component.DataComponents;
//#else
//$$ import net.minecraft.world.item.BowItem;
//$$ import net.minecraft.world.entity.projectile.AbstractArrow;
//$$ import com.llamalad7.mixinextras.sugar.Local;
//#endif
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.UUID;

//#if MC > 12004
@Mixin(ProjectileWeaponItem.class)
//#else
//$$ @Mixin(BowItem.class)
//#endif
public abstract class ProjectileWeaponItemMixin {
    //#if MC > 12004
    @WrapOperation(
            method = "shoot",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ProjectileWeaponItem;createProjectile(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/entity/projectile/Projectile;")
    )
    //#else
    //$$ @WrapOperation(
    //$$         method = "releaseUsing",
    //$$         at = @At(value = "INVOKE",
    //$$                 target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z")
    //$$ )
    //#endif
    //#if MC > 12004
    private Projectile shoot(
    //#else
    //$$ private boolean shoot(
    //#endif
            //#if MC > 12004
            ProjectileWeaponItem instance, Level level, LivingEntity livingEntity, ItemStack itemStack, ItemStack itemStack2, boolean bl, Operation<Projectile> original
            //#else
            //$$ Level level, Entity entity, Operation<Boolean> original, @Local AbstractArrow abstractArrow, @Local(ordinal = 0, argsOnly = true) ItemStack itemStack
            //#endif
    ) {
        //#if MC > 12004
        Projectile projectile = original.call(instance, level, livingEntity, itemStack, itemStack2, bl);
        //#endif
        if (Carpet_CuOSettings.customizeTheArrowOwner) {
            //#if MC > 12004
            var var0 = itemStack.get(DataComponents.CUSTOM_NAME);
            if (var0 != null) {
                String name = var0.getString();
            //#else
            //$$ if (itemStack.hasCustomHoverName()) {
            //$$    String name = itemStack.getHoverName().getString();
            //#endif
                String uuid = searchUUID(name);
                if (uuid != null) {
                    try {
                        Entity entity1 = ((ServerLevel) level).getEntity(UUID.fromString(uuid));
                        if (entity1 instanceof LivingEntity) {
                            //#if MC > 12004
                            projectile.setOwner(entity1);
                            //#else
                            //$$ abstractArrow.setOwner(entity1);
                            //#endif
                        }
                    } catch (IllegalArgumentException ignored) {}
                }
            }
        }
        //#if MC > 12004
        return projectile;
        //#else
        //$$ return original.call(level, entity);
        //#endif
    }

    @Unique
    private String searchUUID(String name) {
        int left = name.indexOf("[");
        int right = name.lastIndexOf("]");
        if (left != -1 && right != -1 && left < right) {
            return name.substring(left + 1, right);
        }
        return null;
    }
}
