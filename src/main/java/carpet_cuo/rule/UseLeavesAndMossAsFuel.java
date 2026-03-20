package carpet_cuo.rule;

import carpet_cuo.Carpet_CuOSettings;
//#if MC >= 12102
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
//#elseif MC < 102102
//$$import net.fabricmc.fabric.api.registry.FuelRegistry;
//#endif
import net.minecraft.item.Items;
import net.minecraft.world.World;

public class UseLeavesAndMossAsFuel {
    public static void registerFuels(World world){
        //#if MC >= 12102
        if (Carpet_CuOSettings.useLeavesAndMossAsFuel && !world.isClient()){
            FuelRegistryEvents.BUILD.register((builder, context) -> {
                builder.add(Items.SPRUCE_LEAVES,300);
                builder.add(Items.FLOWERING_AZALEA_LEAVES,300);
                builder.add(Items.DARK_OAK_LEAVES,300);
                builder.add(Items.OAK_LEAVES,300);
                builder.add(Items.MANGROVE_LEAVES,300);
                builder.add(Items.JUNGLE_LEAVES,300);
                builder.add(Items.CHERRY_LEAVES,300);
                builder.add(Items.BIRCH_LEAVES,300);
                builder.add(Items.AZALEA_LEAVES,300);
                builder.add(Items.ACACIA_LEAVES,300);
                builder.add(Items.MOSS_BLOCK,300);
                builder.add(Items.MOSS_CARPET,50);
            });
        }
//#elseif MC < 12102
//$$        if (Carpet_CuOSettings.useLeavesAndMossAsFuel && !world.isClient()){
//$$            FuelRegistry.INSTANCE.add(Items.SPRUCE_LEAVES,300);
//$$            FuelRegistry.INSTANCE.add(Items.FLOWERING_AZALEA_LEAVES,300);
//$$            FuelRegistry.INSTANCE.add(Items.DARK_OAK_LEAVES,300);
//$$            FuelRegistry.INSTANCE.add(Items.OAK_LEAVES,300);
//$$            FuelRegistry.INSTANCE.add(Items.MANGROVE_LEAVES,300);
//$$            FuelRegistry.INSTANCE.add(Items.JUNGLE_LEAVES,300);
//$$            FuelRegistry.INSTANCE.add(Items.CHERRY_LEAVES,300);
//$$            FuelRegistry.INSTANCE.add(Items.BIRCH_LEAVES,300);
//$$            FuelRegistry.INSTANCE.add(Items.AZALEA_LEAVES,300);
//$$            FuelRegistry.INSTANCE.add(Items.ACACIA_LEAVES,300);
//$$            FuelRegistry.INSTANCE.add(Items.MOSS_BLOCK,300);
//$$            FuelRegistry.INSTANCE.add(Items.MOSS_CARPET,50);
//$$        }else {
//$$            FuelRegistry.INSTANCE.remove(Items.SPRUCE_LEAVES);
//$$            FuelRegistry.INSTANCE.remove(Items.FLOWERING_AZALEA_LEAVES);
//$$            FuelRegistry.INSTANCE.remove(Items.DARK_OAK_LEAVES);
//$$            FuelRegistry.INSTANCE.remove(Items.OAK_LEAVES);
//$$            FuelRegistry.INSTANCE.remove(Items.MANGROVE_LEAVES);
//$$            FuelRegistry.INSTANCE.remove(Items.JUNGLE_LEAVES);
//$$            FuelRegistry.INSTANCE.remove(Items.CHERRY_LEAVES);
//$$            FuelRegistry.INSTANCE.remove(Items.BIRCH_LEAVES);
//$$            FuelRegistry.INSTANCE.remove(Items.AZALEA_LEAVES);
//$$            FuelRegistry.INSTANCE.remove(Items.ACACIA_LEAVES);
//$$            FuelRegistry.INSTANCE.remove(Items.MOSS_BLOCK);
//$$            FuelRegistry.INSTANCE.remove(Items.MOSS_CARPET);
//$$        }
//#endif
    }
}
