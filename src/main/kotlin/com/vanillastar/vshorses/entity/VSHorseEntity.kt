package com.vanillastar.vshorses.entity

import net.minecraft.entity.passive.AbstractHorseEntity
import net.minecraft.inventory.SingleStackInventory

@Suppress("FunctionName")  // Mixin method name convention
interface VSHorseEntity {
  /**
   * Gets the 0-indexed slot at which to **set** the horseshoe slot in the
   * entity's inventory screen.
   *
   * For example, a horse's inventory screen will have saddle set at 0,
   * horse armor at 1, and horseshoe at 2. Same for a donkey's, even though
   * donkeys cannot wear horse armor and its respective screen slot is never
   * drawn.
   */
  fun `vshorses$getHorseshoeScreenSlot`(): Int

  /**
   * Gets the 0-indexed slot at which to **draw** the horseshoe slot in the
   * entity's inventory screen.
   *
   * For example, a horse's inventory screen will have saddle drawn at 0, horse
   * armor at 1, and horseshoe at 2. A donkey's will have saddle drawn at 0 and
   * horseshoe at 1.
   */
  fun `vshorses$getHorseshoeScreenDrawSlot`(): Int

  /**
   * Gets the inventory dedicated to holding a horseshoe.
   *
   * [AbstractHorseEntity] inventory is dedicated only to storage. Saddle is
   * stored as a flag, and horse armor is stored in a separate
   * [SingleStackInventory] simply called `inventory`.
   */
  fun `vshorses$getHorseshoeInventory`(): SingleStackInventory

  /** Whether the entity supports wearing horseshoes. */
  fun `vshorses$canBeShoed`(): Boolean

  /** Whether the entity is wearing a horseshoe. */
  fun `vshorses$isShoed`(): Boolean
}
