package com.vanillastar.vshorses.sound

import com.vanillastar.vshorses.utils.ModRegistry
import com.vanillastar.vshorses.utils.getModIdentifier
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry.Reference
import net.minecraft.sound.SoundEvent

abstract class ModSounds : ModRegistry() {
  /** Sound for equipping a horseshoe. */
  @JvmField val equipHorseshoeSound = registerSound("equip_horseshoe")

  @Suppress("SameParameterValue")
  private fun registerSound(name: String): Reference<SoundEvent> {
    val id = getModIdentifier(name)
    val sound = Registry.registerReference(Registries.SOUND_EVENT, id, SoundEvent.of(id))
    logger.info("Registered sound {}", id)
    return sound
  }

  override fun initialize() {}
}

@JvmField val MOD_SOUNDS = object : ModSounds() {}
