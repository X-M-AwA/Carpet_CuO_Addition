package carpet_cuo.mixins;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
//#if MC > 12002
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#else
//$$import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#endif

@Mixin(Block.class)
public abstract class BlockMixin {
    @Unique
    private boolean TheRightTool(Player player){
        ItemStack itemStack = player.getMainHandItem();
        return itemStack.is(Items.DIAMOND_PICKAXE) || itemStack.is(Items.NETHERITE_PICKAXE);
    }

    @Inject(
            method = "playerWillDestroy",
            at = @At("HEAD")
    )
    //#if MC > 12002
    private void onBreak(Level world, BlockPos pos, BlockState state, Player player, CallbackInfoReturnable<BlockState> cir) {
    //#else
    //$$private void onBreak(Level world, BlockPos pos, BlockState state, Player player, CallbackInfo ci) {
    //#endif
        if (Carpet_CuOSettings.endPortalFrameCanBeMined && state.is(Blocks.END_PORTAL_FRAME) && !player.isCreative() && TheRightTool(player)) {
            ItemStack portalFrameStack = new ItemStack(Items.END_PORTAL_FRAME);
            ItemEntity itemEntity = new ItemEntity(
                    world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    portalFrameStack
            );
            world.addFreshEntity(itemEntity);
        } else if (Carpet_CuOSettings.bedrockCanBeMined && state.is(Blocks.BEDROCK) && !player.isCreative() && TheRightTool(player)) {
            ItemStack portalFrameStack = new ItemStack(Items.BEDROCK);
            ItemEntity itemEntity = new ItemEntity(
                    world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    portalFrameStack
            );
            world.addFreshEntity(itemEntity);
        }
    }
}
