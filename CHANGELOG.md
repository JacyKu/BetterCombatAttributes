# Changelog
## 0.2.0
### Added
- `bcattributes:upswing_multiplier` to control Better Combat upswing timing.
- `bcattributes:movement_speed_while_attacking` to scale movement while attacking.
- `bcattributes:target_search_range_multiplier` to expand Better Combat target acquisition range.
- `bcattributes:dual_wielding_main_hand_damage_multiplier` to control dual-wield main-hand damage.
- `bcattributes:dual_wielding_off_hand_damage_multiplier` to control dual-wield off-hand damage.
- `bcattributes:dual_wielding_attack_speed_multiplier` to control dual-wield attack speed.
- `bcattributes:attack_interval_cap` to control Better Combat's attack cooldown cap.
- `bcattributes:reworked_sweeping_extra_target_count` to control reworked sweeping extra-target scaling.
- `bcattributes:reworked_sweeping_maximum_damage_penalty` to control the maximum reworked sweeping damage penalty.
- `bcattributes:reworked_sweeping_enchant_restores` to control how much Sweeping Edge restores that penalty.

---
## 0.1.1
### Fixed
- Updated code that would only run in the development environment **(always test in production before release)**.

---
## 0.1.0
- Initial commit.
### Added
- Support for Forge `forge:entity_reach` with Better Combat melee attacks.
- `bcattributes:max_sweep_targets` to limit extra sweep targets.
- `bcattributes:sweep_range_damage_falloff` to reduce sweep damage by distance.
- `bcattributes:sweep_angle` to widen or shrink Better Combat's default sweep angle.