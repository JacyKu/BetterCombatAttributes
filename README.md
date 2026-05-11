# Better Combat Attributes

Forge mod for Minecraft 1.20.1 that adds attribute-driven tweaks for Better Combat.

## Features

- Supports Forge `forge:entity_reach` with Better Combat melee attacks.
- Adds `bcattributes:max_sweep_targets` to limit extra sweep targets.
- Adds `bcattributes:sweep_range_damage_falloff` to reduce sweep damage by distance.
- Adds `bcattributes:sweep_angle` to widen or shrink Better Combat's default sweep angle.

## Notes

- `bcattributes:sweep_angle` is additive to the weapon's default Better Combat angle.
- If the effective sweep angle is reduced to `0` or below, extra sweep targets are suppressed.
- `bcattributes:sweep_range_damage_falloff` uses a `0.0` to `1.0` range.