package com.jcyyy_.bcattributes;

import net.bettercombat.BetterCombatMod;
import net.bettercombat.logic.PlayerAttackHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public final class BcAttributesCommon {
    private static final ResourceLocation DUAL_WIELDING_ATTACK_SPEED_DELTA_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath("bcattributes", "dual_wielding_attack_speed_delta");

    private BcAttributesCommon() {
    }

    public static void onPlayerTick(final PlayerTickEvent.Post event) {
        AttributeInstance attackSpeed = event.getEntity().getAttribute(Attributes.ATTACK_SPEED);
        if (attackSpeed == null) {
            return;
        }

        attackSpeed.removeModifier(DUAL_WIELDING_ATTACK_SPEED_DELTA_MODIFIER_ID);
        if (BetterCombatMod.config == null || !PlayerAttackHelper.isDualWielding(event.getEntity())) {
            return;
        }

        double delta = BcAttributes.getDualWieldingAttackSpeedMultiplier(event.getEntity()) - BetterCombatMod.config.dual_wielding_attack_speed_multiplier;
        if (Math.abs(delta) < 1.0E-6D) {
            return;
        }

        attackSpeed.addTransientModifier(new AttributeModifier(
                DUAL_WIELDING_ATTACK_SPEED_DELTA_MODIFIER_ID,
                delta,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        ));
    }
}
