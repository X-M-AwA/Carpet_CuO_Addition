package carpet_cuo.mixins.logger.FeatureMixin;

import carpet.logging.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(Logger.class)
public interface LoggerAccessor {
    @Accessor("subscribedOnlinePlayers")
    Map<String, String> getSubscribedOnlinePlayers();
}
