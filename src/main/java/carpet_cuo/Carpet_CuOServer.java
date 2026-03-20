package carpet_cuo;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.api.settings.SettingsManager;
import carpet.utils.Translations;

import java.util.Map;

public class Carpet_CuOServer implements CarpetExtension {

    private static final CarpetExtension INSTANCE = new Carpet_CuOServer();
    private SettingsManager CarpetExtension;

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
    public SettingsManager extensionSettingsManager(){
        return CarpetExtension;
    }

    @Override
    public Map<String,String> canHasTranslations(String lang){
        return Translations.getTranslationFromResourcePath("assets/carpet_cuo/lang/%s.json".formatted(lang));
    }
}
