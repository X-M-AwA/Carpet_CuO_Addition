package carpet_cuo.mixins.rules.BedrockCanBeMinedMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
//#if MC > 12002
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#else
//$$import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#endif

@Mixin(Block.class)
public abstract class BlockMixin {
    @Shadow
    public static void popResource(Level level, BlockPos blockPos, ItemStack itemStack) {}

    @Inject(
            method = "playerWillDestroy",
            at = @At("HEAD")
    )
    //#if MC > 12002
    private void onBreak(Level level, BlockPos pos, BlockState state, Player player, CallbackInfoReturnable<BlockState> cir) {
        //#else
        //$$ private void onBreak(Level level, BlockPos pos, BlockState state, Player player, CallbackInfo ci) {
        //#endif
        if (Carpet_CuOSettings.bedrockCanBeMined && state.is(Blocks.BEDROCK) && !player.isCreative()) {
            ItemStack tool = player.getMainHandItem();
            if (tool.isCorrectToolForDrops(Blocks.OBSIDIAN.defaultBlockState())) {
                ItemStack drop = new ItemStack(Items.BEDROCK);
                popResource(level, pos, drop);
            }
        }
    }
}
