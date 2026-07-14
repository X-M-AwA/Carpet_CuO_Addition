package carpet_cuo.mixins.rules.CustomizeTheArrowOwnerMixin;

import carpet_cuo.Carpet_CuOSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.component.DataComponents;
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

@Mixin(ProjectileWeaponItem.class)
public class ProjectileWeaponItemMixin {
    @WrapOperation(
            method = "shoot",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ProjectileWeaponItem;createProjectile(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/entity/projectile/Projectile;")
    )
    private Projectile shoot(ProjectileWeaponItem instance, Level level, LivingEntity livingEntity, ItemStack itemStack, ItemStack itemStack2, boolean bl, Operation<Projectile> original) {
        Projectile projectile = original.call(instance, level, livingEntity, itemStack, itemStack2, bl);
        if (Carpet_CuOSettings.customizeTheArrowOwner) {
            var var0 = itemStack.get(DataComponents.CUSTOM_NAME);
            if (var0 != null) {
                String name = var0.getString();
                String uuid = searchUUID(name);
                if (uuid != null) {
                    try {
                        Entity entity = ((ServerLevel) level).getEntity(UUID.fromString(uuid));
                        if (entity instanceof LivingEntity) {
                            projectile.setOwner(entity);
                        }
                    } catch (IllegalArgumentException ignored) {}
                }
            }
        }
        return projectile;
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
