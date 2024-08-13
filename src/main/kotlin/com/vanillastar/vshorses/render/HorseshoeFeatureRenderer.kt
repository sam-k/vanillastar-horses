package com.vanillastar.vshorses.render

import com.vanillastar.vshorses.entity.VSHorseEntity
import com.vanillastar.vshorses.entity.isHorselike
import com.vanillastar.vshorses.render.HorseshoeEntityModel.Companion.HORSESHOE_MODEL
import com.vanillastar.vshorses.utils.getModIdentifier
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.EntityModelLoader
import net.minecraft.client.render.entity.model.HorseEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.passive.AbstractHorseEntity

@Environment(EnvType.CLIENT)
class HorseshoeFeatureRenderer<TEntity: AbstractHorseEntity, TModel: HorseEntityModel<TEntity>>(
  context: FeatureRendererContext<TEntity, TModel>, loader: EntityModelLoader
): FeatureRenderer<TEntity, TModel>(context) {
  companion object {
    private val HORSESHOE_SKIN_ID =
      getModIdentifier("textures/entity/horse/horseshoe.png")
  }

  private val model =
    HorseEntityModel<TEntity>(loader.getModelPart(HORSESHOE_MODEL))

  override fun render(
    matrices: MatrixStack,
    vertexConsumers: VertexConsumerProvider,
    light: Int,
    entity: TEntity,
    limbAngle: Float,
    limbDistance: Float,
    tickDelta: Float,
    animationProgress: Float,
    headYaw: Float,
    headPitch: Float
  ) {
    if (!isHorselike(entity) || entity !is VSHorseEntity || !entity.`vshorses$isShoed`()) {
      return
    }

    this.contextModel.copyStateTo(this.model)
    this.model.animateModel(entity, limbAngle, limbDistance, tickDelta)
    this.model.setAngles(
      entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch
    )
    this.model.render(
      matrices, vertexConsumers.getBuffer(
        RenderLayer.getEntityCutoutNoCull(HORSESHOE_SKIN_ID)
      ), light, OverlayTexture.DEFAULT_UV
    )
    if (entity.`vshorses$getHorseshoeInventory`().stack.hasGlint()) {
      this.model.render(
        matrices,
        vertexConsumers.getBuffer(RenderLayer.getEntityGlint()),
        light,
        OverlayTexture.DEFAULT_UV
      )
    }
  }
}

