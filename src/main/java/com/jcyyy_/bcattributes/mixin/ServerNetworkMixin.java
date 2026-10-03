package com.jcyyy_.bcattributes.mixin;

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
import net.bettercombat.logic.PlayerAttackHelper;
import net.bettercombat.network.Packets;
import net.bettercombat.network.ServerNetwork;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

@Mixin(value = ServerNetwork.class, remap = false)
public abstract class ServerNetworkMixin {
    @Unique
    private static final ResourceLocation bcattributes$SWEEP_DAMAGE_FALLOFF_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath("bcattributes", "sweep_range_damage_falloff");

    @Redirect(
            method = "lambda$handleAttackRequest$3",
            remap = false,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/bettercombat/logic/PlayerAttackHelper;getDualWieldingAttackDamageMultiplier(Lnet/minecraft/world/entity/player/Player;Lnet/bettercombat/api/AttackHand;)F",
                    remap = false
            )
    )
    private static float bcattributes$applyTwoHandedDamageMultiplier(
            final Player player,
            final AttackHand hand
    ) {
        float damageMultiplier = PlayerAttackHelper.getDualWieldingAttackDamageMultiplier(player, hand);
        if (PlayerAttackHelper.isTwoHandedWielding(player)) {
            damageMultiplier *= BcAttributes.getTwoHandedDamageMultiplier(player);
        }

        return damageMultiplier;
    }

    @Redirect(
            method = "lambda$handleAttackRequest$3",
            remap = false,
            at = @At(
                    value = "FIELD",
                    target = "Lnet/bettercombat/config/ServerConfig;reworked_sweeping_maximum_damage_penalty:F",
                    opcode = Opcodes.GETFIELD,
                    remap = false
            ),
            require = 0
    )
    private static float bcattributes$overrideReworkedSweepingMaximumDamagePenalty(
            final ServerConfig config,
            final ServerPlayer attackingPlayer,
            final WeaponAttributes weaponAttributes,
            final WeaponAttributes.Attack attack,
            final AttackHand hand,
            final ServerLevel level,
            final Packets.C2S_AttackRequest attackRequest,
            final boolean useVanillaPacket,
            final ServerGamePacketListenerImpl packetListener
    ) {
        return BcAttributes.getReworkedSweepingMaximumDamagePenalty(attackingPlayer);
    }

    @Redirect(
            method = "lambda$handleAttackRequest$3",
            remap = false,
            at = @At(
                    value = "FIELD",
                    target = "Lnet/bettercombat/config/ServerConfig;reworked_sweeping_extra_target_count:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 0,
                    remap = false
            ),
            require = 0
    )
    private static int bcattributes$overrideReworkedSweepingExtraTargetCountForPenaltyStep(
            final ServerConfig config,
            final ServerPlayer attackingPlayer,
            final WeaponAttributes weaponAttributes,
            final WeaponAttributes.Attack attack,
            final AttackHand hand,
            final ServerLevel level,
            final Packets.C2S_AttackRequest attackRequest,
            final boolean useVanillaPacket,
            final ServerGamePacketListenerImpl packetListener
    ) {
        return BcAttributes.getReworkedSweepingExtraTargetCount(attackingPlayer);
    }

    @Redirect(
            method = "lambda$handleAttackRequest$3",
            remap = false,
            at = @At(
                    value = "FIELD",
                    target = "Lnet/bettercombat/config/ServerConfig;reworked_sweeping_extra_target_count:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 1,
                    remap = false
            ),
            require = 0
    )
    private static int bcattributes$overrideReworkedSweepingExtraTargetCountForPenaltyCap(
            final ServerConfig config,
            final ServerPlayer attackingPlayer,
            final WeaponAttributes weaponAttributes,
            final WeaponAttributes.Attack attack,
            final AttackHand hand,
            final ServerLevel level,
            final Packets.C2S_AttackRequest attackRequest,
            final boolean useVanillaPacket,
            final ServerGamePacketListenerImpl packetListener
    ) {
        return BcAttributes.getReworkedSweepingExtraTargetCount(attackingPlayer);
    }

    @Redirect(
            method = "lambda$handleAttackRequest$3",
            remap = false,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;attack(Lnet/minecraft/world/entity/Entity;)V",
                    remap = false
            ),
            require = 0
    )
    private static void bcattributes$applySweepRangeDamageFalloff(
            final ServerPlayer attackingPlayer,
            final Entity target,
            final ServerPlayer sourcePlayer,
            final WeaponAttributes weaponAttributes,
            final WeaponAttributes.Attack attack,
            final AttackHand hand,
            final ServerLevel level,
            final Packets.C2S_AttackRequest attackRequest,
            final boolean useVanillaPacket,
            final ServerGamePacketListenerImpl packetListener
    ) {
        bcattributes$doApplySweepRangeDamageFalloff(attackingPlayer, target, weaponAttributes);
    }

    @Unique
    private static void bcattributes$doApplySweepRangeDamageFalloff(
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

        Multimap<Holder<Attribute>, AttributeModifier> modifiers = HashMultimap.create();
        modifiers.put(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(
                        bcattributes$SWEEP_DAMAGE_FALLOFF_MODIFIER_ID,
                        damageMultiplier - 1.0D,
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
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
