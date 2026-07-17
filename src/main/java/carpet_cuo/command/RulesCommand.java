package carpet_cuo.command;

import carpet.CarpetServer;
import carpet.api.settings.CarpetRule;
import carpet.api.settings.InvalidRuleValueException;
import carpet.api.settings.SettingsManager;
import carpet_cuo.utils.Messenger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import static carpet_cuo.Carpet_CuOServer.LOGGER;

public class RulesCommand {
    private static final RulesCommand INSTANCE = new RulesCommand();

    public static RulesCommand getInstance() {
        return INSTANCE;
    }

    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("rules")
                        .then(Commands.literal("close")
                                .executes((CommandContext<CommandSourceStack> ctx) -> execute(ctx, false, false))
                        )
                        .then(Commands.literal("enabled")
                                .executes((CommandContext<CommandSourceStack> ctx) -> execute(ctx, true, false))
                        )
                        .then(Commands.literal("def")
                                .executes((CommandContext<CommandSourceStack> ctx) -> execute(ctx, false, true))
                        )
        );
    }

    private int execute(CommandContext<CommandSourceStack> ctx, boolean hasArgs, boolean def) {
        CommandSourceStack source = ctx.getSource();
        SettingsManager settingsManager = CarpetServer.settingsManager;
        boolean bl = false;
        String value = "";
        for (CarpetRule<?> rule : settingsManager.getCarpetRules()) {
            try {
                if (def) {
                    value = rule.defaultValue().toString();
                    rule.set(source, rule.defaultValue().toString());
                    bl = true;
                }else if (rule.value() instanceof Boolean) {
                    value = Boolean.toString(hasArgs);
                    rule.set(source, Boolean.toString(hasArgs));
                    bl = true;
                }
            } catch (InvalidRuleValueException e) {
                LOGGER.error("Failure, unable to set value {}", value);
            }
        }
        if (bl) {
            source.sendSuccess(() -> Messenger.tr("carpet.command.rules.value"), true);
            return 1;
        }
        source.sendSuccess(() -> Messenger.f(Messenger.tr("carpet.command.rules.fail"), ChatFormatting.RED), true);
        return 0;
    }
}
