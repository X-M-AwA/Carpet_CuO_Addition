package carpet_cuo.Logging;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;

import java.util.function.Consumer;

public abstract class AbstractLogger {
    private final String NAME;

    public AbstractLogger(String name) {
        this.NAME = name;
    }

    public String getName() {
        return this.NAME;
    }

    protected void actionWithLogger(Consumer<Logger> action) {
        Logger logger = LoggerRegistry.getLogger(this.getName());
        if (logger != null) {
            action.accept(logger);
        }
    }

	public void log(Logger.lMessageIgnorePlayer messagePromise){
		actionWithLogger(logger -> logger.log(messagePromise));
	}
}
