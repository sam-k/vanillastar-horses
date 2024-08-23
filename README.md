<!--suppress ALL -->

<p align="center">
  <img src="src/main/resources/vshorses-icon.png" width="128" alt="Vanilla Star Horses">
</p>

# Vanilla* Horses

Ride horses in your world again!

This mod is a long-needed update to horses that still stays within the spirit of vanilla Minecraft.
It improves horse movement, lets you customize horse armor, adds horseshoes for walking across
rivers, and more.

## <img src=".github/assets/spyglass.png" alt="Spyglass" style="height: 1em"> Overview

Horses are slow compared to endgame modes of transportation like elytras. They should be attractive
early- and midgame, but they’re impractical in face of common terrain features like forests and
rivers. They’re also very outdated—except for breeding tweaks in 2023 ([JE 1.19.4](https://minecraft.wiki/w/Java_Edition_1.19.4)/[BE 1.19.70](https://minecraft.wiki/w/Bedrock_Edition_1.19.70)),
they’ve seen no meaningful improvements in their mechanics since their first introduction in 2013 ([JE 1.6.1](https://minecraft.wiki/w/Java_Edition_1.6.1)).

This mod catches up horses to modern Minecraft and makes them a viable, competitive and pleasant
mode of transportation.

<p align="center">
  <img src=".github/assets/screenshot.png" width="600" alt="Black horse with gold-trimmed netherite armor and horseshoes standing on a frosted river">
</p>

## <img src=".github/assets/writable_book.png" alt="Book and Quill" style="height: 1em"> All Changes

### Horse Movement

- **Remove leaves collision** in certain cases for ridden horses (and other ridden animals),
  especially under trees
- **Automatically leash** horses (and other rideable animals) **when automatically dismounting
  underwater**, if leads are in the player’s inventory
- Horses (and donkeys and mules) **move faster in water**—horses in real life are excellent
  swimmers!

### Horse Armor

- **Netherite horse armor**, crafted by upgrading diamond horse armor
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

- **Horseshoes**, crafted with copper ingots
- **Speeds up horses** (and donkeys and mules) when equipped in the new horseshoe slot
- Support **enchanting** with all generic and boots-specific enchantments (Depth Strider, Feather
  Falling, Frost Walker, Soul Speed)
- De-facto fixes for existing bugs:
  - [`MC-268935`](https://bugs.mojang.com/browse/MC-268935): The frost walker enchantment doesn’t
    function on horse armor
  - [`MC-268936`](https://bugs.mojang.com/browse/MC-268936): The soul speed enchantment doesn’t
    function on horse armor

## <img src=".github/assets/filled_map.png" alt="Filled Map" style="height: 1em"> Installation

### Requirements

As a Fabric mod, this mod requires the [Fabric Loader](https://fabricmc.net/). It also needs the
following dependencies:

- <img src="https://cdn.modrinth.com/data/P7dR8mSH/icon.png" alt="Fabric API" style="height: 1em"> <a href="https://modrinth.com/mod/fabric-api">Fabric API</a>
- <img src="https://cdn.modrinth.com/data/Ha28R6CL/icon.png" alt="Fabric Language Kotlin" style="height: 1em"> <a href="https://modrinth.com/mod/fabric-language-kotlin">Fabric Language Kotlin</a>
