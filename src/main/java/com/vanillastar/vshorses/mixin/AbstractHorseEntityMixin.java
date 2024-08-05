package com.vanillastar.vshorses.mixin;

import com.vanillastar.vshorses.entity.VSHorseEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;
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
import static com.vanillastar.vshorses.utils.IdentiferHelperKt.getModIdentifier;

@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseEntityMixin extends AnimalEntity implements VSHorseEntity {
  /**
   * {@code HORSE_FLAGS} value for horseshoe equipped status.
   * <p>
   * This is the next flag after {@code EATING_FLAG = 64}.
   */
  @Unique
  private static final int SHOED_FLAG = 128;

  /**
   * NBT tag name for the entity's horseshoe inventory slot.
   */
  @Unique
  private static final String HORSESHOE_INVENTORY_SLOT_NBT_TAG =
    "HorseshoeItem";

  /**
   * ID for the {@link EntityAttributeModifier} that makes shoed horses faster.
   */
  @Unique
  private static final Identifier HORSESHOE_SPEED_MODIFIER_ID =
    getModIdentifier("horseshoe_boost");

  @Shadow
  protected SimpleInventory items;

  @Shadow
  public abstract boolean canBeSaddled();

  @Shadow
  protected abstract boolean getHorseFlag(int bitmask);

  @Shadow
  protected abstract void setHorseFlag(int bitmask, boolean flag);

  @Shadow
  public abstract int getInventorySize();

  protected AbstractHorseEntityMixin(
    EntityType<? extends AnimalEntity> entityType, World world
  ) {
    super(entityType, world);
  }

  @Override
  public int vshorses$getHorseshoeScreenSlot() {
    // Screen slot 0 is for saddle. Screen slot 1 is for horse armor if
    // applicable.
    return this.canUseSlot(EquipmentSlot.BODY) ? 2 : 1;
  }

  @Override
  public int vshorses$getHorseshoeInventorySlot() {
    // Set horseshoe inventory slot to end of the inventory to prevent collision
    // with any storage inventory slots.
    return this.getInventorySize() - 1;
  }

  @Override
  public boolean vshorses$canBeShoed() {
    return this.canBeSaddled() && isHorselike(this);
  }

  @Override
  public boolean vshorses$isShoed() {
    return this.getHorseFlag(SHOED_FLAG);
  }

  @Override
  public @NotNull ItemStack vshorses$getHorseshoeInInventory() {
    return this.items.getStack(this.vshorses$getHorseshoeInventorySlot());
  }

  @Override
  public void vshorses$setHorseshoeInInventory(@NotNull ItemStack stack) {
    this.items.setStack(this.vshorses$getHorseshoeInventorySlot(), stack);
  }

  @Unique
  protected void updateShoedFlag() {
    if (!this.getWorld().isClient) {
      this.setHorseFlag(SHOED_FLAG,
        this.vshorses$getHorseshoeInInventory().isOf(HORSESHOE_ITEM)
      );
    }
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

  @Inject(
    method = "getInventorySize(I)I", at = @At("RETURN"), cancellable = true
  )
  private static void getShoeableInventorySize(
    int columns, @NotNull CallbackInfoReturnable<Integer> cir
  ) {
    // This makes all `AbstractHorseEntity`'s have inventory space for
    // horseshoes, but this is less painful than modifying this static method to
    // read the instance's `EntityType`.
    cir.setReturnValue(cir.getReturnValue() + 1);
    cir.cancel();
  }

  @Inject(method = "onInventoryChanged", at = @At("TAIL"))
  private void onHorseshoeInventoryChanged(Inventory sender, CallbackInfo ci) {
    EntityAttributeInstance speedAttributeInstance =
      this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
    assert speedAttributeInstance != null;

    boolean wasPreviouslyShoed = this.vshorses$isShoed();
    this.updateShoedFlag();
    if (!this.vshorses$isShoed()) {
      speedAttributeInstance.removeModifier(HORSESHOE_SPEED_MODIFIER_ID);
      return;
    }
    if (wasPreviouslyShoed) {
      // No change in horseshoe equipped status.
      return;
    }

    speedAttributeInstance.addTemporaryModifier(HORSESHOE_ITEM.getSpeedModifierWithId(
      HORSESHOE_SPEED_MODIFIER_ID));
    // Same hack as in `AbstractHorseEntity.onInventoryChanged` to suppress
    // sound playback upon entity initialization.
    if (this.age > 20) {
      this.playSound(EQUIP_HORSESHOE_SOUND, 0.5F, 1.0F);
    }
  }

  @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
  private void writeHorseshoeDataToNbt(NbtCompound nbt, CallbackInfo ci) {
    ItemStack stack = this.vshorses$getHorseshoeInInventory();
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
    if (nbt.contains(HORSESHOE_INVENTORY_SLOT_NBT_TAG,
      NbtElement.COMPOUND_TYPE
    )) {
      ItemStack stack = ItemStack.fromNbt(this.getRegistryManager(),
        nbt.getCompound(HORSESHOE_INVENTORY_SLOT_NBT_TAG)
      ).orElse(ItemStack.EMPTY);
      if (stack.isOf(HORSESHOE_ITEM)) {
        this.vshorses$setHorseshoeInInventory(stack);
      }
    }
    this.updateShoedFlag();
  }
}
