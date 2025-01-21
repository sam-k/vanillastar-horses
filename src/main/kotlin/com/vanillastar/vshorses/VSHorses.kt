package com.vanillastar.vshorses

import com.vanillastar.vshorses.item.MOD_ITEMS
import com.vanillastar.vshorses.networking.MOD_NETWORKING
import com.vanillastar.vshorses.sound.MOD_SOUNDS
import net.fabricmc.api.ModInitializer

const val MOD_ID = "vshorses"

object VSHorses : ModInitializer {
  override fun onInitialize() {
    MOD_ITEMS.initialize()
    MOD_SOUNDS.initialize()
    MOD_NETWORKING.initialize()
  }
}
