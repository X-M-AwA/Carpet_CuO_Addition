package carpet_cuo.rule.InfiniteBuff;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
//#if MC >= 12006
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
//#else
//$$ import net.minecraft.world.item.alchemy.Potion;
//$$ import net.minecraft.world.item.alchemy.PotionUtils;
//$$ import net.minecraft.nbt.CompoundTag;
//$$ import net.minecraft.nbt.ListTag;
//#endif

import java.util.ArrayList;

public class InfiniteBuff {
    private final ArrayList<MobEffectInstance> effects = new ArrayList<>();

    private void getEffects(ServerPlayer player) {
        this.effects.clear();

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

            if (!name.contains("[InfiniteBuff]")) continue;
            //#if MC >= 12006
            Consumable consumable = itemStack.get(DataComponents.CONSUMABLE);
            if (consumable != null) {
                for (ConsumeEffect consumeEffect : consumable.onConsumeEffects()) {
                    if ((consumeEffect instanceof ApplyStatusEffectsConsumeEffect statusEffectsConsumeEffect)) {
                        for (MobEffectInstance mobEffectInstance : statusEffectsConsumeEffect.effects()) {
                            this.effects.add(new MobEffectInstance(mobEffectInstance.getEffect(), 30, mobEffectInstance.getAmplifier()));
                        }
                    }
                }
            }
            //#endif

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
                    this.effects.add(new MobEffectInstance(mobEffectInstance.getEffect(), 30, mobEffectInstance.getAmplifier()));
                }
            }

            //#if MC >= 12006
            SuspiciousStewEffects stewEffects = itemStack.get(DataComponents.SUSPICIOUS_STEW_EFFECTS);
            if (stewEffects != null) {
                for (SuspiciousStewEffects.Entry entry : stewEffects.effects()) {
                    this.effects.add(new MobEffectInstance(entry.effect(), 30, entry.createEffectInstance().getAmplifier()));
                }
            }
            //#else
            //$$ CompoundTag nbt = itemStack.getTag();
            //$$ if (nbt != null && nbt.contains("Effects", 9)) {
            //$$     ListTag effectsList = nbt.getList("Effects", 10);
            //$$     for (CompoundTag tag : effectsList.toArray(new CompoundTag[0])) {
            //$$         MobEffectInstance mobEffectInstance = MobEffectInstance.load(tag);
            //$$         if (mobEffectInstance != null) {
            //$$             this.effects.add(new MobEffectInstance(mobEffectInstance.getEffect(), 30, mobEffectInstance.getAmplifier()));
            //$$         }
            //$$     }
            //$$ }
            //#endif
        }
    }

    public void tick(ServerPlayer player) {
        this.getEffects(player);
        for (MobEffectInstance mobEffectInstance : this.effects) {
            player.addEffect(mobEffectInstance);
        }
    }
}
