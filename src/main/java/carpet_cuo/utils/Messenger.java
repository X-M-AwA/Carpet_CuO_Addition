package carpet_cuo.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.ChatFormatting;
import org.jetbrains.annotations.NotNull;


public class Messenger {

    public static MutableComponent c(Object... fields) {
        return (MutableComponent) carpet.utils.Messenger.c(fields);
    }

    public static MutableComponent s(Object text) {
        return Component.literal(text.toString());
    }

    @NotNull
    public static MutableComponent f(MutableComponent text, ChatFormatting... formattings) {
        return text.withStyle(formattings);
    }

    public static MutableComponent tr(String key, Object... args) {
        return Component.translatable(key, args);
    }

    public static MutableComponent hover(MutableComponent text, HoverEvent hoverEvent) {
        return text.withStyle(style -> style.withHoverEvent(hoverEvent));
    }
}
