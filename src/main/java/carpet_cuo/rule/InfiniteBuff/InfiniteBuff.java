package carpet_cuo.rule.InfiniteBuff;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
//#if MC >= 12103
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
//#else
//$$ import net.minecraft.world.food.FoodProperties;
//#endif
//#if MC >= 12006
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.SuspiciousStewEffects;
//#else
//$$ import net.minecraft.world.item.alchemy.Potion;
//$$ import net.minecraft.world.item.alchemy.PotionUtils;
//$$ import com.mojang.datafixers.util.Pair;
//$$ import net.minecraft.core.registries.BuiltInRegistries;
//$$ import net.minecraft.nbt.CompoundTag;
//$$ import net.minecraft.nbt.ListTag;
//$$ import net.minecraft.nbt.NbtOps;
//$$ import net.minecraft.world.level.block.SuspiciousEffectHolder;
//#endif

import java.util.ArrayList;
import java.util.List;

public class InfiniteBuff {
    private final List<MobEffectInstance> pendingEffects = new ArrayList<>();

    private void collectEffects(ServerPlayer player) {
        this.pendingEffects.clear();

        for (ItemStack itemStack : player.getInventory().items) {
            if (itemStack.isEmpty()) continue;

            String name = "";
            //#if MC > 12004
            var var0 = itemStack.get(DataComponents.CUSTOM_NAME);
            if (var0 != null) {
                name = var0.getString();
                //#else
                //$$ if (itemStack.hasCustomHoverName()) {
                //$$    name = itemStack.getHoverName().getString();
                //#endif
            }

            if (name.contains("[InfiniteBuff]")) {
                this.collectFoodEffects(itemStack);
                this.collectPotionEffects(itemStack);
                this.collectSuspiciousStewEffects(itemStack);
            }
        }
    }

    private void collectFoodEffects(ItemStack itemStack) {
        //#if MC >= 12103
        Consumable consumable = itemStack.get(DataComponents.CONSUMABLE);
        if (consumable != null) {
            for (ConsumeEffect consumeEffect : consumable.onConsumeEffects()) {
                if (consumeEffect instanceof ApplyStatusEffectsConsumeEffect statusEffects) {
                    for (MobEffectInstance mobEffectInstance : statusEffects.effects()) {
                        this.addInfiniteEffect(mobEffectInstance);
                    }
                }
            }
        }
        //#elseif MC >= 12006
        //$$ FoodProperties foodProperties = itemStack.get(DataComponents.FOOD);
        //$$ if (foodProperties != null) {
        //$$     for (FoodProperties.PossibleEffect possibleEffect : foodProperties.effects()) {
        //$$         this.addInfiniteEffect(possibleEffect.effect());
        //$$     }
        //$$ }
        //#else
        //$$ FoodProperties foodProperties = itemStack.getItem().getFoodProperties();
        //$$ if (foodProperties != null) {
        //$$     for (Pair<MobEffectInstance, Float> instance : foodProperties.getEffects()) {
        //$$         this.addInfiniteEffect(instance.getFirst());
        //$$     }
        //$$ }
        //#endif
    }

    private void collectPotionEffects(ItemStack itemStack) {
        //#if MC >= 12006
        PotionContents contents = itemStack.get(DataComponents.POTION_CONTENTS);
        //#else
        //$$ Potion contents = PotionUtils.getPotion(itemStack);
        //#endif
        if (contents != null) {
            //#if MC >= 12006
            for (MobEffectInstance mobEffectInstance : contents.getAllEffects()) {
                //#else
                //$$ for (MobEffectInstance mobEffectInstance : contents.getEffects()) {
                //#endif
                this.addInfiniteEffect(mobEffectInstance);
            }
        }
    }

    private void collectSuspiciousStewEffects(ItemStack itemStack) {
        //#if MC >= 12006
        SuspiciousStewEffects stewEffects = itemStack.get(DataComponents.SUSPICIOUS_STEW_EFFECTS);
        if (stewEffects != null) {
            for (SuspiciousStewEffects.Entry entry : stewEffects.effects()) {
                this.addInfiniteEffect(entry.effect());
            }
        }
        //#elseif MC >= 12002
        //$$ CompoundTag compoundTag = itemStack.getTag();
        //$$ if (compoundTag != null && compoundTag.contains("effects", 9)) {
        //$$     var list = SuspiciousEffectHolder.EffectEntry.LIST_CODEC
        //$$             .parse(NbtOps.INSTANCE, compoundTag.getList("effects", 10))
        //$$             .result().orElse(java.util.Collections.emptyList());
        //$$     for (SuspiciousEffectHolder.EffectEntry entry : list) {
        //$$         this.addInfiniteEffect(entry.effect());
        //$$     }
        //$$ }
        //#else
        //$$ CompoundTag compoundTag = itemStack.getTag();
        //$$ if (compoundTag != null && compoundTag.contains("Effects", 9)) {
        //$$     ListTag listTag = compoundTag.getList("Effects", 10);
        //$$     for (int i = 0; i < listTag.size(); ++i) {
        //$$         CompoundTag compoundTag2 = listTag.getCompound(i);
        //$$         MobEffect mobEffect = MobEffect.byId(compoundTag2.getInt("EffectId"));
        //$$         if (mobEffect != null) {
        //$$             this.addInfiniteEffect(mobEffect);
        //$$         }
        //$$     }
        //$$ }
        //#endif
    }

    private void addInfiniteEffect(MobEffectInstance sourceEffect) {
        this.pendingEffects.add(new MobEffectInstance(sourceEffect.getEffect(), 200, sourceEffect.getAmplifier()));
    }

    //#if MC >= 12006
    private void addInfiniteEffect(Holder<MobEffect> mobEffect) {
        //#else
        //$$ private void addInfiniteEffect(MobEffect mobEffect) {
        //#endif
        this.pendingEffects.add(new MobEffectInstance(mobEffect, 200));
    }

    public void tick(ServerPlayer player) {
        this.collectEffects(player);

        for (MobEffectInstance mobEffectInstance : this.pendingEffects) {
            player.addEffect(mobEffectInstance);
        }
    }
}