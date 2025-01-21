package com.vanillastar.vshorses.mixin.component;

import static com.vanillastar.vshorses.item.ModItemsKt.MOD_ITEMS;

import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EquippableComponent.class)
public abstract class EquippableComponentMixin {
  @Inject(
      method =
          "equip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/util/ActionResult;",
      at = @At("HEAD"),
      cancellable = true)
  private void disallowHorseshoeEquipByPlayer(
      @NotNull ItemStack stack, PlayerEntity player, CallbackInfoReturnable<ActionResult> cir) {
    if (stack.isOf(MOD_ITEMS.horseshoeItem)) {
      cir.setReturnValue(ActionResult.PASS);
    }
  }
}
