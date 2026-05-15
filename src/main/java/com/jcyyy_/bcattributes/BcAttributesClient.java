package com.jcyyy_.bcattributes;

import net.bettercombat.BetterCombat;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;

public final class BcAttributesClient {
    private BcAttributesClient() {
    }

    public static void initialize() {
        MinecraftForge.EVENT_BUS.addListener(BcAttributesClient::onClientTick);
    }

    private static void onClientTick(final TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        if (BetterCombat.config == null || event.side.isServer()) {
            return;
        }

        var minecraft = net.minecraft.client.Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }

        BetterCombat.config.upswing_multiplier = BcAttributes.getUpswingMultiplier(minecraft.player);
        BetterCombat.config.movement_speed_while_attacking = BcAttributes.getMovementSpeedWhileAttacking(minecraft.player);
    }
}