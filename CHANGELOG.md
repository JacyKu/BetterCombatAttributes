# Changelog
## 0.1.0 Beta NeoForge

### Added
- NeoForge support for Minecraft 1.21.1 with Better Combat 2.4.
- `bcattributes:two_handed_damage_multiplier` to control damage dealt while wielding a two-handed weapon.
### Changed
- Config-backed attributes now initialize their base values from Better Combat's server config on join and respawn, instead of overriding it with a hardcoded copy.
### Notes
- `bcattributes:reworked_sweeping_enchant_restores` is registered but has no effect: Better Combat 2.4 uses the vanilla `minecraft:sweeping_damage_ratio` attribute instead of an enchant-restores config.
