package com.vanillastar.vshorses.mixin.gui;

import static com.vanillastar.vshorses.item.HorseArmorItemKt.HORSE_ARMOR;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(SmithingScreen.class)
@Environment(EnvType.CLIENT)
public abstract class SmithingScreenMixin extends ForgingScreen<SmithingScreenHandler> {
  @Unique
  private static final int BASE_SLOT = 1;

  @Unique
  @Nullable
  private HorseEntity horse;

  private SmithingScreenMixin(
      SmithingScreenHandler handler,
      PlayerInventory playerInventory,
      Text title,
      Identifier texture) {
    super(handler, playerInventory, title, texture);
  }

  @Unique
  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private boolean isSmithingHorseArmor() {
    return this.handler.getSlot(BASE_SLOT).getStack().isIn(HORSE_ARMOR);
  }

  @Inject(method = "equipArmorStand", at = @At("HEAD"), cancellable = true)
  private void equipHorse(ItemStack stack, CallbackInfo ci) {
    if (this.horse == null) {
      return;
    }

    this.horse.equipStack(EquipmentSlot.BODY, ItemStack.EMPTY);
    if (!this.isSmithingHorseArmor()) {
      return;
    }

    if (stack.isIn(HORSE_ARMOR)) {
      this.horse.equipStack(EquipmentSlot.BODY, stack.copy());
    }
    ci.cancel();
  }

  @Inject(method = "setup", at = @At("HEAD"))
  private void setupHorsePreview(CallbackInfo ci) {
    if (this.client == null || this.client.world == null) {
      return;
    }

    this.horse = new HorseEntity(EntityType.HORSE, this.client.world);
    this.horse.setBodyYaw(210.0F); // Same as `this.armorStand`
    this.horse.setPitch(25.0F); // Same as `this.armorStand`
  }

  @ModifyArgs(
      method = "drawBackground",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;drawEntity(Lnet/minecraft/client/gui/DrawContext;FFFLorg/joml/Vector3f;Lorg/joml/Quaternionf;Lorg/joml/Quaternionf;Lnet/minecraft/entity/LivingEntity;)V"))
  private void drawHorseOnBackground(Args args) {
    if (this.horse == null || !this.isSmithingHorseArmor()) {
      return;
    }

    args.set(1, (float) this.x + 148); // `float x`
    args.set(2, (float) this.y + 69); // `float y`
    args.set(3, 22.5F); // `float size`
    args.set(7, this.horse); // `LivingEntity entity`
  }
}
