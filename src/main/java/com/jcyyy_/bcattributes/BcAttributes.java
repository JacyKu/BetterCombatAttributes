package com.jcyyy_.bcattributes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.bettercombat.BetterCombatMod;
import net.bettercombat.api.client.AttackRangeExtensions;
import net.bettercombat.config.ServerConfig;
import net.bettercombat.network.Packets;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(BcAttributes.MOD_ID)
public final class BcAttributes {
    public static final String MOD_ID = "bcattributes";
    private static final double VANILLA_BASE_ENTITY_INTERACTION_RANGE = 3.0D;
    private static final double MIN_EFFECTIVE_SWEEP_ANGLE = 0.0001D;
    private static final double DEFAULT_UPSWING_MULTIPLIER = 0.5D;
    private static final double DEFAULT_MOVEMENT_SPEED_WHILE_ATTACKING = 0.5D;
    private static final double DEFAULT_TARGET_SEARCH_RANGE_MULTIPLIER = 2.0D;
    private static final double DEFAULT_DUAL_WIELDING_MAIN_HAND_DAMAGE_MULTIPLIER = 1.0D;
    private static final double DEFAULT_DUAL_WIELDING_OFF_HAND_DAMAGE_MULTIPLIER = 1.0D;
    private static final double DEFAULT_DUAL_WIELDING_ATTACK_SPEED_MULTIPLIER = 1.2D;
    private static final double DEFAULT_TWO_HANDED_DAMAGE_MULTIPLIER = 1.0D;
    private static final double DEFAULT_ATTACK_INTERVAL_CAP = 2.0D;
    private static final double DEFAULT_REWORKED_SWEEPING_EXTRA_TARGET_COUNT = 4.0D;
    private static final double DEFAULT_REWORKED_SWEEPING_MAXIMUM_DAMAGE_PENALTY = 0.5D;
    private static final double DEFAULT_REWORKED_SWEEPING_ENCHANT_RESTORES = 0.5D;

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(net.minecraft.core.registries.Registries.ATTRIBUTE, MOD_ID);
    public static final DeferredHolder<Attribute, Attribute> UPSWING_MULTIPLIER = ATTRIBUTES.register(
        "upswing_multiplier",
        () -> new RangedAttribute("attribute.name.bcattributes.upswing_multiplier", DEFAULT_UPSWING_MULTIPLIER, 0.2D, 1.0D).setSyncable(true)
    );
    public static final DeferredHolder<Attribute, Attribute> MOVEMENT_SPEED_WHILE_ATTACKING = ATTRIBUTES.register(
        "movement_speed_while_attacking",
        () -> new RangedAttribute("attribute.name.bcattributes.movement_speed_while_attacking", DEFAULT_MOVEMENT_SPEED_WHILE_ATTACKING, 0.0D, 1.0D).setSyncable(true)
    );
    public static final DeferredHolder<Attribute, Attribute> TARGET_SEARCH_RANGE_MULTIPLIER = ATTRIBUTES.register(
        "target_search_range_multiplier",
        () -> new RangedAttribute("attribute.name.bcattributes.target_search_range_multiplier", DEFAULT_TARGET_SEARCH_RANGE_MULTIPLIER, 0.0D, 1024.0D).setSyncable(true)
    );
    public static final DeferredHolder<Attribute, Attribute> DUAL_WIELDING_MAIN_HAND_DAMAGE_MULTIPLIER = ATTRIBUTES.register(
        "dual_wielding_main_hand_damage_multiplier",
        () -> new RangedAttribute("attribute.name.bcattributes.dual_wielding_main_hand_damage_multiplier", DEFAULT_DUAL_WIELDING_MAIN_HAND_DAMAGE_MULTIPLIER, 0.0D, 1024.0D).setSyncable(true)
    );
    public static final DeferredHolder<Attribute, Attribute> DUAL_WIELDING_OFF_HAND_DAMAGE_MULTIPLIER = ATTRIBUTES.register(
        "dual_wielding_off_hand_damage_multiplier",
        () -> new RangedAttribute("attribute.name.bcattributes.dual_wielding_off_hand_damage_multiplier", DEFAULT_DUAL_WIELDING_OFF_HAND_DAMAGE_MULTIPLIER, 0.0D, 1024.0D).setSyncable(true)
    );
    public static final DeferredHolder<Attribute, Attribute> DUAL_WIELDING_ATTACK_SPEED_MULTIPLIER = ATTRIBUTES.register(
        "dual_wielding_attack_speed_multiplier",
        () -> new RangedAttribute("attribute.name.bcattributes.dual_wielding_attack_speed_multiplier", DEFAULT_DUAL_WIELDING_ATTACK_SPEED_MULTIPLIER, 0.0D, 1024.0D).setSyncable(true)
    );
    public static final DeferredHolder<Attribute, Attribute> TWO_HANDED_DAMAGE_MULTIPLIER = ATTRIBUTES.register(
        "two_handed_damage_multiplier",
        () -> new RangedAttribute("attribute.name.bcattributes.two_handed_damage_multiplier", DEFAULT_TWO_HANDED_DAMAGE_MULTIPLIER, 0.0D, 1024.0D).setSyncable(true)
    );
    public static final DeferredHolder<Attribute, Attribute> ATTACK_INTERVAL_CAP = ATTRIBUTES.register(
        "attack_interval_cap",
        () -> new RangedAttribute("attribute.name.bcattributes.attack_interval_cap", DEFAULT_ATTACK_INTERVAL_CAP, 0.0D, 1024.0D).setSyncable(true)
    );
    public static final DeferredHolder<Attribute, Attribute> REWORKED_SWEEPING_EXTRA_TARGET_COUNT = ATTRIBUTES.register(
            "reworked_sweeping_extra_target_count",
            () -> new RangedAttribute("attribute.name.bcattributes.reworked_sweeping_extra_target_count", DEFAULT_REWORKED_SWEEPING_EXTRA_TARGET_COUNT, 1.0D, 1024.0D).setSyncable(true)
    );
    public static final DeferredHolder<Attribute, Attribute> REWORKED_SWEEPING_MAXIMUM_DAMAGE_PENALTY = ATTRIBUTES.register(
            "reworked_sweeping_maximum_damage_penalty",
            () -> new RangedAttribute("attribute.name.bcattributes.reworked_sweeping_maximum_damage_penalty", DEFAULT_REWORKED_SWEEPING_MAXIMUM_DAMAGE_PENALTY, 0.0D, 1.0D).setSyncable(true)
    );
    public static final DeferredHolder<Attribute, Attribute> REWORKED_SWEEPING_ENCHANT_RESTORES = ATTRIBUTES.register(
            "reworked_sweeping_enchant_restores",
            () -> new RangedAttribute("attribute.name.bcattributes.reworked_sweeping_enchant_restores", DEFAULT_REWORKED_SWEEPING_ENCHANT_RESTORES, 0.0D, 1.0D).setSyncable(true)
    );
    public static final DeferredHolder<Attribute, Attribute> MAX_SWEEP_TARGETS = ATTRIBUTES.register(
            "max_sweep_targets",
            () -> new RangedAttribute("attribute.name.bcattributes.max_sweep_targets", 0.0D, 0.0D, 1024.0D).setSyncable(true)
    );
    public static final DeferredHolder<Attribute, Attribute> SWEEP_RANGE_DAMAGE_FALLOFF = ATTRIBUTES.register(
            "sweep_range_damage_falloff",
            () -> new RangedAttribute("attribute.name.bcattributes.sweep_range_damage_falloff", 0.0D, 0.0D, 1.0D).setSyncable(true)
    );
    public static final DeferredHolder<Attribute, Attribute> SWEEP_ANGLE = ATTRIBUTES.register(
            "sweep_angle",
            () -> new RangedAttribute("attribute.name.bcattributes.sweep_angle", 0.0D, -360.0D, 360.0D).setSyncable(true)
    );

