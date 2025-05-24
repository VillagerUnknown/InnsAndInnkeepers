# VillagerUnknown's Inns & Innkeepers

Inspired by MMORPG's, this mod adds Inns and Innkeeper Villagers to world generation. 
Includes optional Hearthstone items to allow players to teleport to a bound Fireplace.

## Inns

Inns with Fireplaces, and Innkeepers, can be found throughout the world.

## Innkeeper Villagers

Innkeeper Villagers use Fireplaces as workstations. 
In addition to offering food and potions, the Innkeeper Villager optionally sells Hearthstones that allows players to teleport to a bound Fireplace. 

## Fireplace

The Fireplace, the Innkeeper's workstation, is lit when placed, 
can be extinguished with a shovel, can be reignited with flint and steel or a fire charge, 
and allows players to set their Hearthstone's teleport location. 
The smoke from Fireplaces go through blocks above allowing players to build functional chimneys!
Fireplaces can optionally be used to cook food. _This can be disabled in the config if you don't want an infinite fuel source cooking mechanic._

## Hearthstone

The Hearthstone is a non-stackable item that can be bound to a Fireplace. 
When a Hearthstone is used by a player it will teleport the player back to the bound Fireplace. 
Hearthstones have a configurable cooldown, cannot be crafted, and can only be purchased from an Innkeeper.
Hearthstones also come in multiple colors but all share the same cooldown.
The Hearthstone can be enabled in the config.

## Options

* enableFireplaceCooking - Allows players to use the Fireplace to cook food when lit. (Default: true)
* maxFireplaceSmokeThroughBlocks - Maximum number of blocks smoke from Fireplaces can go through blocks. (Default: 16)
* chanceForSmokeVariation - Chance for different types of smoke to appear from the Fireplaces chimney.
* enableHearthstoneTrade - Enables Hearthstone trade chance for Novice Innkeepers. (Default: true)
* enableGoldenAppleTrade - Enables Golden Apple trade chance for Master Innkeepers. (Default: false)
* enableEnchantedGoldenAppleTrade - Enables Enchanted Golden Apple trade chance for Master Innkeepers. (Default: false)
* hearthstoneUseTime - Time it takes to teleport from first using the Hearthstone. (Default: 80)
* hearthstoneCooldownTime - Time it takes to use a Hearthstone again after using a Hearthstone. (Default: 1000)
* hearthstoneSafeTeleportRange - Range from a Fireplace to search for a safe teleport location. (Default: 2)

## Support

* Request features and report bugs at https://github.com/VillagerUnknown/InnsAndInnkeepers/issues
* View the changelog at https://github.com/VillagerUnknown/InnsAndInnkeepers/blob/main/CHANGELOG.md