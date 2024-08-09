package com.vanillastar.vshorses.mixin.item;

import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Stream;

import static com.vanillastar.vshorses.utils.IdentiferHelperKt.getModIdentifier;

@Mixin(SmithingTemplateItem.class)
public abstract class SmithingTemplateItemMixin {
  @Unique
  private static final Identifier EMPTY_HORSE_ARMOR_SLOT_TEXTURE =
    getModIdentifier("item/empty_horse_armor_slot");

  @Inject(
    method = "getArmorTrimEmptyBaseSlotTextures",
    at = @At("RETURN"),
    cancellable = true
  )
  private static void addHorseArmorEmptyBaseSlotTexture(
    @NotNull CallbackInfoReturnable<List<Identifier>> cir
  ) {
    cir.setReturnValue(Stream.concat(cir.getReturnValue().stream(),
        Stream.of(EMPTY_HORSE_ARMOR_SLOT_TEXTURE)
      )
      .toList());
  }
}