    public BcAttributes(IEventBus modEventBus) {
        ATTRIBUTES.register(modEventBus);
        NeoForge.EVENT_BUS.addListener(BcAttributesCommon::onPlayerTick);
        NeoForge.EVENT_BUS.addListener(BcAttributes::onPlayerLoggedIn);
        NeoForge.EVENT_BUS.addListener(BcAttributes::onPlayerClone);
        modEventBus.addListener(this::onCommonSetup);
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::onEntityAttributeModification);
    }

    private void onCommonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> Packets.C2S_AttackRequest.UseVanillaPacket = false);
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            AttackRangeExtensions.register(BcAttributes::entityReachModifier);
            BcAttributesClient.initialize();
        });
    }

    private void onEntityAttributeModification(final EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, UPSWING_MULTIPLIER);
        event.add(EntityType.PLAYER, MOVEMENT_SPEED_WHILE_ATTACKING);
        event.add(EntityType.PLAYER, TARGET_SEARCH_RANGE_MULTIPLIER);
        event.add(EntityType.PLAYER, DUAL_WIELDING_MAIN_HAND_DAMAGE_MULTIPLIER);
        event.add(EntityType.PLAYER, DUAL_WIELDING_OFF_HAND_DAMAGE_MULTIPLIER);
        event.add(EntityType.PLAYER, DUAL_WIELDING_ATTACK_SPEED_MULTIPLIER);
        event.add(EntityType.PLAYER, TWO_HANDED_DAMAGE_MULTIPLIER);
        event.add(EntityType.PLAYER, ATTACK_INTERVAL_CAP);
        event.add(EntityType.PLAYER, REWORKED_SWEEPING_EXTRA_TARGET_COUNT);
        event.add(EntityType.PLAYER, REWORKED_SWEEPING_MAXIMUM_DAMAGE_PENALTY);
        event.add(EntityType.PLAYER, REWORKED_SWEEPING_ENCHANT_RESTORES);
        event.add(EntityType.PLAYER, MAX_SWEEP_TARGETS);
        event.add(EntityType.PLAYER, SWEEP_RANGE_DAMAGE_FALLOFF);
        event.add(EntityType.PLAYER, SWEEP_ANGLE);
    }

    private static void onPlayerLoggedIn(final PlayerEvent.PlayerLoggedInEvent event) {
        applyConfigDefaultBaseValues(event.getEntity());
    }

    private static void onPlayerClone(final PlayerEvent.Clone event) {
        applyConfigDefaultBaseValues(event.getEntity());
    }

    private static void applyConfigDefaultBaseValues(final Player player) {
        ServerConfig config = BetterCombatMod.config;
        if (config == null) {
            return;
        }

        setBaseValueFromConfig(player, UPSWING_MULTIPLIER, DEFAULT_UPSWING_MULTIPLIER, config.upswing_multiplier);
        setBaseValueFromConfig(player, MOVEMENT_SPEED_WHILE_ATTACKING, DEFAULT_MOVEMENT_SPEED_WHILE_ATTACKING, config.movement_speed_while_attacking);
        setBaseValueFromConfig(player, TARGET_SEARCH_RANGE_MULTIPLIER, DEFAULT_TARGET_SEARCH_RANGE_MULTIPLIER, config.target_search_range_multiplier);
        setBaseValueFromConfig(player, DUAL_WIELDING_MAIN_HAND_DAMAGE_MULTIPLIER, DEFAULT_DUAL_WIELDING_MAIN_HAND_DAMAGE_MULTIPLIER, config.dual_wielding_main_hand_damage_multiplier);
        setBaseValueFromConfig(player, DUAL_WIELDING_OFF_HAND_DAMAGE_MULTIPLIER, DEFAULT_DUAL_WIELDING_OFF_HAND_DAMAGE_MULTIPLIER, config.dual_wielding_off_hand_damage_multiplier);
        setBaseValueFromConfig(player, DUAL_WIELDING_ATTACK_SPEED_MULTIPLIER, DEFAULT_DUAL_WIELDING_ATTACK_SPEED_MULTIPLIER, config.dual_wielding_attack_speed_multiplier);
        setBaseValueFromConfig(player, ATTACK_INTERVAL_CAP, DEFAULT_ATTACK_INTERVAL_CAP, config.attack_interval_cap);
        setBaseValueFromConfig(player, REWORKED_SWEEPING_EXTRA_TARGET_COUNT, DEFAULT_REWORKED_SWEEPING_EXTRA_TARGET_COUNT, config.reworked_sweeping_extra_target_count);
        setBaseValueFromConfig(player, REWORKED_SWEEPING_MAXIMUM_DAMAGE_PENALTY, DEFAULT_REWORKED_SWEEPING_MAXIMUM_DAMAGE_PENALTY, config.reworked_sweeping_maximum_damage_penalty);
    }

    private static void setBaseValueFromConfig(
            final Player player,
            final DeferredHolder<Attribute, Attribute> attribute,
            final double registeredDefault,
            final double configValue
    ) {
        var attributeInstance = player.getAttribute(attribute);
        if (attributeInstance == null || attributeInstance.getBaseValue() != registeredDefault || configValue == (float) registeredDefault) {
            return;
        }

        attributeInstance.setBaseValue(configValue);
    }

    private static double entityReachDelta(final double entityReach) {
        return entityReach - VANILLA_BASE_ENTITY_INTERACTION_RANGE;
    }

    private static double getAttributeValue(final Player player, final DeferredHolder<Attribute, Attribute> attribute, final double defaultValue) {
        var attributeInstance = player.getAttribute(attribute);
        return attributeInstance != null ? attributeInstance.getValue() : defaultValue;
    }

    public static double applyEntityReachToAttackRange(final Player player, final double attackRange) {
        var entityReachAttribute = player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE);
        if (entityReachAttribute == null) {
            return attackRange;
        }

        double modifiedAttackRange = attackRange + entityReachDelta(entityReachAttribute.getValue());
        return Math.max(0.0D, modifiedAttackRange);
    }

    public static double resolveSweepMaxAngle(final Player player, final double defaultAngle) {
        double additionalAngle = getAttributeValue(player, SWEEP_ANGLE, 0.0D);
        if (additionalAngle == 0.0D) {
            return defaultAngle;
        }

        double resolvedAngle = defaultAngle + additionalAngle;
        if (resolvedAngle <= 0.0D) {
            return MIN_EFFECTIVE_SWEEP_ANGLE;
        }

        return Mth.clamp(resolvedAngle, MIN_EFFECTIVE_SWEEP_ANGLE, 360.0D);
    }

    public static int getAttackIntervalCap(final Player player) {
        return Math.max(0, Mth.floor(getAttributeValue(player, ATTACK_INTERVAL_CAP, DEFAULT_ATTACK_INTERVAL_CAP)));
    }

    public static float getUpswingMultiplier(final Player player) {
        return (float) Mth.clamp(getAttributeValue(player, UPSWING_MULTIPLIER, DEFAULT_UPSWING_MULTIPLIER), 0.2D, 1.0D);
    }

    public static float getMovementSpeedWhileAttacking(final Player player) {
        return (float) Mth.clamp(getAttributeValue(player, MOVEMENT_SPEED_WHILE_ATTACKING, DEFAULT_MOVEMENT_SPEED_WHILE_ATTACKING), 0.0D, 1.0D);
    }

    public static float getTargetSearchRangeMultiplier(final Player player) {
        return (float) Math.max(0.0D, getAttributeValue(player, TARGET_SEARCH_RANGE_MULTIPLIER, DEFAULT_TARGET_SEARCH_RANGE_MULTIPLIER));
    }

    public static float getDualWieldingMainHandDamageMultiplier(final Player player) {
        return (float) Math.max(0.0D, getAttributeValue(player, DUAL_WIELDING_MAIN_HAND_DAMAGE_MULTIPLIER, DEFAULT_DUAL_WIELDING_MAIN_HAND_DAMAGE_MULTIPLIER));
    }

    public static float getDualWieldingOffHandDamageMultiplier(final Player player) {
        return (float) Math.max(0.0D, getAttributeValue(player, DUAL_WIELDING_OFF_HAND_DAMAGE_MULTIPLIER, DEFAULT_DUAL_WIELDING_OFF_HAND_DAMAGE_MULTIPLIER));
    }

    public static float getDualWieldingAttackSpeedMultiplier(final Player player) {
        return (float) Math.max(0.0D, getAttributeValue(player, DUAL_WIELDING_ATTACK_SPEED_MULTIPLIER, DEFAULT_DUAL_WIELDING_ATTACK_SPEED_MULTIPLIER));
    }

    public static float getTwoHandedDamageMultiplier(final Player player) {
        return (float) Math.max(0.0D, getAttributeValue(player, TWO_HANDED_DAMAGE_MULTIPLIER, DEFAULT_TWO_HANDED_DAMAGE_MULTIPLIER));
    }

    public static List<Entity> limitSweepTargets(
            final Player player,
            final Entity cursorTarget,
            final List<Entity> entities,
            final double defaultAngle
    ) {
        if (getResolvedSweepAngle(player, defaultAngle) <= 0.0D) {
            if (cursorTarget != null && entities.contains(cursorTarget)) {
                return List.of(cursorTarget);
            }

            return List.of();
        }

        int maxSweepTargets = (int) Math.floor(getAttributeValue(player, MAX_SWEEP_TARGETS, 0.0D));
        if (maxSweepTargets <= 0) {
            return entities;
        }

        Entity preservedTarget = cursorTarget != null && entities.contains(cursorTarget) ? cursorTarget : null;
        List<Entity> extraTargets = new ArrayList<>(entities);
        if (preservedTarget != null) {
            extraTargets.remove(preservedTarget);
        }

        extraTargets.sort(Comparator.comparingDouble(entity -> player.distanceToSqr(entity)));
        if (extraTargets.size() > maxSweepTargets) {
            extraTargets.subList(maxSweepTargets, extraTargets.size()).clear();
        }

        if (preservedTarget == null) {
            return extraTargets;
        }

        List<Entity> limitedTargets = new ArrayList<>(extraTargets.size() + 1);
        limitedTargets.add(preservedTarget);
        limitedTargets.addAll(extraTargets);
        return limitedTargets;
    }

    private static double getResolvedSweepAngle(final Player player, final double defaultAngle) {
        return defaultAngle + getAttributeValue(player, SWEEP_ANGLE, 0.0D);
    }

    public static int getReworkedSweepingExtraTargetCount(final Player player) {
        return Math.max(1, Mth.floor(getAttributeValue(player, REWORKED_SWEEPING_EXTRA_TARGET_COUNT, DEFAULT_REWORKED_SWEEPING_EXTRA_TARGET_COUNT)));
    }

    public static float getReworkedSweepingMaximumDamagePenalty(final Player player) {
        return (float) Mth.clamp(
                getAttributeValue(player, REWORKED_SWEEPING_MAXIMUM_DAMAGE_PENALTY, DEFAULT_REWORKED_SWEEPING_MAXIMUM_DAMAGE_PENALTY),
                0.0D,
                1.0D
        );
    }

    public static float getReworkedSweepingEnchantRestores(final Player player) {
        return (float) Mth.clamp(
                getAttributeValue(player, REWORKED_SWEEPING_ENCHANT_RESTORES, DEFAULT_REWORKED_SWEEPING_ENCHANT_RESTORES),
                0.0D,
                1.0D
        );
    }

    public static double getSweepDamageMultiplier(final Player player, final Entity target, final double attackRange) {
        double falloff = getAttributeValue(player, SWEEP_RANGE_DAMAGE_FALLOFF, 0.0D);
        if (falloff <= 0.0D) {
            return 1.0D;
        }

        double effectiveAttackRange = applyEntityReachToAttackRange(player, attackRange);
        if (effectiveAttackRange <= 0.0D) {
            return 1.0D;
        }

        double distanceRatio = Mth.clamp(distanceToTargetHitbox(player, target) / effectiveAttackRange, 0.0D, 1.0D);
        return Math.max(0.0D, 1.0D - (distanceRatio * falloff));
    }

    private static double distanceToTargetHitbox(final Player player, final Entity target) {
        Vec3 attackOrigin = player.getEyePosition();
        AABB targetBounds = target.getBoundingBox();
        double closestX = Mth.clamp(attackOrigin.x, targetBounds.minX, targetBounds.maxX);
        double closestY = Mth.clamp(attackOrigin.y, targetBounds.minY, targetBounds.maxY);
        double closestZ = Mth.clamp(attackOrigin.z, targetBounds.minZ, targetBounds.maxZ);
        return attackOrigin.distanceTo(new Vec3(closestX, closestY, closestZ));
    }

    private static AttackRangeExtensions.Modifier entityReachModifier(final AttackRangeExtensions.Context context) {
        double modifiedAttackRange = applyEntityReachToAttackRange(context.player(), context.attackRange());
        double reachDelta = modifiedAttackRange - context.attackRange();
        return new AttackRangeExtensions.Modifier(reachDelta, AttackRangeExtensions.Operation.ADD);
    }
}
