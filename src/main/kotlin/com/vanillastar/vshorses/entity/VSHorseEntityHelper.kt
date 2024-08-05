package com.vanillastar.vshorses.entity

import com.vanillastar.vshorses.utils.getModIdentifier
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.sound.SoundEvent

private val HORSELIKE: TagKey<EntityType<*>> =
  TagKey.of(RegistryKeys.ENTITY_TYPE, getModIdentifier("horselike"))

/** Whether an entity is horse-like enough to wear horseshoes. */
fun isHorselike(entity: Entity) = entity.type.isIn(HORSELIKE)

/** Sound for equipping a horseshoe. */
@JvmField
val EQUIP_HORSESHOE_SOUND: SoundEvent =
  SoundEvent.of(getModIdentifier("equip_horseshoe"))
