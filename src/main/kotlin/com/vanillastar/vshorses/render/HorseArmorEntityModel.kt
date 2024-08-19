package com.vanillastar.vshorses.render

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.model.Dilation
import net.minecraft.client.model.ModelData
import net.minecraft.client.model.ModelPart
import net.minecraft.client.model.ModelPartBuilder
import net.minecraft.client.model.ModelTransform
import net.minecraft.client.render.entity.model.EntityModelPartNames
import net.minecraft.client.render.entity.model.HorseEntityModel
import net.minecraft.entity.passive.AbstractHorseEntity

/**
 * Entity model for horse armor.
 *
 * Most of the [ModelData] is copied from that of [HorseEntityModel], with omissions of parts not
 * applicable to horse armor (i.e., all saddle parts), and dilation applied to all parts so that no
 * horse armor part is flush with the horse itself. This prevents horse armor glint from being
 * applied to the entire horse.
 */
@Environment(EnvType.CLIENT)
// Workaround for https://youtrack.jetbrains.com/issue/KT-12993.
@Suppress("ACCIDENTAL_OVERRIDE")
class HorseArmorEntityModel<TEntity : AbstractHorseEntity>(root: ModelPart) :
    HorseEntityModel<TEntity>(root) {
  companion object {
    @Override
    @JvmStatic
    fun getModelData(dilation: Dilation): ModelData {
      val modelData = ModelData()
      val root = modelData.root

      // Horse's head parts.
      val headParts =
          root.addChild(
              "head_parts",
              ModelPartBuilder.create()
                  .uv(0, 35)
                  .cuboid(-2.05f, -6.0f, -2.0f, 4.0f, 12.0f, 7.0f, dilation),
              ModelTransform.of(0.0f, 4.0f, -12.0f, (Math.PI / 6).toFloat(), 0.0f, 0.0f),
          )
      headParts.addChild(
          EntityModelPartNames.MANE,
          ModelPartBuilder.create()
              .uv(56, 36)
              .cuboid(-1.0f, -11.0f, 5.01f, 2.0f, 16.0f, 2.0f, dilation),
          ModelTransform.NONE,
      )
      headParts.addChild(
          "upper_mouth",
          ModelPartBuilder.create()
              .uv(0, 25)
              .cuboid(-2.0f, -11.0f, -7.0f, 4.0f, 5.0f, 5.0f, dilation),
          ModelTransform.NONE,
      )
      addEmptyChildren(
          headParts,
          listOf(
              "left_saddle_mouth",
              "right_saddle_mouth",
              "left_saddle_line",
              "right_saddle_line",
              "head_saddle",
              "mouth_saddle_wrap",
          ),
      )

      val head =
          headParts.addChild(
              EntityModelPartNames.HEAD,
              ModelPartBuilder.create()
                  .uv(0, 13)
                  .cuboid(-3.0f, -11.0f, -2.0f, 6.0f, 5.0f, 7.0f, dilation),
              ModelTransform.NONE,
          )
      head.addChild(
          EntityModelPartNames.LEFT_EAR,
          ModelPartBuilder.create()
              .uv(19, 16)
              .cuboid(0.55f, -13.0f, 4.0f, 2.0f, 3.0f, 1.0f, dilation.add(-0.001f)),
          ModelTransform.NONE,
      )
      head.addChild(
          EntityModelPartNames.RIGHT_EAR,
          ModelPartBuilder.create()
              .uv(19, 16)
              .cuboid(-2.55f, -13.0f, 4.0f, 2.0f, 3.0f, 1.0f, dilation.add(-0.001f)),
          ModelTransform.NONE,
      )

      // Horse's body. Dilation is added.
      val body =
          root.addChild(
              EntityModelPartNames.BODY,
              ModelPartBuilder.create()
                  .uv(0, 32)
                  .cuboid(-5.0f, -8.0f, -17.0f, 10.0f, 10.0f, 22.0f, dilation.add(0.05f)),
              ModelTransform.pivot(0.0f, 11.0f, 5.0f),
          )
      body.addChild(
          EntityModelPartNames.TAIL,
          ModelPartBuilder.create()
              .uv(42, 36)
              .cuboid(-1.5f, 0.0f, 0.0f, 3.0f, 14.0f, 4.0f, dilation),
          ModelTransform.of(0.0f, -5.0f, 2.0f, (Math.PI / 6).toFloat(), 0.0f, 0.0f),
      )
      addEmptyChild(body, "saddle")

      // Horse's legs. `sizeY` is decreased by 1 to allow room for horseshoes.
      root.addChild(
          EntityModelPartNames.LEFT_FRONT_LEG,
          ModelPartBuilder.create()
              .uv(48, 21)
              .mirrored()
              .cuboid(-3.0f, -1.01f, -1.9f, 4.0f, 10.0f, 4.0f, dilation),
          ModelTransform.pivot(4.0f, 14.0f, -12.0f),
      )
      root.addChild(
          EntityModelPartNames.RIGHT_FRONT_LEG,
          ModelPartBuilder.create()
              .uv(48, 21)
              .cuboid(-1.0f, -1.01f, -1.9f, 4.0f, 10.0f, 4.0f, dilation),
          ModelTransform.pivot(-4.0f, 14.0f, -12.0f),
      )
      root.addChild(
          EntityModelPartNames.LEFT_HIND_LEG,
          ModelPartBuilder.create()
              .uv(48, 21)
              .mirrored()
              .cuboid(-3.0f, -1.01f, -1.0f, 4.0f, 10.0f, 4.0f, dilation),
          ModelTransform.pivot(4.0f, 14.0f, 7.0f),
      )
      root.addChild(
          EntityModelPartNames.RIGHT_HIND_LEG,
          ModelPartBuilder.create()
              .uv(48, 21)
              .cuboid(-1.0f, -1.01f, -1.0f, 4.0f, 10.0f, 4.0f, dilation),
          ModelTransform.pivot(-4.0f, 14.0f, 7.0f),
      )

      // Foal's legs. `sizeY` is decreased by 1 to allow room for horseshoes.
      val babyDilation = dilation.add(0.0f, 5.5f, 0.0f)
      root.addChild(
          "left_front_baby_leg",
          ModelPartBuilder.create()
              .uv(48, 21)
              .mirrored()
              .cuboid(-3.0f, -1.01f, -1.9f, 4.0f, 10.0f, 4.0f, babyDilation),
          ModelTransform.pivot(4.0f, 14.0f, -12.0f),
      )
      root.addChild(
          "right_front_baby_leg",
          ModelPartBuilder.create()
              .uv(48, 21)
              .cuboid(-1.0f, -1.01f, -1.9f, 4.0f, 10.0f, 4.0f, babyDilation),
          ModelTransform.pivot(-4.0f, 14.0f, -12.0f),
      )
      root.addChild(
          "left_hind_baby_leg",
          ModelPartBuilder.create()
              .uv(48, 21)
              .mirrored()
              .cuboid(-3.0f, -1.01f, -1.0f, 4.0f, 10.0f, 4.0f, babyDilation),
          ModelTransform.pivot(4.0f, 14.0f, 7.0f),
      )
      root.addChild(
          "right_hind_baby_leg",
          ModelPartBuilder.create()
              .uv(48, 21)
              .cuboid(-1.0f, -1.01f, -1.0f, 4.0f, 10.0f, 4.0f, babyDilation),
          ModelTransform.pivot(-4.0f, 14.0f, 7.0f),
      )

      return modelData
    }
  }
}
