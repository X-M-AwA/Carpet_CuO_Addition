package carpet_cuo.mixins;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
//#if MC <= 12002
//$$import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#elseif MC > 12002
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(
            method = "onBreak",
            at = @At("HEAD")
    )
    //#if MC > 12002
    private void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfoReturnable<BlockState> cir) {
    //#elseif MC <= 12002
    //$$private void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo ci) {
    //#endif
        if (Carpet_CuOSettings.endPortalFrameCanBeMined && !world.isClient() && state.isOf(Blocks.END_PORTAL_FRAME) && !player.isCreative() && TheRightTool(player) && world.isClient()) {
            ItemStack portalFrameStack = new ItemStack(Items.END_PORTAL_FRAME);
            ItemEntity itemEntity = new ItemEntity(
                    world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    portalFrameStack
            );
            world.spawnEntity(itemEntity);
        } else if (Carpet_CuOSettings.bedrockCanBeMined && !world.isClient() && state.isOf(Blocks.BEDROCK) && !player.isCreative() && TheRightTool(player) && world.isClient()) {
            ItemStack portalFrameStack = new ItemStack(Items.BEDROCK);
            ItemEntity itemEntity = new ItemEntity(
                    world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    portalFrameStack
            );
            world.spawnEntity(itemEntity);
        }
    }
    @Unique
    private boolean TheRightTool(PlayerEntity player){
        ItemStack itemStack = player.getMainHandStack();
        return itemStack.isOf(Items.DIAMOND_PICKAXE) || itemStack.isOf(Items.NETHERITE_PICKAXE);
    }
}
