# Better Combat Attributes

Forge mod for Minecraft 1.20.1 that adds attribute-driven tweaks for Better Combat.

## Features

- Supports Forge `forge:entity_reach` with Better Combat melee attacks.
- Adds synced player attributes for Better Combat windup, movement, targeting, sweeping, and dual-wield tuning.
- Works with `/attribute`, item `AttributeModifiers`, or any other source of vanilla attribute modifiers.

## Attributes

All `bcattributes:*` entries below are registered by this mod on players. `forge:entity_reach` is still provided by Forge, but this mod makes Better Combat respect it.

| Attribute | Default | Range | Effect |
| --- | ---: | ---: | --- |
| `bcattributes:upswing_multiplier` | `0.5` | `0.2` to `1.0` | Controls Better Combat upswing timing before the hit lands. |
| `bcattributes:movement_speed_while_attacking` | `0.5` | `0.0` to `1.0` | Scales player movement input while attacking. |
| `bcattributes:target_search_range_multiplier` | `2.0` | `0.0` to `1024.0` | Expands Better Combat's initial target search volume. |
| `bcattributes:dual_wielding_main_hand_damage_multiplier` | `1.0` | `0.0` to `1024.0` | Damage multiplier for main-hand hits during dual wielding. |
| `bcattributes:dual_wielding_off_hand_damage_multiplier` | `1.0` | `0.0` to `1024.0` | Damage multiplier for off-hand hits during dual wielding. |
| `bcattributes:dual_wielding_attack_speed_multiplier` | `1.2` | `0.0` to `1024.0` | Effective dual-wield attack speed multiplier. |
| `bcattributes:attack_interval_cap` | `2` | `0` to `1024` | Minimum cooldown tick cap used by Better Combat. |
| `bcattributes:reworked_sweeping_extra_target_count` | `4` | `1` to `1024` | Extra-target count used by Better Combat's reworked sweeping penalty logic. |
| `bcattributes:reworked_sweeping_maximum_damage_penalty` | `0.5` | `0.0` to `1.0` | Maximum total damage penalty for reworked sweeping extra targets. |
| `bcattributes:reworked_sweeping_enchant_restores` | `0.5` | `0.0` to `1.0` | Sweeping Edge restoration against the reworked sweeping penalty. |
| `bcattributes:max_sweep_targets` | `0` | `0` to `1024` | Caps extra sweep targets after Better Combat target selection. `0` leaves the target count unchanged. |
| `bcattributes:sweep_range_damage_falloff` | `0.0` | `0.0` to `1.0` | Applies linear sweep damage falloff by distance to each target hitbox. |
| `bcattributes:sweep_angle` | `0.0` | `-360.0` to `360.0` | Additive adjustment to a weapon's Better Combat sweep angle. |
| `forge:entity_reach` | Forge default | Forge-defined | Extends Better Combat melee range through Forge's reach attribute. |

## Notes

- Item `AttributeModifiers` are additive on top of the defaults above. For example, an item modifier of `0` on `bcattributes:attack_interval_cap` leaves the effective value at the default `2`, not `0`.
- `bcattributes:sweep_angle` is additive to the weapon's default Better Combat angle.
- If the effective sweep angle is reduced to `0` or below, extra sweep targets are suppressed.
- `bcattributes:sweep_range_damage_falloff` uses a `0.0` to `1.0` range, where `0.0` disables falloff.
- `bcattributes:target_search_range_multiplier` is not raw reach. It expands the initial candidate search volume and stacks with `forge:entity_reach`.
- For isolated testing, prefer `/attribute @s ... base set ...` so you are setting the final value directly instead of stacking on defaults.

## Example

```mcfunction
/attribute @s bcattributes:target_search_range_multiplier base set 4
/attribute @s bcattributes:sweep_angle base set 70
/attribute @s forge:entity_reach base set 6
```
