package carpet_cuo.Logging;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
//#if MC >= 260102
//$$import net.minecraft.network.chat.*;
//#else
import net.minecraft.network.chat.Component;
//#endif

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractLogger {
    private final String NAME;

    public AbstractLogger(String name) {
        this.NAME = name;
    }

    public String getName() {
        return this.NAME;
    }

    protected void actionWithLogger(Consumer<Logger> action){
        Logger logger = LoggerRegistry.getLogger(this.getName());
        if (logger != null){
            action.accept(logger);
        }
    }

    public void log(
            Supplier<Component[]> messagePromise
    ){
        actionWithLogger(logger -> logger.log(messagePromise));
    }

    public void log(Logger.lMessageIgnorePlayer messagePromise){
        actionWithLogger(logger -> logger.log(messagePromise));
    }
}
