package com.vanillastar.vshorses.render

import com.vanillastar.vshorses.utils.getModIdentifier
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.model.Dilation
import net.minecraft.client.model.ModelData
import net.minecraft.client.model.ModelPart
import net.minecraft.client.model.ModelPartBuilder
import net.minecraft.client.model.ModelTransform
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.client.render.entity.model.EntityModelPartNames
import net.minecraft.client.render.entity.model.HorseEntityModel
import net.minecraft.entity.passive.AbstractHorseEntity

/**
 * Entity model for horseshoes.
 *
 * Most of the [ModelData] is copied from that of [HorseEntityModel], with
 * omissions of parts not applicable to horseshoes.
 */
@Environment(EnvType.CLIENT)
// Workaround for https://youtrack.jetbrains.com/issue/KT-12993.
@Suppress("ACCIDENTAL_OVERRIDE")
class HorseshoeEntityModel<TEntity: AbstractHorseEntity>(root: ModelPart):
  HorseEntityModel<TEntity>(root) {
  companion object {
    @JvmField
    val HORSESHOE_MODEL =
      EntityModelLayer(getModIdentifier("horseshoe"), "main")

    @Override
    @JvmStatic
    fun getModelData(dilation: Dilation): ModelData {
      val modelData = ModelData()
      val root = modelData.root

      addEmptyChildren(
        addEmptyChild(root, "head_parts"), listOf(
          EntityModelPartNames.HEAD,
          "left_saddle_mouth",
          "right_saddle_mouth",
          "left_saddle_line",
          "right_saddle_line",
          "head_saddle",
          "mouth_saddle_wrap"
        )
      )

      addEmptyChildren(
        addEmptyChild(root, EntityModelPartNames.BODY),
        listOf(EntityModelPartNames.TAIL, "saddle")
      )

      // Horse's legs, with `offsetY` increased by 10 and `sizeY` set to 1.
      // This positions the 1 px-high horseshoes on the 11 px-high legs.
      root.addChild(
        EntityModelPartNames.LEFT_FRONT_LEG,
        ModelPartBuilder.create()
          .uv(48, 21)
          .mirrored()
          .cuboid(-3.0f, -1.01f + 10.0f, -1.9f, 4.0f, 1.0f, 4.0f, dilation),
        ModelTransform.pivot(4.0f, 14.0f, -12.0f)
      )
      root.addChild(
        EntityModelPartNames.RIGHT_FRONT_LEG,
        ModelPartBuilder.create()
          .uv(48, 21)
          .cuboid(-1.0f, -1.01f + 10.0f, -1.9f, 4.0f, 1.0f, 4.0f, dilation),
        ModelTransform.pivot(-4.0f, 14.0f, -12.0f)
      )
      root.addChild(
        EntityModelPartNames.LEFT_HIND_LEG,
        ModelPartBuilder.create()
          .uv(48, 21)
          .mirrored()
          .cuboid(-3.0f, -1.01f + 10.0f, -1.0f, 4.0f, 1.0f, 4.0f, dilation),
        ModelTransform.pivot(4.0f, 14.0f, 7.0f)
      )
      root.addChild(
        EntityModelPartNames.RIGHT_HIND_LEG,
        ModelPartBuilder.create()
          .uv(48, 21)
          .cuboid(-1.0f, -1.01f + 10.0f, -1.0f, 4.0f, 1.0f, 4.0f, dilation),
        ModelTransform.pivot(-4.0f, 14.0f, 7.0f)
      )

      // Foal's legs, with `offsetY` increased by 10 and `sizeY` set to 1.
      // This positions the 1 px-high horseshoes on the 11 px-high legs.
      val babyDilation = dilation.add(0.0f, 5.5f, 0.0f)
      root.addChild(
        "left_front_baby_leg",
        ModelPartBuilder.create()
          .uv(48, 21)
          .mirrored()
          .cuboid(-3.0f, -1.01f + 10.0f, -1.9f, 4.0f, 1.0f, 4.0f, babyDilation),
        ModelTransform.pivot(4.0f, 14.0f, -12.0f)
      )
      root.addChild(
        "right_front_baby_leg",
        ModelPartBuilder.create()
          .uv(48, 21)
          .cuboid(-1.0f, -1.01f + 10.0f, -1.9f, 4.0f, 1.0f, 4.0f, babyDilation),
        ModelTransform.pivot(-4.0f, 14.0f, -12.0f)
      )
      root.addChild(
        "left_hind_baby_leg",
        ModelPartBuilder.create()
          .uv(48, 21)
          .mirrored()
          .cuboid(-3.0f, -1.01f + 10.0f, -1.0f, 4.0f, 1.0f, 4.0f, babyDilation),
        ModelTransform.pivot(4.0f, 14.0f, 7.0f)
      )
      root.addChild(
        "right_hind_baby_leg",
        ModelPartBuilder.create()
          .uv(48, 21)
          .cuboid(-1.0f, -1.01f + 10.0f, -1.0f, 4.0f, 1.0f, 4.0f, babyDilation),
        ModelTransform.pivot(-4.0f, 14.0f, 7.0f)
      )

      return modelData
    }
  }
}
