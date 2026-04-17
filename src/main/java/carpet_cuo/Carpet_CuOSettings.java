package carpet_cuo;

import carpet.api.settings.Rule;
import carpet.api.settings.Validators;

import static carpet.api.settings.RuleCategory.*;

@SuppressWarnings("unused")
public class Carpet_CuOSettings {
    private static final String CuO = "CuO";
    private static final String NOT_VANILLA = "not_vanilla";

    @Rule(
            options = {"-1","0","65535","1000000","2147483647"},
            strict = false,
            categories = {CuO,CREATIVE,NOT_VANILLA}
    )
    public static int maxChainDepth = 1000000;

    //#if MC >= 12100
    @Rule(categories = {CuO,CREATIVE,FEATURE,NOT_VANILLA})
    public static boolean infinitelyVault = false;
    //#endif

    //#if MC <= 260102
    @Rule(categories = {CuO,BUGFIX})
    public static boolean comparatorDupeFix = false;
    //#endif

    @Rule(
            options = {"-1","0","512","1000000","2147483647"},
            strict = false,
            categories = {CuO,CREATIVE,NOT_VANILLA}
    )
    public static int maxUpdateDepth = 512;

    @Rule(categories = {CuO,CREATIVE,FEATURE,NOT_VANILLA})
    public static boolean useLeavesAndMossAsFuel = false;

    @Rule(categories = {CuO,CREATIVE,FEATURE,NOT_VANILLA})
    public static boolean infinitelyTotem = false;

    @Rule(categories = {CuO,NOT_VANILLA,EXPERIMENTAL})
    public static boolean noEnchantmentLevelLimit = false;

    @Rule(categories = {CuO,NOT_VANILLA,EXPERIMENTAL})
    public static boolean noTooExpensive = false;

    //#if MC >= 12109
    //$$ @Rule(categories = {CuO,CREATIVE,FEATURE,BUGFIX})
    //$$ public static boolean copperGolemFix = false;
    //#endif

    //#if MC >= 12101
    @Rule(categories = {CuO,CREATIVE,FEATURE})
    public static boolean blockEntitySwapReintroduced = false;
    //#endif

    @Rule(categories = {CuO,CREATIVE,NOT_VANILLA})
    public static boolean endPortalFrameCanBeMined = false;

    @Rule(categories = {CuO,CREATIVE,NOT_VANILLA})
    public static boolean bedrockCanBeMined = false;

    @Rule(categories = {CuO,EXPERIMENTAL,FEATURE})
    public static boolean piglinTradeInstantly = false;

    @Rule(categories = {CuO,FEATURE,CREATIVE,EXPERIMENTAL})
    public static boolean blockDyeing = false;

    @Rule(categories = {CuO,CREATIVE,FEATURE,EXPERIMENTAL})
    public static boolean rightClickBlockUpdate = false;

    @Rule(categories = {CuO,CREATIVE,EXPERIMENTAL})
    public static boolean instantDispenserAndDropper = false;

    @Rule(categories = {CuO,BUGFIX})
    public static boolean bambooBlockCrashFix = false;

    @Rule(categories = {CuO,CREATIVE,NOT_VANILLA})
    public static boolean pistonBreakingBlockProducesUpdate = false;

    @Rule(categories = {CuO,FEATURE,CREATIVE,EXPERIMENTAL})
    public static boolean entityHighLight = false;
}
