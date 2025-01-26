package com.vanillastar.vshorses.mixin.render;

import com.vanillastar.vshorses.render.VSHorseEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.LivingHorseEntityRenderState;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingHorseEntityRenderState.class)
public abstract class LivingHorseEntityRenderStateMixin extends LivingEntityRenderState
    implements VSHorseEntityRenderState {
  @Unique
  private ItemStack horseshoe = ItemStack.EMPTY;

  @Override
  public @NotNull ItemStack vshorses$getHorseshoe() {
    return horseshoe;
  }

  @Override
  public void vshorses$setHorseshoe(@NotNull ItemStack itemStack) {
    horseshoe = itemStack;
  }
}
