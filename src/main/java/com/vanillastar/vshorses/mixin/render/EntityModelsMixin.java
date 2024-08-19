package com.vanillastar.vshorses.mixin.render;

import static com.vanillastar.vshorses.render.HorseshoeEntityModel.HORSESHOE_MODEL;

import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.sugar.Local;
import com.vanillastar.vshorses.render.HorseArmorEntityModel;
import com.vanillastar.vshorses.render.HorseshoeEntityModel;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModels;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(EntityModels.class)
@Environment(EnvType.CLIENT)
public abstract class EntityModelsMixin {
  @ModifyArgs(
      method = "getModels",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lcom/google/common/collect/ImmutableMap$Builder;put(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;",
              remap = false))
  private static void addHorseArmorModel(@NotNull Args args) {
    // `EntityModelLayer key`.
    if (args.get(0) != EntityModelLayers.HORSE_ARMOR) {
      return;
    }
    // `TexturedModelData value`.
    args.set(
        1, TexturedModelData.of(HorseArmorEntityModel.getModelData(new Dilation(0.1F)), 64, 64));
  }

  @Inject(
      method = "getModels",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;",
              remap = false))
  private static void addHorseshoeModel(
      CallbackInfoReturnable<Map<EntityModelLayer, TexturedModelData>> cir,
      @Local ImmutableMap.@NotNull Builder<EntityModelLayer, TexturedModelData> builder) {
    builder.put(
        HORSESHOE_MODEL,
        TexturedModelData.of(HorseshoeEntityModel.getModelData(new Dilation(0.1F)), 64, 64));
  }
}
