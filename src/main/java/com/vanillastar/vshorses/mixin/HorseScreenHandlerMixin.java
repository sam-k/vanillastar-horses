package com.vanillastar.vshorses.mixin;

import com.vanillastar.vshorses.entity.VSHorseEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.HorseScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.vanillastar.vshorses.item.HorseshoeItemKt.HORSESHOE_ITEM;
import static com.vanillastar.vshorses.utils.ScreenHelperKt.INVENTORY_SLOT_SIZE_PX;

@Mixin(HorseScreenHandler.class)
public abstract class HorseScreenHandlerMixin extends ScreenHandler {
  protected HorseScreenHandlerMixin(
    @Nullable ScreenHandlerType<HorseScreenHandler> type, int syncId
  ) {
    super(type, syncId);
  }

  @Inject(
    method = "<init>", at = @At(
    value = "INVOKE",
    // Target immediately after the second `addSlot` call, because screen slot
    // indices are determined by `addSlot` call order.
    // First call adds the saddle screen slot. Second call adds the horse armor
    // screen slot, even if the entity cannot wear horse armor.
    // Note that an entity's inventory screen slot indices are unrelated to its
    // inventory slot indices.
    target = "Lnet/minecraft/screen/HorseScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;",
    ordinal = 1,
    shift = At.Shift.AFTER
  )
  )
  private void initHorseshoeScreenSlot(
    int syncId,
    PlayerInventory playerInventory,
    Inventory inventory,
    AbstractHorseEntity entity,
    int slotColumnCount,
    CallbackInfo ci
  ) {
    if (!(entity instanceof VSHorseEntity vsHorseEntity)) {
      return;
    }

    this.addSlot(new Slot(
      inventory,
      vsHorseEntity.vshorses$getHorseshoeInventorySlot(),
      8,
      INVENTORY_SLOT_SIZE_PX *
        (vsHorseEntity.vshorses$getHorseshoeScreenSlot() + 1)
    ) {
      @Override
      public boolean canInsert(ItemStack stack) {
        return stack.isOf(HORSESHOE_ITEM) &&
          !this.hasStack() &&
          vsHorseEntity.vshorses$canBeShoed();
      }

      @Override
      public boolean isEnabled() {
        return vsHorseEntity.vshorses$canBeShoed();
      }
    });
  }
}
