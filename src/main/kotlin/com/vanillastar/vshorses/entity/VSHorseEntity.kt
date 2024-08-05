package com.vanillastar.vshorses.entity

import net.minecraft.item.ItemStack

@Suppress("FunctionName")  // Mixin method name convention
interface VSHorseEntity {
  /**
   * Gets the 0-indexed slot at which to draw the horseshoe slot in the entity's
   * inventory screen.
   *
   * For example, a horse's inventory screen will have saddle at 0, horse armor
   * at 1, and horseshoe at 2.
   */
  fun `vshorses$getHorseshoeScreenSlot`(): Int

  /**
   * Gets the 0-indexed slot at which to store the horseshoe in the entity's
   * inventory.
   *
   * For example, a horse will have saddle at 0 and horseshoe at 1.
   */
  fun `vshorses$getHorseshoeInventorySlot`(): Int

  /** Whether the entity supports wearing horseshoes. */
  fun `vshorses$canBeShoed`(): Boolean

  /** Whether the entity is wearing a horseshoe. */
  fun `vshorses$isShoed`(): Boolean

  /** Gets item in the entity's horseshoe inventory slot. */
  fun `vshorses$getHorseshoeInInventory`(): ItemStack

  /** Sets item to the entity's horseshoe inventory slot. */
  fun `vshorses$setHorseshoeInInventory`(stack: ItemStack)
}
