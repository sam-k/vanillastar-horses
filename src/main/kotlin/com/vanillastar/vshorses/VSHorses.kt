package com.vanillastar.vshorses

import com.google.common.collect.ImmutableList
import com.vanillastar.vshorses.entity.EQUIP_HORSESHOE_SOUND
import com.vanillastar.vshorses.item.HORSESHOE_ITEM
import com.vanillastar.vshorses.item.NETHERITE_HORSE_ARMOR_ITEM
import com.vanillastar.vshorses.utils.getLogger
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

const val MOD_ID = "vshorses"

object VSHorses: ModInitializer {
  private val LOGGER = getLogger()

  private fun registerItems() {
    val items = ImmutableList.of(HORSESHOE_ITEM, NETHERITE_HORSE_ARMOR_ITEM)
    for (item in items) {
      Registry.register(Registries.ITEM, item.id, item)
      ItemGroupEvents.modifyEntriesEvent(item.itemGroup)
        .register { entries -> entries.add(item) }
      LOGGER.info(
        "Registered item {} in group {}|{}",
        item.id,
        item.itemGroup.registry,
        item.itemGroup.value
      )
    }
  }

  private fun registerSounds() {
    val sounds = ImmutableList.of(EQUIP_HORSESHOE_SOUND)
    for (sound in sounds) {
      Registry.register(Registries.SOUND_EVENT, sound.id, sound)
      LOGGER.info("Registered sound {}", sound.id)
    }
  }

  override fun onInitialize() {
    registerItems()
    registerSounds()
  }
}
