package com.jcyyy_.bcattributes.mixin;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.jcyyy_.bcattributes.BcAttributes;

import net.bettercombat.api.AttackHand;
import net.bettercombat.config.ServerConfig;
import net.bettercombat.logic.PlayerAttackHelper;
import net.minecraft.world.entity.player.Player;

@Mixin(value = PlayerAttackHelper.class, remap = false)
public abstract class PlayerAttackHelperMixin {
    @Redirect(
            method = "getDualWieldingAttackDamageMultiplier",
            remap = false,
            at = @At(
                    value = "FIELD",
                    target = "Lnet/bettercombat/config/ServerConfig;dual_wielding_off_hand_damage_multiplier:F",
                    opcode = Opcodes.GETFIELD,
                    remap = false
            )
    )
    private static float bcattributes$overrideDualWieldingOffHandDamageMultiplier(
            final ServerConfig config,
            final Player player,
            final AttackHand hand
    ) {
        return BcAttributes.getDualWieldingOffHandDamageMultiplier(player);
    }

    @Redirect(
            method = "getDualWieldingAttackDamageMultiplier",
            remap = false,
            at = @At(
                    value = "FIELD",
                    target = "Lnet/bettercombat/config/ServerConfig;dual_wielding_main_hand_damage_multiplier:F",
                    opcode = Opcodes.GETFIELD,
                    remap = false
            )
    )
    private static float bcattributes$overrideDualWieldingMainHandDamageMultiplier(
            final ServerConfig config,
            final Player player,
            final AttackHand hand
    ) {
        return BcAttributes.getDualWieldingMainHandDamageMultiplier(player);
    }

    @Redirect(
            method = "getAttackCooldownTicksCapped",
            remap = false,
            at = @At(
                    value = "FIELD",
                    target = "Lnet/bettercombat/config/ServerConfig;attack_interval_cap:I",
                    opcode = Opcodes.GETFIELD,
                    remap = false
            )
    )
    private static int bcattributes$overrideAttackIntervalCap(
            final ServerConfig config,
            final Player player
    ) {
        return BcAttributes.getAttackIntervalCap(player);
    }
}