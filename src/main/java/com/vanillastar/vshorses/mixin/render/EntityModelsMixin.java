package com.vanillastar.vshorses.mixin.render;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModels;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

import static com.vanillastar.vshorses.render.HorseshoeModelLayerKt.HORSESHOE_MODEL_LAYER;

@Mixin(EntityModels.class)
@Environment(EnvType.CLIENT)
public class EntityModelsMixin {
  @Inject(
    method = "getModels", at = @At(
    value = "INVOKE",
    target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;",
    shift = At.Shift.BEFORE
  ), locals = LocalCapture.CAPTURE_FAILHARD
  )
  private static void addHorseshoeModel(
    CallbackInfoReturnable<Map<EntityModelLayer, TexturedModelData>> cir,
    ImmutableMap.@NotNull Builder<EntityModelLayer, TexturedModelData> builder
  ) {
    builder.put(HORSESHOE_MODEL_LAYER,
      TexturedModelData.of(HorseEntityModel.getModelData(new Dilation(0.1F)),
        64,
        64
      )
    );
  }
}
