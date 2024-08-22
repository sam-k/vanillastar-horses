<!--suppress ALL -->

<p style="text-align: center">
  <img src="src/main/resources/vshorses-icon.png" width="128" alt="Vanilla Star Horses">
</p>

# Vanilla* Horses

Minecraft Fabric mod for horses as viable mode of transportation.

---

<h2 style="display: flex; column-gap: 0.25em; align-items: center">
  <img src=".github/assets/spyglass.png" alt="Spyglass" style="height: 1em">
  <span>Overview</span>
</h2>

Horses are slow compared to endgame modes of transportation like elytras, and they are impractical
early- and midgame in face of common terrain features like forests and rivers. They are also very
outdated—except for breeding tweaks in <span title="Java 1.19.4; Bedrock 1.19.70" style="text-decoration: underline dotted; cursor: help">2023</span>,
they’ve seen no meaningful improvements in their mechanics since their introduction in <span title="Java 1.6.1, the “Horse Update”" style="text-decoration: underline dotted; cursor: help">2013</span>.

This mod catches up horses to modern Minecraft and makes them a viable, competitive and pleasant
mode of transportation.

<p style="text-align: center">
  <img src=".github/assets/screenshot.png" width="600" alt="Black horse with gold-trimmed netherite armor and horseshoes standing on a frosted river">
</p>

---

<h2 style="display: flex; column-gap: 0.25em; align-items: center">
  <img src=".github/assets/writable_book.png" alt="Book and Quill" style="height: 1em">
  <span>All Changes</span>
</h2>

### Horse Movement

- **Remove leaves collision** in certain cases for ridden horses[^1]
- **Automatically leash horses**[^1] when automatically dismounting underwater, if leads are in the
  player’s inventory
- Horses[^2] **swim faster** in water

### Horse Armor

- **Netherite horse armor**, crafted by upgrading diamond horse armor
- Support **enchanting** with all generic and armor-specific enchantments (e.g., Protection, Thorns)
- Support **trimming** with all armor trims
- Add **durability** according to its material
- Miscellaneous fixes for existing bugs:
  - [MC-16829](https://bugs.mojang.com/browse/MC-16829): The horse armor model does not show the
enchantment glint effect
  - [MC-275395](https://bugs.mojang.com/browse/MC-275395): Some model parts of horse armor do not
have a visible 0.1F cube deformation
  - [MC-275574](https://bugs.mojang.com/browse/MC-275574): Rendered horse armor textures are
asymmetric

### Horseshoe

- Add **horseshoes**, crafted with copper ingots
- **Speeds up horses**[^2] when equipped in the new horseshoe slot
- Support **enchanting** with all generic and boots-specific enchantments (Depth Strider, Feather
Falling, Frost Walker, Soul Speed)
- De-facto fixes for existing bugs:
  - [MC-268935](https://bugs.mojang.com/browse/MC-268935): The frost walker enchantment doesn’t
function on horse armor
  - [MC-268936](https://bugs.mojang.com/browse/MC-268936): The soul speed enchantment doesn’t
function on horse armor

[^1]: Also all other rideable animals (donkeys, mules, camels, llamas, pigs, striders)
[^2]: Also donkeys and mules

---

<h2 style="display: flex; column-gap: 0.25em; align-items: center">
  <img src=".github/assets/filled_map.png" alt="Filled Map" style="height: 1em">
  <span>Installation</span>
</h2>

### Requirements

As a Fabric mod, this mod requires the [Fabric Loader](https://fabricmc.net/). It also needs the
following dependencies:

<ul>
  <li>
    <a href="https://modrinth.com/mod/fabric-api" style="display: flex; column-gap: 0.25em; align-items: center">
      <img src="https://cdn.modrinth.com/data/P7dR8mSH/icon.png" alt="Fabric API" style="height: 1em">
      <span>Fabric API</span>
    </a>
  </li>
  <li>
    <a href="https://modrinth.com/mod/fabric-language-kotlin" style="display: flex; column-gap: 0.25em; align-items: center">
      <img src="https://cdn.modrinth.com/data/Ha28R6CL/icon.png" alt="Fabric API" style="height: 1em">
      <span>Fabric Language Kotlin</span>
    </a>
  </li>
</ul>
