package carpet_cuo.utils;

import net.minecraft.network.chat.Component;
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
    public static MutableComponent f(MutableComponent text, LayOut... formattings) {
        ChatFormatting[] chatFormattings = new ChatFormatting[formattings.length];

        for (int i = 0; i < formattings.length; i++) {
            chatFormattings[i] = formattings[i].getFormatting();
        }

        return text.withStyle(chatFormattings);
    }

    public static MutableComponent tr(String key, Object... args) {
        return Component.translatable(key, args);
    }
}
