package com.jcyyy_.bcattributes.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.jcyyy_.bcattributes.BcAttributes;

import net.bettercombat.api.WeaponAttributes;
import net.bettercombat.client.collision.TargetFinder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

@Mixin(value = TargetFinder.class, remap = false)
public abstract class TargetFinderMixin {
    @Redirect(
            method = "findAttackTargetResult",
            remap = false,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/bettercombat/client/collision/TargetFinder;getInitialTargets(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;D)Ljava/util/List;",
                    remap = false
            )
    )
    private static List<Entity> bcattributes$expandInitialTargetSearch(
            final Player player,
            final Entity cursorTarget,
            final double attackRange
    ) {
        double modifiedAttackRange = BcAttributes.applyForgeReachToAttackRange(player, attackRange);
        return TargetFinder.getInitialTargets(player, cursorTarget, modifiedAttackRange);
    }

    @Redirect(
            method = "findAttackTargetResult",
            remap = false,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/bettercombat/api/WeaponAttributes$Attack;angle()D",
                    ordinal = 0,
                    remap = false
            )
    )
    private static double bcattributes$overrideSweepAngleForWideSweepCheck(
            final WeaponAttributes.Attack attack,
            final Player player,
            final Entity cursorTarget,
            final WeaponAttributes.Attack currentAttack,
            final double attackRange
    ) {
        return BcAttributes.resolveSweepMaxAngle(player, attack.angle());
    }

    @Redirect(
            method = "findAttackTargetResult",
            remap = false,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/bettercombat/api/WeaponAttributes$Attack;angle()D",
                    ordinal = 1,
                    remap = false
            )
    )
    private static double bcattributes$overrideSweepAngleForRadialFilter(
            final WeaponAttributes.Attack attack,
            final Player player,
            final Entity cursorTarget,
            final WeaponAttributes.Attack currentAttack,
            final double attackRange
    ) {
        return BcAttributes.resolveSweepMaxAngle(player, attack.angle());
    }

    @Inject(method = "findAttackTargetResult", remap = false, at = @At("RETURN"))
    private static void bcattributes$limitSweepTargets(
            final Player player,
            final Entity cursorTarget,
            final WeaponAttributes.Attack attack,
            final double attackRange,
            final CallbackInfoReturnable<TargetFinder.TargetResult> cir
    ) {
        TargetFinder.TargetResult targetResult = cir.getReturnValue();
                targetResult.entities = BcAttributes.limitSweepTargets(player, cursorTarget, targetResult.entities, attack.angle());
    }
}