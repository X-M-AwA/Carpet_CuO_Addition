package carpet_cuo.mixins.rules.GameTickOptimizationMixin;

import carpet_cuo.Carpet_CuOSettings;
import carpet_cuo.rule.GameTickOptimization;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityTickList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void tick(BooleanSupplier booleanSupplier, CallbackInfo ci) {
        GameTickOptimization.setSkipPhases(Carpet_CuOSettings.gameTickOptimization);
    }

    //天气更新
    @Inject(
            method = "advanceWeatherCycle",
            at = @At("HEAD"),
            cancellable = true
    )
    private void weather(CallbackInfo ci) {
        if (GameTickOptimization.weather) ci.cancel();
    }

    //方块计划刻
    @Inject(
            method = "tickBlock",
            at = @At("HEAD"),
            cancellable = true
    )
    private void tickBlock(CallbackInfo ci) {
        if (GameTickOptimization.scheduleTick) ci.cancel();
    }

    //流体计划刻
    @Inject(
            method = "tickFluid",
            at = @At("HEAD"),
            cancellable = true
    )
    private void tickFluid(CallbackInfo ci) {
        if (GameTickOptimization.scheduleTick) ci.cancel();
    }

    //方块事件
    @Inject(
            method = "runBlockEvents",
            at = @At("HEAD"),
            cancellable = true
    )
    private void blockEvents(CallbackInfo ci) {
        if (GameTickOptimization.blockEvent) ci.cancel();
    }

    //实体
    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/entity/EntityTickList;forEach(Ljava/util/function/Consumer;)V"
            )
    )
    private void entityUpdate(EntityTickList instance, Consumer<Entity> entityConsumer) {
        if (GameTickOptimization.entityUpdate) {
            Consumer<Entity> wrappedConsumer = entity -> {
                if (entity instanceof Player) {
                    entityConsumer.accept(entity);
                }
            };

            instance.forEach(wrappedConsumer);
        }else instance.forEach(entityConsumer);
    }
}
