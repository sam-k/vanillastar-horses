package com.vanillastar.vshorses.item

import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.registry.RegistryKey

data class ModItemMetadata(
    /** Name for this [Item]. */
    val name: String,
    /** [RegistryKey] for this [Item] in the creative-mode inventory. */
    val itemGroup: RegistryKey<ItemGroup>,
    /** Settings for this [Item]. */
    val settingsProvider: (Item.Settings) -> Item.Settings,
)
