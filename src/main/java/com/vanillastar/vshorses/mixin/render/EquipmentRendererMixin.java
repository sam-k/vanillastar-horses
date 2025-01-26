package com.vanillastar.vshorses.mixin.render;

import static com.vanillastar.vshorses.render.HorseArmorTrimAtlasKt.HORSE_ARMOR_TRIM_ENTITY_ATLAS;
import static com.vanillastar.vshorses.render.TextureAtlasHelperKt.getTextureAtlasId;
import static com.vanillastar.vshorses.utils.IdentiferHelperKt.getModIdentifier;

import com.llamalad7.mixinextras.sugar.Local;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.equipment.EquipmentRenderer;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.EquipmentModel;
import net.minecraft.item.equipment.trim.ArmorTrim;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(EquipmentRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class EquipmentRendererMixin {
  @Unique
  private static final BakedModelManager MODEL_MANAGER =
      MinecraftClient.getInstance().getBakedModelManager();

  @Unique
  private static final Identifier HORSE_ARMOR_TRIM_ENTITY_ATLAS_TEXTURE =
      getTextureAtlasId(HORSE_ARMOR_TRIM_ENTITY_ATLAS);

  @Unique
  private static final Function<ArmorTrim, Identifier> TRIM_MODEL_ID_GETTER =
      Util.memoize(trim -> getModIdentifier(String.format(
          "trims/entity/horse_body/%s_%s",
          trim.pattern().value().assetId().getPath(), trim.material().value().assetName())));

  @ModifyArgs(
      method =
          "render(Lnet/minecraft/item/equipment/EquipmentModel$LayerType;Lnet/minecraft/util/Identifier;Lnet/minecraft/client/model/Model;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/util/Identifier;)V",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/client/model/Model;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V"))
  private void renderHorseArmorTrim(
      Args args,
      @Local(ordinal = 0, argsOnly = true) EquipmentModel.LayerType layerType,
      @Local(ordinal = 0, argsOnly = true) ItemStack stack,
      @Local(ordinal = 0, argsOnly = true) VertexConsumerProvider vertexConsumers) {
    if (layerType != EquipmentModel.LayerType.HORSE_BODY) {
      return;
    }

    ArmorTrim armorTrim = stack.get(DataComponentTypes.TRIM);
    if (armorTrim == null) {
      return;
    }

    SpriteAtlasTexture horseArmorTrimsAtlas =
        MODEL_MANAGER.getAtlas(HORSE_ARMOR_TRIM_ENTITY_ATLAS_TEXTURE);
    if (horseArmorTrimsAtlas == null) {
      return;
    }

    args.set(
        1, // `VertexConsumer vertices`
        horseArmorTrimsAtlas
            .getSprite(TRIM_MODEL_ID_GETTER.apply(armorTrim))
            .getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(
                RenderLayer.getArmorCutoutNoCull(HORSE_ARMOR_TRIM_ENTITY_ATLAS_TEXTURE))));
  }
}
