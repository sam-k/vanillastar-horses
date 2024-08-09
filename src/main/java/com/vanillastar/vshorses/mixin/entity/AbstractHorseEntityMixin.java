package com.vanillastar.vshorses.mixin.entity;

import com.vanillastar.vshorses.entity.VSHorseEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SingleStackInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.vanillastar.vshorses.entity.VSHorseEntityHelperKt.EQUIP_HORSESHOE_SOUND;
import static com.vanillastar.vshorses.entity.VSHorseEntityHelperKt.isHorselike;
import static com.vanillastar.vshorses.item.HorseshoeItemKt.HORSESHOE_ITEM;

@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseEntityMixin extends AnimalEntity implements VSHorseEntity {
  /**
   * NBT tag name for the entity's horseshoe inventory slot.
   */
  @Unique
  private static final String HORSESHOE_INVENTORY_SLOT_NBT_TAG =
    "HorseshoeItem";

  /**
   * Number of ticks of travel each horseshoe durability point affords.
   */
  @Unique
  private static final int TICKS_PER_HORSESHOE_DAMAGE = 20 * 10;

  /**
   * Number of ticks this entity has been moving while ridden.
   * <p>
   * This is reset to 0 every {@code TICKS_PER_HORSESHOE_DAMAGE}.
   */
  @Unique
  private int movingWhileRiddenTicks = 0;

  /**
   * Pseudo-inventory in which to hold a horseshoe.
   * <p>
   * All inventory accesses and updates are redirected to the entity's
   * equipment slots.
   */
  @Unique
  private final SingleStackInventory horseshoeInventory =
    new SingleStackInventory() {
      @Override
      public ItemStack getStack() {
        return AbstractHorseEntityMixin.this.getEquippedStack(HORSESHOE_ITEM.getEquipmentSlot());
      }

      @Override
      public void setStack(@NotNull ItemStack stack) {
        if (!stack.isEmpty()) {
          // Same hack as in `AbstractHorseEntity.onInventoryChanged` to
          // suppress sound playback upon entity initialization.
          if (AbstractHorseEntityMixin.this.age > 20) {
            AbstractHorseEntityMixin.this.playSound(EQUIP_HORSESHOE_SOUND,
              0.5F,
              1.0F
            );
          }
        }
        AbstractHorseEntityMixin.this.equipLootStack(HORSESHOE_ITEM.getEquipmentSlot(),
          stack
        );
      }

      @Override
      public void markDirty() {
      }

      @Override
      public boolean canPlayerUse(@NotNull PlayerEntity player) {
        return player.getVehicle() == AbstractHorseEntityMixin.this ||
          player.canInteractWithEntity(AbstractHorseEntityMixin.this, 4.0);
      }
    };

  @Shadow
  public abstract boolean canBeSaddled();

  private AbstractHorseEntityMixin(
    EntityType<? extends AnimalEntity> entityType, World world
  ) {
    super(entityType, world);
  }

  @Override
  public int vshorses$getHorseshoeScreenSlot() {
    // Screen slot 0 is for saddle. Screen slot 1 is for horse armor, even if
    // the entity does not support wearing it.
    return 2;
  }

  @Override
  public int vshorses$getHorseshoeScreenDrawSlot() {
    // Drawn screen slot 0 is for saddle. Drawn screen slot 1 is for horse armor
    // if applicable.
    return this.canUseSlot(EquipmentSlot.BODY) ? 2 : 1;
  }

  @Override
  public @NotNull SingleStackInventory vshorses$getHorseshoeInventory() {
    return this.horseshoeInventory;
  }

  @Override
  public boolean vshorses$canBeShoed() {
    return this.canBeSaddled() && isHorselike(this);
  }

  @Override
  public boolean vshorses$isShoed() {
    return this.horseshoeInventory.getStack().isOf(HORSESHOE_ITEM);
  }

  @Override
  public void damageArmor(DamageSource source, float amount) {
    this.damageEquipment(source, amount, EquipmentSlot.BODY);
  }

  @Inject(
    method = "createBaseHorseAttributes",
    at = @At(value = "RETURN"),
    cancellable = true
  )
  private static void addWaterMovementEfficiencyAttribute(
    @NotNull CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir
  ) {
    cir.setReturnValue(cir.getReturnValue()
      .add(EntityAttributes.GENERIC_WATER_MOVEMENT_EFFICIENCY, 0.3D));
  }

  @Inject(method = "tickControlled", at = @At("TAIL"))
  private void damageHorseshoeOnTickControlled(
    @NotNull PlayerEntity controllingPlayer,
    @NotNull Vec3d movementInput,
    CallbackInfo ci
  ) {
    if (!(this.getWorld() instanceof ServerWorld) ||
      !isHorselike(this) ||
      !this.vshorses$isShoed()) {
      return;
    }
    if (controllingPlayer.forwardSpeed == 0.0F &&
      controllingPlayer.sidewaysSpeed == 0.0F) {
      // Entity is not being directed to move.
      return;
    }

    movingWhileRiddenTicks =
      (movingWhileRiddenTicks + 1) % TICKS_PER_HORSESHOE_DAMAGE;
    if (movingWhileRiddenTicks == 0) {
      this.horseshoeInventory.getStack().damage(1, this, EquipmentSlot.FEET);
    }
  }

  @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
  private void writeHorseshoeDataToNbt(NbtCompound nbt, CallbackInfo ci) {
    ItemStack stack = this.horseshoeInventory.getStack();
    if (!stack.isEmpty()) {
      nbt.put(HORSESHOE_INVENTORY_SLOT_NBT_TAG,
        stack.encode(this.getRegistryManager())
      );
    }
  }

  @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
  private void readHorseshoeDataFromNbt(
    @NotNull NbtCompound nbt, CallbackInfo ci
  ) {
    if (!nbt.contains(HORSESHOE_INVENTORY_SLOT_NBT_TAG,
      NbtElement.COMPOUND_TYPE
    )) {
      return;
    }
    ItemStack stack = ItemStack.fromNbt(this.getRegistryManager(),
      nbt.getCompound(HORSESHOE_INVENTORY_SLOT_NBT_TAG)
    ).orElse(ItemStack.EMPTY);
    if (stack.isOf(HORSESHOE_ITEM)) {
      this.horseshoeInventory.setStack(stack);
    }
  }
}
