package carpet_cuo.translations;

import carpet_cuo.utils.Messenger;
import net.minecraft.network.chat.MutableComponent;

public record Translator(String translationPath) {
    public MutableComponent tr(String key, Object... args) {
        String translationKey = TranslationConstants.TRANSLATION_KEY_PREFIX + this.translationPath + "." + key;
        return Messenger.tr(translationKey, args);
    }
}
