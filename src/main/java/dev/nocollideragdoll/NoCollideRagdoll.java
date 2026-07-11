package dev.nocollideragdoll;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(NoCollideRagdoll.MOD_ID)
public class NoCollideRagdoll {
    public static final String MOD_ID = "nocollide_ragdoll";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static boolean noCollideEnabled = true;

    // ThreadLocal flag: true while ANY Entity.move() is executing (player/mob movement).
    // Physics engine queries happen outside Entity.move(), so this reliably distinguishes them.
    public static final ThreadLocal<Boolean> INSIDE_ENTITY_MOVE = ThreadLocal.withInitial(() -> false);

    public NoCollideRagdoll() {
        NeoForge.EVENT_BUS.register(this);
    }

    public static boolean isNoCollideEnabled() {
        return noCollideEnabled;
    }

    public static boolean toggleNoCollide() {
        noCollideEnabled = !noCollideEnabled;
        return noCollideEnabled;
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("nocollide")
            .requires(source -> source.hasPermission(2))
            .executes(this::executeNoCollide)
        );
    }

    private int executeNoCollide(CommandContext<CommandSourceStack> context) {
        boolean nowEnabled = toggleNoCollide();
        String status = nowEnabled ? "enabled" : "disabled";
        context.getSource().sendSuccess(
            () -> Component.literal("No-Collide mode for Sable Ragdolls is now " + status + "."),
            true
        );
        return 1;
    }
}