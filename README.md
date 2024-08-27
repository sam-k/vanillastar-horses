<!--suppress ALL -->

<p align="center">
  <img src="src/main/resources/vshorses-icon.png" width="128" alt="Vanilla Star Horses">
</p>

# Vanilla* Horses

Ride horses in your world again!

This mod is a long-needed update to horses that still stays within the spirit of vanilla Minecraft.
It improves horse movement, lets you customize horse armor, adds horseshoes for walking across
rivers, and more.

## <img src=".github/assets/spyglass.png" style="height: 1em" alt="Spyglass"> Overview

Horses are slow compared to endgame modes of transportation like elytras. They should be attractive
early- and midgame, but they’re impractical in face of common terrain features like forests and
rivers. They’re also very outdated—except for breeding tweaks in 2023 ([JE 1.19.4](https://minecraft.wiki/w/Java_Edition_1.19.4)/[BE 1.19.70](https://minecraft.wiki/w/Bedrock_Edition_1.19.70)),
they’ve seen no meaningful improvements in their mechanics since their first introduction in 2013 ([JE 1.6.1](https://minecraft.wiki/w/Java_Edition_1.6.1)).

This mod catches up horses to modern Minecraft and makes them a viable, competitive and pleasant
mode of transportation.

(Bonus: This also makes the Frost Walker enchantment useful instead of a novelty. Applied to
horseshoes, it lets your horse run across water with ease.)

<p align="center">
  <img src=".github/assets/screenshot.png" width="600" alt="Black horse with gold-trimmed netherite armor and horseshoes standing on a frosted river">
</p>

## <img src=".github/assets/writable_book.png" style="height: 1em" alt="Book and Quill"> All Changes

### Horse Movement

- **Remove leaves collision in certain cases** for ridden horses (and other ridden animals),
  especially under trees
  - Leaves lose collision if at your horse’s eye level and above a non-collidable block.
  - This means your horse can navigate forests with ease as well as run on treetops as before.
- **Automatically leash** horses (and other rideable animals) **when automatically dismounting
  underwater**, if leads are in the player’s inventory
- Horses (and donkeys and mules) **move faster in water**—horses in real life are excellent
  swimmers!

### Horse Armor

- Add **netherite horse armor** <img src=".github/assets/netherite_horse_armor.png" style="height: 1em" alt="Netherite horse armor">,
  crafted by upgrading diamond horse armor
- Support **enchanting** with all generic and armor-specific enchantments (e.g., Protection, Thorns)
- Support **trimming** with all armor trims
- Add **durability** according to its material
- Miscellaneous fixes for existing bugs:
  - [`MC-16829`](https://bugs.mojang.com/browse/MC-16829): The horse armor model does not show the
    enchantment glint effect
  - [`MC-275395`](https://bugs.mojang.com/browse/MC-275395): Some model parts of horse armor do not
    have a visible 0.1F cube deformation
  - [`MC-275574`](https://bugs.mojang.com/browse/MC-275574): Rendered horse armor textures are
    asymmetric

### Horseshoe

- Add **horseshoes** <img src=".github/assets/horseshoe.png" style="height: 1em" alt="Horseshoe">,
  crafted with copper ingots
- **Speeds up horses** (and donkeys and mules) when equipped in the new horseshoe slot
- Support **enchanting** with all generic and boots-specific enchantments (Depth Strider, Feather
  Falling, Frost Walker, Soul Speed)
- De-facto fixes for existing bugs:
  - [`MC-268935`](https://bugs.mojang.com/browse/MC-268935): The frost walker enchantment doesn’t
    function on horse armor
  - [`MC-268936`](https://bugs.mojang.com/browse/MC-268936): The soul speed enchantment doesn’t
    function on horse armor

## <img src=".github/assets/filled_map.png" style="height: 1em" alt="Filled Map"> Installation

### Requirements

As a Fabric mod, this mod requires the [Fabric Loader](https://fabricmc.net/). It also needs the
following dependencies:

- <img src="https://cdn.modrinth.com/data/P7dR8mSH/icon.png" style="height: 1em" alt="Fabric API"> [Fabric API](https://modrinth.com/mod/fabric-api)
- <img src="https://cdn.modrinth.com/data/Ha28R6CL/icon.png" style="height: 1em" alt="Fabric Language Kotlin"> [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin)

### Downloads

You can find downloadable versions of the mod in the
[Releases](https://github.com/sam-k/vanillastar-horses/releases) page.
