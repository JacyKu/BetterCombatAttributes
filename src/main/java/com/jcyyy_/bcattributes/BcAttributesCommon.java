package com.jcyyy_.bcattributes;

import java.util.UUID;

import net.bettercombat.BetterCombat;
import net.bettercombat.logic.PlayerAttackHelper;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.TickEvent;

public final class BcAttributesCommon {
    private static final UUID DUAL_WIELDING_ATTACK_SPEED_DELTA_MODIFIER_ID = UUID.fromString("5dfaa88d-094b-4304-a62d-9d0e81b6c864");

    private BcAttributesCommon() {
    }

    public static void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        AttributeInstance attackSpeed = event.player.getAttribute(Attributes.ATTACK_SPEED);
        if (attackSpeed == null) {
            return;
        }

        attackSpeed.removeModifier(DUAL_WIELDING_ATTACK_SPEED_DELTA_MODIFIER_ID);
        if (BetterCombat.config == null || !PlayerAttackHelper.isDualWielding(event.player)) {
            return;
        }

        double delta = BcAttributes.getDualWieldingAttackSpeedMultiplier(event.player) - BetterCombat.config.dual_wielding_attack_speed_multiplier;
        if (Math.abs(delta) < 1.0E-6D) {
            return;
        }

        attackSpeed.addTransientModifier(new AttributeModifier(
                DUAL_WIELDING_ATTACK_SPEED_DELTA_MODIFIER_ID,
                "BCAttributes dual wielding attack speed delta",
                delta,
                AttributeModifier.Operation.MULTIPLY_BASE
        ));
    }
}