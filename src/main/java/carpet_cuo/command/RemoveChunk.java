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

public class RemoveChunk {
    public static final ArrayList<ChunkPos> chunks = new ArrayList<>();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("remove")
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
            //$$ chunkPos = new ChunkPos(pos.getX(), pos.getZ());
            //#endif
        }
        chunks.add(chunkPos);
        removeChunk(source.getLevel(), chunkPos);
        source.sendSuccess(() -> Messenger.s("Successfully deleted"), false);
        return 1;
    }

    private static void removeChunk(ServerLevel level, ChunkPos chunkPos) {
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
