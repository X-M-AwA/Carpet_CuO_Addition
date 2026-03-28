package carpet_cuo.mixins;

//#if MC <= 12004
//$$import net.minecraft.world.level.block.state.BlockState;
//$$import net.minecraft.world.level.block.Blocks;
//$$import net.minecraft.world.item.ItemStack;
//$$import net.minecraft.world.item.DiggerItem;
//$$import net.minecraft.world.item.PickaxeItem;
//$$import carpet_cuo.Carpet_CuOSettings;
//$$import org.spongepowered.asm.mixin.Final;
//$$import org.spongepowered.asm.mixin.Mixin;
//$$import org.spongepowered.asm.mixin.Shadow;
//$$import org.spongepowered.asm.mixin.injection.At;
//$$import org.spongepowered.asm.mixin.injection.Inject;
//$$import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//$$
//$$@Mixin(DiggerItem.class)
//$$public abstract class ItemMixin {
//$$    @Shadow
//$$    @Final
//$$    protected float speed;
//$$
//$$    @Inject(
//$$            method = "getDestroySpeed",
//$$            at = @At("HEAD"),
//$$            cancellable = true
//$$    )
//$$    private void mining(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
//$$        if ((Carpet_CuOSettings.endPortalFrameCanBeMined || Carpet_CuOSettings.bedrockCanBeMined) && (state.getBlock() == Blocks.END_PORTAL_FRAME || state.getBlock() == Blocks.BEDROCK)) {
//$$            DiggerItem toolItem = (DiggerItem) (Object) this;
//$$            if (toolItem instanceof PickaxeItem) {
//$$                cir.setReturnValue(this.speed);
//$$            }
//$$        }
//$$    }
//$$}
//#elseif MC > 12004
import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(
            method = "getDestroySpeed",
            at = @At("HEAD"),
            cancellable = true
    )
    private void miningSpeed(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
        if ((Carpet_CuOSettings.endPortalFrameCanBeMined || Carpet_CuOSettings.bedrockCanBeMined) && (state.is(Blocks.END_PORTAL_FRAME) || state.is(Blocks.BEDROCK))) {
            Tool tool = stack.get(DataComponents.TOOL);
            if (tool == null) return;
            cir.setReturnValue(tool.getMiningSpeed(Blocks.OBSIDIAN.defaultBlockState()));
        }
    }
}
//#endif