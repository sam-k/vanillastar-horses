package com.vanillastar.vshorses.item

import com.vanillastar.vshorses.utils.ModRegistry
import com.vanillastar.vshorses.utils.getModIdentifier
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys

abstract class ModItems : ModRegistry() {
  @JvmField val horseshoeItem = registerItem(HORSESHOE_ITEM_METADATA, ::HorseshoeItem)

  @Suppress("Unused")
  @JvmField
  val netheriteHorseArmorItem =
      registerItem(NETHERITE_HORSE_ARMOR_ITEM_METADATA, ::NetheriteHorseArmorItem)

  private fun registerItem(metadata: ModItemMetadata, constructor: (Item.Settings) -> Item): Item {
    val item =
        Items.register(
            RegistryKey.of(RegistryKeys.ITEM, getModIdentifier(metadata.name)),
            { constructor(metadata.settingsProvider(it)) },
            Item.Settings(),
        )
    ItemGroupEvents.modifyEntriesEvent(metadata.itemGroup).register { it.add(item) }
    logger.info(
        "Registered item {} in group {}|{}",
        metadata.name,
        metadata.itemGroup.registry,
        metadata.itemGroup.value,
    )
    return item
  }

  override fun initialize() {}
}

@JvmField val MOD_ITEMS = object : ModItems() {}
