package com.vanillastar.vshorses.mixin.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Leashable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
  private LivingEntityMixin(EntityType type, World world) {
    super(type, world);
  }

  @Inject(method = "onDismounted", at = @At("TAIL"))
  private void leashUponUnderwaterDismount(
    @NotNull Entity vehicle, CallbackInfo ci
  ) {
    if (!((LivingEntity) (Object) this instanceof PlayerEntity player) ||
      !player.isSubmergedIn(FluidTags.WATER)) {
      return;
    }
    if (!vehicle.shouldDismountUnderwater() ||
      !(vehicle instanceof Leashable leashable) ||
      !leashable.canLeashAttachTo()) {
      return;
    }

    PlayerInventory playerInventory = player.getInventory();
    int leadSlot = playerInventory.getSlotWithStack(new ItemStack(Items.LEAD));
    if (leadSlot < 0) {
      // No leads in inventory.
      return;
    }
    ItemStack itemStack = playerInventory.getStack(leadSlot);
    if (!this.getWorld().isClient()) {
      leashable.attachLeash(player, true);
    }
    if (!player.isInCreativeMode()) {
      itemStack.decrement(1);
    }
  }
}
