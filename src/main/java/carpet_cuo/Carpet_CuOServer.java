package carpet_cuo;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.utils.Translations;
import carpet_cuo.Logging.CuOAdditionLoggerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Carpet_CuOServer implements CarpetExtension {

    private static final CarpetExtension INSTANCE = new Carpet_CuOServer();
    public static final Logger LOGGER = LoggerFactory.getLogger(Carpet_CuOMod.MOD_ID);

    public static void init(){
        CarpetServer.manageExtension(INSTANCE);
    }

    @Override
    public void onGameStarted(){
        CarpetServer.settingsManager.parseSettingsClass(Carpet_CuOSettings.class);
    }

    @Override
    public String version(){
        return Carpet_CuOMod.version;
    }

    @Override
    public Map<String,String> canHasTranslations(String lang){
        return Translations.getTranslationFromResourcePath("assets/carpet_cuo/lang/%s.json".formatted(lang));
    }

    @Override
    public void registerLoggers() {
        CuOAdditionLoggerRegistry.registerLoggers();
    }
}
