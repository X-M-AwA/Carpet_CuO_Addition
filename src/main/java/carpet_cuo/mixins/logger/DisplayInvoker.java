package carpet_cuo.mixins.logger;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Display.TextDisplay.class)
public interface DisplayInvoker {
    @Invoker("setText")
    void carpet_cuo$setText(Component text);
}
