package com.jcyyy_.bcattributes.mixin;

import java.util.UUID;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.jcyyy_.bcattributes.BcAttributes;

import net.bettercombat.api.AttackHand;
import net.bettercombat.api.WeaponAttributes;
import net.bettercombat.config.ServerConfig;
import net.bettercombat.network.Packets;
import net.bettercombat.network.ServerNetwork;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

@Mixin(value = ServerNetwork.class, remap = false)
public abstract class ServerNetworkMixin {
    @Unique
    private static final UUID bcattributes$SWEEP_DAMAGE_FALLOFF_MODIFIER_ID = UUID.fromString("e6528b6c-dfae-4bbb-81c6-cf55dcfd81c4");

    @Redirect(
            method = "lambda$initializeHandlers$5",
            remap = false,
            at = @At(
                    value = "FIELD",
                    target = "Lnet/bettercombat/config/ServerConfig;reworked_sweeping_enchant_restores:F",
                    opcode = Opcodes.GETFIELD,
                    remap = false
            )
    )
    private static float bcattributes$overrideReworkedSweepingEnchantRestores(
            final ServerConfig config,
            final ServerPlayer attackingPlayer,
            final Packets.C2S_AttackRequest attackRequest,
            final WeaponAttributes weaponAttributes,
            final WeaponAttributes.Attack attack,
            final AttackHand hand,
            final ServerLevel level,
            final boolean useVanillaPacket,
            final ServerGamePacketListenerImpl packetListener
    ) {
        return BcAttributes.getReworkedSweepingEnchantRestores(attackingPlayer);
    }

    @Redirect(
            method = "lambda$initializeHandlers$5",
            remap = false,
            at = @At(
                    value = "FIELD",
                    target = "Lnet/bettercombat/config/ServerConfig;reworked_sweeping_maximum_damage_penalty:F",
                    opcode = Opcodes.GETFIELD,
                    remap = false
            )
    )
    private static float bcattributes$overrideReworkedSweepingMaximumDamagePenalty(
            final ServerConfig config,
            final ServerPlayer attackingPlayer,
            final Packets.C2S_AttackRequest attackRequest,
            final WeaponAttributes weaponAttributes,
            final WeaponAttributes.Attack attack,
            final AttackHand hand,
            final ServerLevel level,
            final boolean useVanillaPacket,
            final ServerGamePacketListenerImpl packetListener
    ) {
        return BcAttributes.getReworkedSweepingMaximumDamagePenalty(attackingPlayer);
    }

    @Redirect(
            method = "lambda$initializeHandlers$5",
            remap = false,
            at = @At(
                    value = "FIELD",
                    target = "Lnet/bettercombat/config/ServerConfig;reworked_sweeping_extra_target_count:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 0,
                    remap = false
            )
    )
    private static int bcattributes$overrideReworkedSweepingExtraTargetCountForPenaltyStep(
            final ServerConfig config,
            final ServerPlayer attackingPlayer,
            final Packets.C2S_AttackRequest attackRequest,
            final WeaponAttributes weaponAttributes,
            final WeaponAttributes.Attack attack,
            final AttackHand hand,
            final ServerLevel level,
            final boolean useVanillaPacket,
            final ServerGamePacketListenerImpl packetListener
    ) {
        return BcAttributes.getReworkedSweepingExtraTargetCount(attackingPlayer);
    }

    @Redirect(
            method = "lambda$initializeHandlers$5",
            remap = false,
            at = @At(
                    value = "FIELD",
                    target = "Lnet/bettercombat/config/ServerConfig;reworked_sweeping_extra_target_count:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 1,
                    remap = false
            )
    )
    private static int bcattributes$overrideReworkedSweepingExtraTargetCountForPenaltyCap(
            final ServerConfig config,
            final ServerPlayer attackingPlayer,
            final Packets.C2S_AttackRequest attackRequest,
            final WeaponAttributes weaponAttributes,
            final WeaponAttributes.Attack attack,
            final AttackHand hand,
            final ServerLevel level,
            final boolean useVanillaPacket,
            final ServerGamePacketListenerImpl packetListener
    ) {
        return BcAttributes.getReworkedSweepingExtraTargetCount(attackingPlayer);
    }

    @Redirect(
            method = "lambda$initializeHandlers$5",
            remap = false,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;attack(Lnet/minecraft/world/entity/Entity;)V",
                    remap = false
            ),
            require = 0
    )
    private static void bcattributes$applySweepRangeDamageFalloffNamed(
            final ServerPlayer attackingPlayer,
            final Entity target,
            final ServerPlayer sourcePlayer,
            final Packets.C2S_AttackRequest attackRequest,
            final WeaponAttributes weaponAttributes,
            final WeaponAttributes.Attack attack,
            final AttackHand hand,
            final ServerLevel level,
            final boolean useVanillaPacket,
            final ServerGamePacketListenerImpl packetListener
    ) {
        bcattributes$applySweepRangeDamageFalloff(attackingPlayer, target, weaponAttributes);
    }

    @Redirect(
            method = "lambda$initializeHandlers$5",
            remap = false,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;m_5706_(Lnet/minecraft/world/entity/Entity;)V",
                    remap = false
            ),
            require = 0
    )
    private static void bcattributes$applySweepRangeDamageFalloffSrg(
            final ServerPlayer attackingPlayer,
            final Entity target,
            final ServerPlayer sourcePlayer,
            final Packets.C2S_AttackRequest attackRequest,
            final WeaponAttributes weaponAttributes,
            final WeaponAttributes.Attack attack,
            final AttackHand hand,
            final ServerLevel level,
            final boolean useVanillaPacket,
            final ServerGamePacketListenerImpl packetListener
    ) {
        bcattributes$applySweepRangeDamageFalloff(attackingPlayer, target, weaponAttributes);
    }

    @Unique
    private static void bcattributes$applySweepRangeDamageFalloff(
            final ServerPlayer attackingPlayer,
            final Entity target,
            final WeaponAttributes weaponAttributes
    ) {
        double attackRange = weaponAttributes != null ? weaponAttributes.attackRange() : 0.0D;
        double damageMultiplier = BcAttributes.getSweepDamageMultiplier(attackingPlayer, target, attackRange);
        if (damageMultiplier >= 0.999999D) {
            attackingPlayer.attack(target);
            return;
        }

        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
        modifiers.put(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(
                        bcattributes$SWEEP_DAMAGE_FALLOFF_MODIFIER_ID,
                        "SWEEP_RANGE_DAMAGE_FALLOFF",
                        damageMultiplier - 1.0D,
                        AttributeModifier.Operation.MULTIPLY_TOTAL
                )
        );

        attackingPlayer.getAttributes().addTransientAttributeModifiers(modifiers);
        try {
            attackingPlayer.attack(target);
        } finally {
            attackingPlayer.getAttributes().removeAttributeModifiers(modifiers);
        }
    }
}