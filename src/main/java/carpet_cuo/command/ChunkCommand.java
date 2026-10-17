package carpet_cuo.command;

import carpet_cuo.Carpet_CuOSettings;
import carpet_cuo.mixins.command.ChunkMapAccessor;
import carpet_cuo.mixins.command.ThreadedLevelLightEngineInvoker;
import carpet_cuo.utils.Messenger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.*;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
//#if MC >= 12111
//$$ import net.minecraft.server.permissions.Permission;
//$$ import net.minecraft.server.permissions.PermissionLevel;
//$$ import net.minecraft.server.permissions.Permissions;
//#endif


public class ChunkCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("chunk")
                        .requires(source ->
                                //#if MC < 12111
                                source.hasPermission(Carpet_CuOSettings.chunkCommandLevel))
                        //#else
                        //$$ source.permissions().hasPermission(getLevel(Carpet_CuOSettings.chunkCommandLevel)))
                        //#endif
                        .then(Commands.literal("remove")
                                .executes(ctx -> remove(ctx, false))
                                .then(Commands.argument("x", IntegerArgumentType.integer())
                                        .then(Commands.argument("z", IntegerArgumentType.integer())
                                                .executes(ctx -> remove(ctx, true))
                                        )
                                )
                        )
                        .then(Commands.literal("regen")
                                .executes(ctx -> regen(ctx, false))
                                .then(Commands.argument("x", IntegerArgumentType.integer())
                                        .then(Commands.argument("z", IntegerArgumentType.integer())
                                                .executes(ctx -> regen(ctx, true))
                                        )
                                )
                        )
        );
    }

    private static int remove(CommandContext<CommandSourceStack> ctx, boolean hasArgs) {
        CommandSourceStack source = ctx.getSource();
        ChunkPos chunkPos = getChunkPos(ctx, hasArgs);
        if (chunkPos != null) {
            removeChunk(source.getLevel(), chunkPos);
            source.sendSuccess(() -> Messenger.c(Messenger.tr("carpet.command.chunk.remove"), Messenger.s(chunkPos)), false);
            return 1;
        }
        return 0;
    }

    private static int regen(CommandContext<CommandSourceStack> ctx, boolean hasArgs) {
        CommandSourceStack source = ctx.getSource();
        ChunkPos chunkPos = getChunkPos(ctx, hasArgs);
        if (chunkPos != null) {
            regenChunk(source.getLevel(), chunkPos);
            source.sendSuccess(() -> Messenger.c(Messenger.tr("carpet.command.chunk.regen"), Messenger.s(chunkPos)), false);
            return 1;
        }
        return 0;
    }

    public static void removeChunk(ServerLevel level, ChunkPos chunkPos) {
        ServerChunkCache chunkSource = level.getChunkSource();
        ChunkMap chunkMap = chunkSource.chunkMap;
        //#if MC >= 12103
        chunkMap.write(chunkPos, () -> null);
        //#else
        //$$ chunkMap.write(chunkPos, null);
        //#endif
    }

    public static void regenChunk(ServerLevel level, ChunkPos chunkPos) {
        ServerChunkCache chunkSource = level.getChunkSource();
        ChunkMap chunkMap = chunkSource.chunkMap;
        ChunkMapAccessor accessor = (ChunkMapAccessor) chunkMap;
        //#if MC >= 260102
        //$$ long key = chunkPos.pack();
        //#else
        long key = chunkPos.toLong();
        //#endif

        //#if MC <= 1.21.11
        LevelChunk chunk = chunkSource.getChunkNow(chunkPos.x, chunkPos.z);
        //#else
        //$$ LevelChunk chunk = chunkSource.getChunkNow(chunkPos.x(), chunkPos.z());
        //#endif
        ChunkHolder holder = accessor.carpet_cuo$getUpdatingChunkMap().remove(key);
        accessor.carpet_cuo$getVisibleChunkMap().remove(key);
        accessor.carpet_cuo$getPendingUnloads().remove(key);
        removeChunk(level, chunkPos);

        if (chunk != null) {
            chunk.setLoaded(false);
            level.unload(chunk);
            ThreadedLevelLightEngine lightEngine = chunkSource.getLightEngine();
            ((ThreadedLevelLightEngineInvoker) lightEngine).carpet_cuo$updateChunkStatus(chunkPos);
            lightEngine.tryScheduleUpdate();
        }

        if (holder != null && ChunkLevel.isLoaded(holder.getTicketLevel())) {
            int ticketLevel = holder.getTicketLevel();
            //#if MC >= 12105
            //$$ int radius = ChunkLevel.byStatus(FullChunkStatus.FULL) - Math.max(0, ticketLevel - 1);
            //$$ chunkSource.addTicketWithRadius(TicketType.UNKNOWN, chunkPos, radius);
            //$$ chunkSource.removeTicketWithRadius(TicketType.UNKNOWN, chunkPos, radius);
            //#else
            int regenLevel = Math.max(0, ticketLevel - 1);
            DistanceManager distanceManager = chunkMap.getDistanceManager();
            distanceManager.addTicket(TicketType.UNKNOWN, chunkPos, regenLevel, chunkPos);
            distanceManager.removeTicket(TicketType.UNKNOWN, chunkPos, regenLevel, chunkPos);
            //#endif
        }
    }

    public static ChunkPos getChunkPos(CommandContext<CommandSourceStack> ctx, boolean hasArgs) {
        CommandSourceStack source = ctx.getSource();
        ServerPlayer player = source.getPlayer();
        ChunkPos chunkPos;
        if (player == null && !hasArgs) return null;

        int x, z;
        if (hasArgs) {
            x = IntegerArgumentType.getInteger(ctx, "x");
            z = IntegerArgumentType.getInteger(ctx, "z");
            chunkPos = new ChunkPos(x, z);
        } else {
            chunkPos = player.chunkPosition();
        }
        return chunkPos;
    }

    //#if MC >= 12111
    /*$$
    private static Permission getLevel(int i) {
        switch (i) {
            case 0 ->{
                return new Permission.HasCommandLevel(PermissionLevel.ALL);
            }case 1 -> {
                return Permissions.COMMANDS_MODERATOR;
            }case 2 -> {
                return Permissions.COMMANDS_GAMEMASTER;
            }case 3 -> {
                return Permissions.COMMANDS_ADMIN;
            }case 4 -> {
                return Permissions.COMMANDS_OWNER;
            }
        }
        return null;
    }
    $$*/
    //#endif
}
