package com.vanillastar.vshorses.mixin.screen;

import static com.vanillastar.vshorses.entity.VSHorseEntityHelperKt.isHorselike;
import static com.vanillastar.vshorses.item.HorseshoeItemKt.HORSESHOE_ITEM;
import static com.vanillastar.vshorses.utils.ScreenHelperKt.INVENTORY_SLOT_SIZE_PX;

import com.vanillastar.vshorses.entity.VSHorseEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.HorseScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.ArmorSlot;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HorseScreenHandler.class)
public abstract class HorseScreenHandlerMixin extends ScreenHandler {
  @Shadow
  @Final
  private Inventory inventory;

  @Shadow
  @Final
  private AbstractHorseEntity entity;

  private HorseScreenHandlerMixin(
      @Nullable ScreenHandlerType<HorseScreenHandler> type, int syncId) {
    super(type, syncId);
  }

  @Inject(
      method = "<init>",
      at =
          @At(
              value = "INVOKE",
              // Target immediately after the second `addSlot` call, because screen slot indices are
              // determined by `addSlot` call order.
              // First call adds the saddle screen slot. Second call adds the horse armor screen
              // slot, even if the entity cannot wear horse armor.
              // Note that an entity's inventory screen slot indices are unrelated to its inventory
              // slot indices.
              target =
                  "Lnet/minecraft/screen/HorseScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;",
              ordinal = 1,
              shift = At.Shift.AFTER))
  private void initHorseshoeScreenSlot(
      int syncId,
      PlayerInventory playerInventory,
      Inventory inventory,
      AbstractHorseEntity entity,
      int slotColumnCount,
      CallbackInfo ci) {
    if (!(entity instanceof VSHorseEntity vsHorseEntity)) {
      return;
    }

    this.addSlot(
        new ArmorSlot(
            vsHorseEntity.vshorses$getHorseshoeInventory(),
            entity,
            EquipmentSlot.FEET,
            0, // This is a pseudo-inventory with size 1, so 0 is the only index
            8,
            INVENTORY_SLOT_SIZE_PX * (vsHorseEntity.vshorses$getHorseshoeScreenDrawSlot() + 1),
            null) {
          @Override
          public boolean canInsert(ItemStack stack) {
            return stack.isOf(HORSESHOE_ITEM)
                && !this.hasStack()
                && vsHorseEntity.vshorses$canBeShoed();
          }

          @Override
          public boolean isEnabled() {
            return vsHorseEntity.vshorses$canBeShoed();
          }
        });
  }

  @Inject(method = "quickMove", at = @At("HEAD"), cancellable = true)
  private void supportHorseshoeInQuickMove(
      PlayerEntity player, int screenSlot, CallbackInfoReturnable<ItemStack> cir) {
    if (!isHorselike(this.entity) || !(this.entity instanceof VSHorseEntity vsHorseEntity)) {
      return;
    }

    Slot screenSlotRef = this.slots.get(screenSlot);
    if (!screenSlotRef.hasStack()) {
      // Nothing to quick-move from screen slot.
      cir.setReturnValue(ItemStack.EMPTY);
      cir.cancel();
      return;
    }

    ItemStack itemStack = screenSlotRef.getStack();
    ItemStack itemStackCopy = itemStack.copy();
    int horseshoeScreenSlot = vsHorseEntity.vshorses$getHorseshoeScreenSlot();
    // Entity's screen inventory size, including the sizes of its pseudo-inventories (horse armor,
    // horseshoe).
    int screenInventorySize = this.inventory.size() + 2;

    if (screenSlot < screenInventorySize) {
      // Screen slot is in entity's screen inventory. Try quick-moving to all other screen slots.
      if (!this.insertItem(itemStackCopy, screenInventorySize, this.slots.size(), true)) {
        cir.setReturnValue(ItemStack.EMPTY); // Quick-move failed
        cir.cancel();
        return;
      }
    } else if (this.getSlot(horseshoeScreenSlot).canInsert(itemStackCopy)
        && !this.getSlot(horseshoeScreenSlot).hasStack()) {
      // Entity's horseshoe screen slot can accept this item. Try quick-moving to there.
      if (!this.insertItem(itemStackCopy, horseshoeScreenSlot, horseshoeScreenSlot + 1, false)) {
        cir.setReturnValue(ItemStack.EMPTY); // Quick-move failed
        cir.cancel();
        return;
      }
    } else {
      // Defer to existing implementation.
      return;
    }

    if (itemStackCopy.isEmpty()) {
      screenSlotRef.setStack(ItemStack.EMPTY);
    } else {
      screenSlotRef.markDirty();
    }
    cir.setReturnValue(itemStack);
    cir.cancel();
  }
}
