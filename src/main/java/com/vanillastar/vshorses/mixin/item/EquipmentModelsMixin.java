package com.vanillastar.vshorses.mixin.item;

import static com.vanillastar.vshorses.utils.IdentiferHelperKt.getModIdentifier;

import java.util.function.BiConsumer;
import net.minecraft.item.equipment.EquipmentModel;
import net.minecraft.item.equipment.EquipmentModels;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EquipmentModels.class)
public interface EquipmentModelsMixin {
  @Inject(method = "accept(Ljava/util/function/BiConsumer;)V", at = @At(value = "TAIL"))
  private static void acceptNetheriteHorseArmor(
      @NotNull BiConsumer<Identifier, EquipmentModel> equipmentModelBiConsumer, CallbackInfo ci) {
    equipmentModelBiConsumer.accept(
        getModIdentifier("netherite"),
        EquipmentModel.builder().addLayers(EquipmentModel.LayerType.HORSE_BODY).build());
  }
}
