package com.jcyyy_.bcattributes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.bettercombat.api.client.AttackRangeExtensions;
import net.bettercombat.network.Packets;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(BcAttributes.MOD_ID)
public final class BcAttributes {
    public static final String MOD_ID = "bcattributes";
    private static final double VANILLA_BASE_ENTITY_REACH = 3.0D;
    private static final double MIN_EFFECTIVE_SWEEP_ANGLE = 0.0001D;
    private static final double DEFAULT_UPSWING_MULTIPLIER = 0.5D;
    private static final double DEFAULT_MOVEMENT_SPEED_WHILE_ATTACKING = 0.5D;
    private static final double DEFAULT_TARGET_SEARCH_RANGE_MULTIPLIER = 2.0D;
    private static final double DEFAULT_DUAL_WIELDING_MAIN_HAND_DAMAGE_MULTIPLIER = 1.0D;
    private static final double DEFAULT_DUAL_WIELDING_OFF_HAND_DAMAGE_MULTIPLIER = 1.0D;
    private static final double DEFAULT_DUAL_WIELDING_ATTACK_SPEED_MULTIPLIER = 1.2D;
    private static final double DEFAULT_ATTACK_INTERVAL_CAP = 2.0D;
    private static final double DEFAULT_REWORKED_SWEEPING_EXTRA_TARGET_COUNT = 4.0D;
    private static final double DEFAULT_REWORKED_SWEEPING_MAXIMUM_DAMAGE_PENALTY = 0.5D;
    private static final double DEFAULT_REWORKED_SWEEPING_ENCHANT_RESTORES = 0.5D;

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, MOD_ID);
    public static final RegistryObject<Attribute> UPSWING_MULTIPLIER = ATTRIBUTES.register(
        "upswing_multiplier",
        () -> new RangedAttribute("attribute.name.bcattributes.upswing_multiplier", DEFAULT_UPSWING_MULTIPLIER, 0.2D, 1.0D).setSyncable(true)
    );
    public static final RegistryObject<Attribute> MOVEMENT_SPEED_WHILE_ATTACKING = ATTRIBUTES.register(
        "movement_speed_while_attacking",
        () -> new RangedAttribute("attribute.name.bcattributes.movement_speed_while_attacking", DEFAULT_MOVEMENT_SPEED_WHILE_ATTACKING, 0.0D, 1.0D).setSyncable(true)
    );
    public static final RegistryObject<Attribute> TARGET_SEARCH_RANGE_MULTIPLIER = ATTRIBUTES.register(
        "target_search_range_multiplier",
        () -> new RangedAttribute("attribute.name.bcattributes.target_search_range_multiplier", DEFAULT_TARGET_SEARCH_RANGE_MULTIPLIER, 0.0D, 1024.0D).setSyncable(true)
    );
    public static final RegistryObject<Attribute> DUAL_WIELDING_MAIN_HAND_DAMAGE_MULTIPLIER = ATTRIBUTES.register(
        "dual_wielding_main_hand_damage_multiplier",
        () -> new RangedAttribute("attribute.name.bcattributes.dual_wielding_main_hand_damage_multiplier", DEFAULT_DUAL_WIELDING_MAIN_HAND_DAMAGE_MULTIPLIER, 0.0D, 1024.0D).setSyncable(true)
    );
    public static final RegistryObject<Attribute> DUAL_WIELDING_OFF_HAND_DAMAGE_MULTIPLIER = ATTRIBUTES.register(
        "dual_wielding_off_hand_damage_multiplier",
        () -> new RangedAttribute("attribute.name.bcattributes.dual_wielding_off_hand_damage_multiplier", DEFAULT_DUAL_WIELDING_OFF_HAND_DAMAGE_MULTIPLIER, 0.0D, 1024.0D).setSyncable(true)
    );
    public static final RegistryObject<Attribute> DUAL_WIELDING_ATTACK_SPEED_MULTIPLIER = ATTRIBUTES.register(
        "dual_wielding_attack_speed_multiplier",
        () -> new RangedAttribute("attribute.name.bcattributes.dual_wielding_attack_speed_multiplier", DEFAULT_DUAL_WIELDING_ATTACK_SPEED_MULTIPLIER, 0.0D, 1024.0D).setSyncable(true)
    );
    public static final RegistryObject<Attribute> ATTACK_INTERVAL_CAP = ATTRIBUTES.register(
        "attack_interval_cap",
        () -> new RangedAttribute("attribute.name.bcattributes.attack_interval_cap", DEFAULT_ATTACK_INTERVAL_CAP, 0.0D, 1024.0D).setSyncable(true)
    );
    public static final RegistryObject<Attribute> REWORKED_SWEEPING_EXTRA_TARGET_COUNT = ATTRIBUTES.register(
            "reworked_sweeping_extra_target_count",
            () -> new RangedAttribute("attribute.name.bcattributes.reworked_sweeping_extra_target_count", DEFAULT_REWORKED_SWEEPING_EXTRA_TARGET_COUNT, 1.0D, 1024.0D).setSyncable(true)
    );
    public static final RegistryObject<Attribute> REWORKED_SWEEPING_MAXIMUM_DAMAGE_PENALTY = ATTRIBUTES.register(
            "reworked_sweeping_maximum_damage_penalty",
            () -> new RangedAttribute("attribute.name.bcattributes.reworked_sweeping_maximum_damage_penalty", DEFAULT_REWORKED_SWEEPING_MAXIMUM_DAMAGE_PENALTY, 0.0D, 1.0D).setSyncable(true)
    );
    public static final RegistryObject<Attribute> REWORKED_SWEEPING_ENCHANT_RESTORES = ATTRIBUTES.register(
            "reworked_sweeping_enchant_restores",
            () -> new RangedAttribute("attribute.name.bcattributes.reworked_sweeping_enchant_restores", DEFAULT_REWORKED_SWEEPING_ENCHANT_RESTORES, 0.0D, 1.0D).setSyncable(true)
    );
    public static final RegistryObject<Attribute> MAX_SWEEP_TARGETS = ATTRIBUTES.register(
            "max_sweep_targets",
            () -> new RangedAttribute("attribute.name.bcattributes.max_sweep_targets", 0.0D, 0.0D, 1024.0D).setSyncable(true)
    );
    public static final RegistryObject<Attribute> SWEEP_RANGE_DAMAGE_FALLOFF = ATTRIBUTES.register(
            "sweep_range_damage_falloff",
            () -> new RangedAttribute("attribute.name.bcattributes.sweep_range_damage_falloff", 0.0D, 0.0D, 1.0D).setSyncable(true)
    );
    public static final RegistryObject<Attribute> SWEEP_ANGLE = ATTRIBUTES.register(
            "sweep_angle",
            () -> new RangedAttribute("attribute.name.bcattributes.sweep_angle", 0.0D, -360.0D, 360.0D).setSyncable(true)
    );

    public BcAttributes() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ATTRIBUTES.register(modEventBus);
        MinecraftForge.EVENT_BUS.addListener(BcAttributesCommon::onPlayerTick);
        modEventBus.addListener(this::onCommonSetup);
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::onEntityAttributeModification);
    }

    private void onCommonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> Packets.C2S_AttackRequest.UseVanillaPacket = false);
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            AttackRangeExtensions.register(BcAttributes::forgeReachModifier);
            BcAttributesClient.initialize();
        });
    }

    private void onEntityAttributeModification(final EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, UPSWING_MULTIPLIER.get());
        event.add(EntityType.PLAYER, MOVEMENT_SPEED_WHILE_ATTACKING.get());
        event.add(EntityType.PLAYER, TARGET_SEARCH_RANGE_MULTIPLIER.get());
        event.add(EntityType.PLAYER, DUAL_WIELDING_MAIN_HAND_DAMAGE_MULTIPLIER.get());
        event.add(EntityType.PLAYER, DUAL_WIELDING_OFF_HAND_DAMAGE_MULTIPLIER.get());
        event.add(EntityType.PLAYER, DUAL_WIELDING_ATTACK_SPEED_MULTIPLIER.get());
        event.add(EntityType.PLAYER, ATTACK_INTERVAL_CAP.get());
        event.add(EntityType.PLAYER, REWORKED_SWEEPING_EXTRA_TARGET_COUNT.get());
        event.add(EntityType.PLAYER, REWORKED_SWEEPING_MAXIMUM_DAMAGE_PENALTY.get());
        event.add(EntityType.PLAYER, REWORKED_SWEEPING_ENCHANT_RESTORES.get());
        event.add(EntityType.PLAYER, MAX_SWEEP_TARGETS.get());
        event.add(EntityType.PLAYER, SWEEP_RANGE_DAMAGE_FALLOFF.get());
        event.add(EntityType.PLAYER, SWEEP_ANGLE.get());
    }

    private static double forgeReachDelta(final double entityReach) {
        return entityReach - VANILLA_BASE_ENTITY_REACH;
    }

    private static double getAttributeValue(final Player player, final RegistryObject<Attribute> attribute, final double defaultValue) {
        var attributeInstance = player.getAttribute(attribute.get());
        return attributeInstance != null ? attributeInstance.getValue() : defaultValue;
    }

    public static double applyForgeReachToAttackRange(final Player player, final double attackRange) {
        var entityReachAttribute = player.getAttribute(ForgeMod.ENTITY_REACH.get());
        if (entityReachAttribute == null) {
            return attackRange;
        }

        double modifiedAttackRange = attackRange + forgeReachDelta(entityReachAttribute.getValue());
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

        double effectiveAttackRange = applyForgeReachToAttackRange(player, attackRange);
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

    private static AttackRangeExtensions.Modifier forgeReachModifier(final AttackRangeExtensions.Context context) {
        double modifiedAttackRange = applyForgeReachToAttackRange(context.player(), context.attackRange());
        double reachDelta = modifiedAttackRange - context.attackRange();
        return new AttackRangeExtensions.Modifier(reachDelta, AttackRangeExtensions.Operation.ADD);
    }
}