package carpet_cuo.command;

import carpet_cuo.Carpet_CuOSettings;
import carpet_cuo.utils.Messenger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
//#if MC >= 12111
//$$ import net.minecraft.core.SectionPos;
//$$ import net.minecraft.server.permissions.Permission;
//$$ import net.minecraft.server.permissions.PermissionLevel;
//$$ import net.minecraft.server.permissions.Permissions;
//#endif
import java.util.ArrayList;

public class deletedChunk {
    public static final ArrayList<ChunkPos> chunks = new  ArrayList<>();
    public static final ArrayList<ServerLevel> levels = new  ArrayList<>();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("deleted")
                        .then(Commands.literal("chunk")
                                .requires(source ->
                                        //#if MC < 12111
                                        source.hasPermission(Carpet_CuOSettings.removeCommandLevel))
                                //#else
                                //$$ source.permissions().hasPermission(getLevel(Carpet_CuOSettings.removeCommandLevel)))
                                //#endif
                                .executes(ctx -> execute(ctx, false))
                                .then(Commands.argument("x", IntegerArgumentType.integer())
                                        .then(Commands.argument("z", IntegerArgumentType.integer())
                                                .executes(ctx -> execute(ctx, true))
                                        )
                                )
                        )
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx, boolean hasArgs) {
        CommandSourceStack source = ctx.getSource();
        ServerPlayer player = source.getPlayer();
        ChunkPos chunkPos;
        if (player == null) {
            return 0;
        }

        int x, z;
        if (hasArgs) {
            x = IntegerArgumentType.getInteger(ctx, "x");
            z = IntegerArgumentType.getInteger(ctx, "z");
            chunkPos = new ChunkPos(x, z);
        } else {
            BlockPos pos = player.blockPosition();
            //#if MC < 260102
            chunkPos = new ChunkPos(pos);
            //#else
            //$$ x = SectionPos.blockToSectionCoord(pos.getX());
            //$$ z = SectionPos.blockToSectionCoord(pos.getZ());
            //$$ chunkPos = new ChunkPos(x, z);
            //#endif
        }
        chunks.add(chunkPos);
        levels.add(source.getLevel());
        source.sendSuccess(() -> Messenger.s("Successfully deleted, ChunkPos " + chunkPos), false);
        return 1;
    }

    //#if MC >= 12111
    //$$private static Permission getLevel(int i) {
    //$$    switch (i) {
    //$$        case 0 ->{
    //$$            return new Permission.HasCommandLevel(PermissionLevel.ALL);
    //$$        }case 1 -> {
    //$$            return Permissions.COMMANDS_MODERATOR;
    //$$        }case 2 -> {
    //$$            return Permissions.COMMANDS_GAMEMASTER;
    //$$        }case 3 -> {
    //$$            return Permissions.COMMANDS_ADMIN;
    //$$        }case 4 -> {
    //$$            return Permissions.COMMANDS_OWNER;
    //$$        }
    //$$    }
    //$$    return null;
    //$$}
    //#endif
}
