package com.vanillastar.vshorses.mixin.render;

import com.vanillastar.vshorses.render.HorseArmorEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HorseArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.HorseEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseArmorFeatureRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class HorseArmorFeatureRendererMixin extends FeatureRenderer<HorseEntity, HorseEntityModel<HorseEntity>> {
  @Shadow
  @Final
  @Mutable
  private HorseEntityModel<HorseEntity> model;

  private HorseArmorFeatureRendererMixin(
    FeatureRendererContext<HorseEntity, HorseEntityModel<HorseEntity>> context,
    @NotNull EntityModelLoader loader
  ) {
    super(context);
    this.model =
      new HorseArmorEntityModel<>(loader.getModelPart(EntityModelLayers.HORSE_ARMOR));
  }

  @Inject(
    method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/render/entity/model/HorseEntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V",
      shift = At.Shift.AFTER
    )
  )
  private void renderGlint(
    MatrixStack matrices,
    VertexConsumerProvider vertexConsumers,
    int light,
    @NotNull HorseEntity horseEntity,
    float limbAngle,
    float limbDistance,
    float tickDelta,
    float animationProgress,
    float headYaw,
    float headPitch,
    CallbackInfo ci
  ) {
    // This fixes https://bugs.mojang.com/browse/MC-16829.
    if (horseEntity.getBodyArmor().hasGlint()) {
      this.model.render(
        matrices,
        vertexConsumers.getBuffer(RenderLayer.getEntityGlint()),
        light,
        OverlayTexture.DEFAULT_UV
      );
    }
  }
}
