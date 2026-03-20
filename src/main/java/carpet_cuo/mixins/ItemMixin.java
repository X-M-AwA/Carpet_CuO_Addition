package carpet_cuo.mixins;
//#if MC <= 12004
//$$import net.minecraft.block.BlockState;
//$$import net.minecraft.block.Blocks;
//$$import net.minecraft.item.ItemStack;
//$$import net.minecraft.item.MiningToolItem;
//$$import net.minecraft.item.PickaxeItem;
//$$import carpet_cuo.Carpet_CuOSettings;
//$$import org.spongepowered.asm.mixin.Final;
//$$import org.spongepowered.asm.mixin.Mixin;
//$$import org.spongepowered.asm.mixin.Shadow;
//$$import org.spongepowered.asm.mixin.injection.At;
//$$import org.spongepowered.asm.mixin.injection.Inject;
//$$import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//$$
//$$@Mixin(MiningToolItem.class)
//$$public abstract class ItemMixin {
//$$    @Shadow
//$$    @Final
//$$    protected float miningSpeed;
//$$
//$$    @Inject(
//$$            method = "getMiningSpeedMultiplier",
//$$            at = @At("HEAD"),
//$$            cancellable = true
//$$    )
//$$    private void mining(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
//$$        if ((Carpet_CuOSettings.endPortalFrameCanBeMined || Carpet_CuOSettings.bedrockCanBeMined) && (state.getBlock() == Blocks.END_PORTAL_FRAME || state.getBlock() == Blocks.BEDROCK)) {
//$$            MiningToolItem toolItem = (MiningToolItem) (Object) this;
//$$            if (toolItem instanceof PickaxeItem) {
//$$                cir.setReturnValue(this.miningSpeed);
//$$            }
//$$        }
//$$    }
//$$}
//#elseif MC > 12004
import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(
            method = "getMiningSpeed",
            at = @At("HEAD"),
            cancellable = true
    )
    private void miningSpeed(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
        if ((Carpet_CuOSettings.endPortalFrameCanBeMined || Carpet_CuOSettings.bedrockCanBeMined) && (state.isOf(Blocks.END_PORTAL_FRAME) || state.isOf(Blocks.BEDROCK))) {
            ToolComponent tool = stack.get(DataComponentTypes.TOOL);
            if (tool == null) return;
            cir.setReturnValue(tool.getSpeed(Blocks.OBSIDIAN.getDefaultState()));
        }
    }
}
//#endif