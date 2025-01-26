package com.vanillastar.vshorses.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType

/** Set of horse-like entities capable of wearing horseshoes. */
val HORSELIKE: Set<EntityType<*>> =
    setOf(
        EntityType.DONKEY,
        EntityType.HORSE,
        EntityType.MULE,
        EntityType.SKELETON_HORSE,
        EntityType.ZOMBIE_HORSE,
    )

/** Whether an entity is horse-like enough to wear horseshoes. */
fun isHorselike(entity: Entity) = HORSELIKE.contains(entity.type)
