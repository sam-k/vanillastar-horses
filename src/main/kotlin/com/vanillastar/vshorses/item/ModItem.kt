package com.vanillastar.vshorses.item

import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier

interface ModItem {
  /** ID for this [Item]. */
  val id: Identifier

  /** [RegistryKey] for this [Item] in the creative-mode inventory. */
  val itemGroup: RegistryKey<ItemGroup>
}
