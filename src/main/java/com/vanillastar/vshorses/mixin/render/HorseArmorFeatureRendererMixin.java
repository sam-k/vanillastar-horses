package com.vanillastar.vshorses.mixin.render;

import com.vanillastar.vshorses.render.HorseArmorEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HorseArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

import static com.vanillastar.vshorses.render.HorseArmorTrimsRenderLayerKt.HORSE_ARMOR_TRIMS_ATLAS_TEXTURE;
import static com.vanillastar.vshorses.utils.IdentiferHelperKt.getModIdentifier;

@Mixin(HorseArmorFeatureRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class HorseArmorFeatureRendererMixin extends FeatureRenderer<HorseEntity, HorseEntityModel<HorseEntity>> {
  @Unique
  private static final BakedModelManager MODEL_MANAGER =
    MinecraftClient.getInstance().getBakedModelManager();

  @Unique
  private static final Function<ArmorTrim, Identifier> TRIM_MODEL_ID_GETTER =
    Util.memoize(trim -> getModIdentifier(String.format(
      "trims/models/horse_armor/%s_%s",
      trim.getPattern().value().assetId().getPath(),
      trim.getMaterial().value().assetName()
    )));

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

  @Unique
  private void renderTrim(
    @NotNull ArmorTrim trim,
    MatrixStack matrices,
    @NotNull VertexConsumerProvider vertexConsumers,
    int light
  ) {
    @Nullable SpriteAtlasTexture horseArmorTrimsAtlas =
      MODEL_MANAGER.getAtlas(HORSE_ARMOR_TRIMS_ATLAS_TEXTURE);
    if (horseArmorTrimsAtlas == null) {
      return;
    }

    Sprite sprite =
      horseArmorTrimsAtlas.getSprite(TRIM_MODEL_ID_GETTER.apply(trim));
    VertexConsumer vertexConsumer =
      vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(
        HORSE_ARMOR_TRIMS_ATLAS_TEXTURE));
    this.model.render(
      matrices,
      sprite.getTextureSpecificVertexConsumer(vertexConsumer),
      light,
      OverlayTexture.DEFAULT_UV
    );
  }

  @Unique
  private void renderGlint(
    MatrixStack matrices,
    @NotNull VertexConsumerProvider vertexConsumers,
    int light
  ) {
    this.model.render(
      matrices,
      vertexConsumers.getBuffer(RenderLayer.getEntityGlint()),
      light,
      OverlayTexture.DEFAULT_UV
    );
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
    ItemStack itemStack = horseEntity.getBodyArmor();
    if (!(itemStack.getItem() instanceof AnimalArmorItem animalArmorItem) ||
      animalArmorItem.getType() != AnimalArmorItem.Type.EQUESTRIAN) {
      return;
    }

    ArmorTrim armorTrim = itemStack.get(DataComponentTypes.TRIM);
    if (armorTrim != null) {
      this.renderTrim(armorTrim, matrices, vertexConsumers, light);
    }

    // This fixes https://bugs.mojang.com/browse/MC-16829.
    if (horseEntity.getBodyArmor().hasGlint()) {
      this.renderGlint(matrices, vertexConsumers, light);
    }
  }
}
