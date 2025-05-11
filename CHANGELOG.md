# Changelog

All notable changes to this project will be documented in this file.

## [1.1.0+1.21.1]

### Added

- Added additional colored Hearthstone items. Innkeeper's will choose a random color to sell.
_All Hearthstones in a player's inventory, and ender chest, share the same cooldown._
- Added ability to cook food in Fireplaces. _Fireplaces accept the same recipes as Smokers._
- Added `hearthstone` item tag.
- Added `igniters` item tag.
- Added `extinguishers` item tag.

### Changed

- Hearthstones now teleport the player to a safe position in front of the bound Fireplace.

### Fixed

- Fixed location of language file.
- Fixed mixin filenames to avoid collisions.

## [1.0.3]

### Changed

- Updated supported Platform version.

## [1.0.2]

### Added

- Added Hearthstone use time and cooldown time config options.

### Changed

- Like Campfires, Fireplaces can now be extinguished with a shovel and reignited with a flint and steel or a fire charge.

## [1.0.1]

### Added

- Added option to enable/disable the Hearthstone trade from Innkeeper Villagers.

### Changed

- Changed Innkeeper Villager trades to accommodate the new Hearthstone option. 
Innkeepers will offer a Bottle of Water or a Cookie if the Hearthstone is disabled as a Novice trade. 
They will always offer an Apple as a Novice trade.
- Changed structures to improve Villager behavior.

### Fixed

- Hearthstones no longer teleports players if the bound Fireplace has been destroyed.

## [1.0.0]

_Initial release for Minecraft 1.21.1_