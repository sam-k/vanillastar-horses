package com.vanillastar.vshorses.render

import com.vanillastar.vshorses.render.HorseshoeEntityModel.Companion.HORSESHOE_BABY_MODEL
import com.vanillastar.vshorses.render.HorseshoeEntityModel.Companion.HORSESHOE_MODEL
import kotlin.jvm.optionals.getOrNull
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.equipment.EquipmentRenderer
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.AbstractHorseEntityModel
import net.minecraft.client.render.entity.model.EntityModelLoader
import net.minecraft.client.render.entity.model.HorseEntityModel
import net.minecraft.client.render.entity.state.LivingHorseEntityRenderState
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.equipment.EquipmentModel

@Environment(EnvType.CLIENT)
class HorseshoeFeatureRenderer<
    TState : LivingHorseEntityRenderState,
    TModel : AbstractHorseEntityModel<TState>,
>(
    context: FeatureRendererContext<TState, TModel>,
    loader: EntityModelLoader,
    private val equipmentRenderer: EquipmentRenderer,
) : FeatureRenderer<TState, TModel>(context) {
  private val model = HorseEntityModel(loader.getModelPart(HORSESHOE_MODEL))
  private val babyModel = HorseEntityModel(loader.getModelPart(HORSESHOE_BABY_MODEL))

  override fun render(
      matrices: MatrixStack,
      vertexConsumers: VertexConsumerProvider,
      light: Int,
      state: TState,
      limbAngle: Float,
      limbDistance: Float,
  ) {
    if (state !is VSHorseEntityRenderState || state.`vshorses$getHorseshoe`().isEmpty) {
      return
    }

    val itemStack = state.`vshorses$getHorseshoe`()
    val horseshoeModelId = itemStack.get(DataComponentTypes.EQUIPPABLE)?.model?.getOrNull()
    if (horseshoeModelId == null) {
      return
    }

    val entityModel = if (state.baby) this.babyModel else this.model
    entityModel.setAngles(state)
    this.equipmentRenderer.render(
        EquipmentModel.LayerType.HORSE_BODY,
        horseshoeModelId,
        entityModel,
        itemStack,
        matrices,
        vertexConsumers,
        light,
    )
  }
}
