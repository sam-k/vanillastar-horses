package com.vanillastar.vshorses.mixin.gui;

import com.llamalad7.mixinextras.sugar.Local;
import com.vanillastar.vshorses.entity.VSHorseEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HorseScreen;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.HorseScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.vanillastar.vshorses.utils.IdentiferHelperKt.getModIdentifier;
import static com.vanillastar.vshorses.utils.ScreenHelperKt.INVENTORY_SLOT_SIZE_PX;

@Mixin(HorseScreen.class)
@Environment(EnvType.CLIENT)
public abstract class HorseScreenMixin extends HandledScreen<HorseScreenHandler> {
  /**
   * ID for the texture sprite of the horse inventory screen's horseshoe slot.
   */
  @Unique
  private static final Identifier HORSESHOE_SLOT_SPRITE_ID =
    getModIdentifier("container/horse/horseshoe_slot");

  @Shadow
  @Final
  private AbstractHorseEntity entity;

  private HorseScreenMixin(
    HorseScreenHandler handler, PlayerInventory inventory, Text title
  ) {
    super(handler, inventory, title);
  }

  @Inject(method = "drawBackground", at = @At("TAIL"))
  private void drawHorseshoeSlot(
    DrawContext context,
    float delta,
    int mouseX,
    int mouseY,
    CallbackInfo ci,
    @Local(ordinal = 2) int i,
    @Local(ordinal = 3) int j
  ) {
    if (!(
      this.entity instanceof VSHorseEntity vsHorseEntity &&
        vsHorseEntity.vshorses$canBeShoed()
    )) {
      return;
    }

    int screenDrawSlot = vsHorseEntity.vshorses$getHorseshoeScreenDrawSlot();
    context.drawGuiTexture(
      HORSESHOE_SLOT_SPRITE_ID,
      i + 7,
      j + (INVENTORY_SLOT_SIZE_PX * (screenDrawSlot + 1)) - 1,
      INVENTORY_SLOT_SIZE_PX,
      INVENTORY_SLOT_SIZE_PX
    );
  }
}
