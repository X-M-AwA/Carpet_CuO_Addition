package carpet_cuo.rule.MainThreadChunkLoading;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.util.thread.BlockableEventLoop;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public final class SwitchableExecutor implements Executor {
    private final Executor asyncExecutor;
    private final BlockableEventLoop<Runnable> mainThreadExecutor;

    public SwitchableExecutor(Executor asyncExecutor, BlockableEventLoop<Runnable> mainThreadExecutor) {
        this.asyncExecutor = asyncExecutor;
        this.mainThreadExecutor = mainThreadExecutor;
    }

    @Override
    public void execute(@NotNull Runnable task) {
        if (Carpet_CuOSettings.mainThreadChunkLoading) {
            this.mainThreadExecutor.execute(task);
        } else {
            this.asyncExecutor.execute(task);
        }
    }
}
