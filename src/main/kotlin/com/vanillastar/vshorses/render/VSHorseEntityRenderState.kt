package com.vanillastar.vshorses.render

import net.minecraft.item.ItemStack

@Suppress("FunctionName") // Mixin method name convention
interface VSHorseEntityRenderState {
  /** Gets the [ItemStack] corresponding to horseshoes. */
  fun `vshorses$getHorseshoe`(): ItemStack

  /** Sets the [ItemStack] corresponding to horseshoes. */
  fun `vshorses$setHorseshoe`(item: ItemStack)
}
