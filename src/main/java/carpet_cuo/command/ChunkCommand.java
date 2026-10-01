package carpet_cuo.command;

import carpet_cuo.Carpet_CuOSettings;
import carpet_cuo.utils.Messenger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.*;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelResource;
//#if MC >= 12111
//$$ import net.minecraft.server.permissions.Permission;
//$$ import net.minecraft.server.permissions.PermissionLevel;
//$$ import net.minecraft.server.permissions.Permissions;
//#endif

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static carpet_cuo.Carpet_CuOServer.LOGGER;

public class ChunkCommand {
    public static final ArrayList<ChunkPos> chunks = new  ArrayList<>();
    public static final ArrayList<ServerLevel> levels = new  ArrayList<>();

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
        );
    }

    private static int remove(CommandContext<CommandSourceStack> ctx, boolean hasArgs) {
        CommandSourceStack source = ctx.getSource();
        ChunkPos chunkPos = getChunkPos(ctx, hasArgs);
        if (chunkPos != null) {
            removeChunk(source.getLevel(), chunkPos);
            chunks.add(chunkPos);
            levels.add(source.getLevel());
            source.sendSuccess(() -> Messenger.c(Messenger.tr("carpet.command.chunk.remove"), Messenger.s(chunkPos)), false);
            return 1;
        }
        return 0;
    }

    public static void removeChunk(ServerLevel level, ChunkPos chunkPos) {
        int regionX = chunkPos.getRegionX();
        int regionZ = chunkPos.getRegionZ();
        int localX = chunkPos.getRegionLocalX();
        int localZ = chunkPos.getRegionLocalZ();
        int index = localX + localZ * 32;

        Path worldRoot = level.getServer().getWorldPath(LevelResource.ROOT);
        Path dimensionDir = DimensionType.getStorageFolder(level.dimension(), worldRoot);
        Path regionDir = dimensionDir.resolve("region");
        Path regionPath = regionDir.resolve(String.format("r.%d.%d.mca", regionX, regionZ));

        if (Files.exists(regionPath)) {
            try (RandomAccessFile raf = new RandomAccessFile(regionPath.toFile(), "rw")) {
                raf.seek((long) index * 4);
                raf.writeInt(0);

                raf.seek(4096L + (long) index * 4);
                raf.writeInt(0);
            } catch (IOException e) {
                LOGGER.error("Failed to remove chunk {}: ", chunkPos, e);
            }
        }
    }

    public static ChunkPos getChunkPos(CommandContext<CommandSourceStack> ctx, boolean hasArgs) {
        CommandSourceStack source = ctx.getSource();
        ServerPlayer player = source.getPlayer();
        ChunkPos chunkPos;
        if (player == null) return null;

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
