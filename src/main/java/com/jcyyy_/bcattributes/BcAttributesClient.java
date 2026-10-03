package com.jcyyy_.bcattributes;

import net.bettercombat.BetterCombatMod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.client.event.ClientTickEvent;

public final class BcAttributesClient {
    private BcAttributesClient() {
    }

    public static void initialize() {
        NeoForge.EVENT_BUS.addListener(BcAttributesClient::onClientTick);
    }

    private static void onClientTick(final ClientTickEvent.Post event) {
        if (BetterCombatMod.config == null) {
            return;
        }

        var minecraft = net.minecraft.client.Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }

        BetterCombatMod.config.upswing_multiplier = BcAttributes.getUpswingMultiplier(minecraft.player);
        BetterCombatMod.config.movement_speed_while_attacking = BcAttributes.getMovementSpeedWhileAttacking(minecraft.player);
    }
}
