# Better Combat Attributes

NeoForge mod for Minecraft 1.21 (NeoForge 21.0.x) that adds attribute-driven tweaks for Better Combat.

## Features

- Supports `minecraft:player.entity_interaction_range` with Better Combat melee attacks.
- Adds synced player attributes for Better Combat windup, movement, targeting, sweeping, dual-wield, and two-handed tuning.
- Works with `/attribute`, item `AttributeModifiers`, or any other source of vanilla attribute modifiers.

## Attributes

All `bcattributes:*` entries below are registered by this mod on players. `minecraft:player.entity_interaction_range` is still the vanilla attribute, but this mod makes Better Combat respect it.

| Attribute | Default | Range | Effect |
| --- | ---: | ---: | --- |
| `bcattributes:upswing_multiplier` | `0.5` | `0.2` to `1.0` | Controls Better Combat upswing timing before the hit lands. |
| `bcattributes:movement_speed_while_attacking` | `0.5` | `0.0` to `1.0` | Scales player movement input while attacking. |
| `bcattributes:target_search_range_multiplier` | `2.0` | `0.0` to `1024.0` | Expands Better Combat's initial target search volume. |
| `bcattributes:dual_wielding_main_hand_damage_multiplier` | `1.0` | `0.0` to `1024.0` | Damage multiplier for main-hand hits during dual wielding. |
| `bcattributes:dual_wielding_off_hand_damage_multiplier` | `1.0` | `0.0` to `1024.0` | Damage multiplier for off-hand hits during dual wielding. |
| `bcattributes:dual_wielding_attack_speed_multiplier` | `1.2` | `0.0` to `1024.0` | Effective dual-wield attack speed multiplier. |
| `bcattributes:two_handed_damage_multiplier` | `1.0` | `0.0` to `1024.0` | Damage multiplier for attacks made while wielding a two-handed weapon. |
| `bcattributes:attack_interval_cap` | `2` | `0` to `1024` | Minimum cooldown tick cap used by Better Combat. |
| `bcattributes:reworked_sweeping_extra_target_count` | `4` | `1` to `1024` | Extra-target count used by Better Combat's reworked sweeping penalty logic. |
| `bcattributes:reworked_sweeping_maximum_damage_penalty` | `0.5` | `0.0` to `1.0` | Maximum total damage penalty for reworked sweeping extra targets. |
| `bcattributes:reworked_sweeping_enchant_restores` | `0.5` | `0.0` to `1.0` | Sweeping Edge restoration against the reworked sweeping penalty. |
| `bcattributes:max_sweep_targets` | `0` | `0` to `1024` | Caps extra sweep targets after Better Combat target selection. `0` leaves the target count unchanged. |
| `bcattributes:sweep_range_damage_falloff` | `0.0` | `0.0` to `1.0` | Applies linear sweep damage falloff by distance to each target hitbox. |
| `bcattributes:sweep_angle` | `0.0` | `-360.0` to `360.0` | Additive adjustment to a weapon's Better Combat sweep angle. |
| `minecraft:player.entity_interaction_range` | `3.0` | vanilla-defined | Extends Better Combat melee range through the vanilla interaction range attribute. |

## Notes

- Attributes that mirror a Better Combat config option (`upswing_multiplier`, `movement_speed_while_attacking`, `target_search_range_multiplier`, `dual_wielding_*`, `attack_interval_cap`, and `reworked_sweeping_*`) initialize to that config's value when a player joins, so Better Combat config changes are respected by default.
- The `Default` values in the table above are only fallbacks used if Better Combat's config is unavailable.
- Item `AttributeModifiers` are additive on top of the defaults above. For example, an item modifier of `0` on `bcattributes:attack_interval_cap` leaves the effective value at the default `2`, not `0`.
- `bcattributes:sweep_angle` is additive to the weapon's default Better Combat angle.
- If the effective sweep angle is reduced to `0` or below, extra sweep targets are suppressed.
- `bcattributes:sweep_range_damage_falloff` uses a `0.0` to `1.0` range, where `0.0` disables falloff.
- `bcattributes:target_search_range_multiplier` is not raw reach. It expands the initial candidate search volume and stacks with `minecraft:player.entity_interaction_range`.
- Better Combat 2.4 uses the vanilla `minecraft:sweeping_damage_ratio` attribute instead of an enchant-restores config option, so `bcattributes:reworked_sweeping_enchant_restores` is registered but has no effect on this version.
- For isolated testing, prefer `/attribute @s ... base set ...` so you are setting the final value directly instead of stacking on defaults.

## Example

```mcfunction
/attribute @s bcattributes:target_search_range_multiplier base set 4
/attribute @s bcattributes:sweep_angle base set 70
/attribute @s minecraft:player.entity_interaction_range base set 6
```
