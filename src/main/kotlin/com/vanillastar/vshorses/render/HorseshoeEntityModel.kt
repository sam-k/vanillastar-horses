package com.vanillastar.vshorses.render

import com.vanillastar.vshorses.utils.getModIdentifier
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.model.*
import net.minecraft.client.render.entity.model.AbstractHorseEntityModel
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.client.render.entity.model.EntityModelPartNames
import net.minecraft.client.render.entity.state.LivingHorseEntityRenderState

/**
 * Entity model for horseshoes.
 *
 * Most of the [ModelData] is copied from that of [AbstractHorseEntityModel], with omissions of
 * parts not applicable to horseshoes (i.e., all non-leg parts).
 */
@Environment(EnvType.CLIENT)
@Suppress("ACCIDENTAL_OVERRIDE") // Workaround for https://youtrack.jetbrains.com/issue/KT-12993
class HorseshoeEntityModel<TState : LivingHorseEntityRenderState>(root: ModelPart) :
    AbstractHorseEntityModel<TState>(root) {
  companion object {
    /** [EntityModelLayer] identifier for the horseshoe entity model. */
    @JvmField val HORSESHOE_MODEL = EntityModelLayer(getModIdentifier("horseshoe"), "main")
    /** [EntityModelLayer] identifier for the baby horseshoe entity model. */
    @JvmField
    val HORSESHOE_BABY_MODEL = EntityModelLayer(getModIdentifier("horseshoe_baby"), "main")

    @Override
    @JvmStatic
    fun getModelData(dilation: Dilation): ModelData {
      val modelData = ModelData()
      val root = modelData.root

      val headParts = addEmptyChild(root, "head_parts")
      addEmptyChildren(
          headParts,
          listOf(
              EntityModelPartNames.MANE,
              "upper_mouth",
              "left_saddle_mouth",
              "right_saddle_mouth",
              "left_saddle_line",
              "right_saddle_line",
              "head_saddle",
              "mouth_saddle_wrap",
          ),
      )

      addEmptyChildren(
          addEmptyChild(headParts, EntityModelPartNames.HEAD),
          listOf(EntityModelPartNames.LEFT_EAR, EntityModelPartNames.RIGHT_EAR),
      )

      addEmptyChildren(
          addEmptyChild(root, EntityModelPartNames.BODY),
          listOf(EntityModelPartNames.TAIL, "saddle"),
      )

      // Horse's legs, with `offsetY` increased by 10 and `sizeY` set to 1.
      // This positions the 1 px-high horseshoes on the 11 px-high legs.
      root.addChild(
          EntityModelPartNames.LEFT_FRONT_LEG,
          ModelPartBuilder.create()
              .uv(48, 21)
              .mirrored()
              .cuboid(
                  -3.0f,
                  /* offsetY= */ -1.01f + 10,
                  -1.9f,
                  4.0f,
                  /* sizeY= */ 11.0f - 10,
                  4.0f,
                  dilation,
              ),
          ModelTransform.pivot(4.0f, 14.0f, -10.0f),
      )
      root.addChild(
          EntityModelPartNames.RIGHT_FRONT_LEG,
          ModelPartBuilder.create()
              .uv(48, 21)
              .cuboid(
                  -1.0f,
                  /* offsetY= */ -1.01f + 10,
                  -1.9f,
                  4.0f,
                  /* sizeY= */ 11.0f - 10,
                  4.0f,
                  dilation,
              ),
          ModelTransform.pivot(-4.0f, 14.0f, -10.0f),
      )
      root.addChild(
          EntityModelPartNames.LEFT_HIND_LEG,
          ModelPartBuilder.create()
              .uv(48, 21)
              .mirrored()
              .cuboid(
                  -3.0f,
                  /* offsetY= */ -1.01f + 10,
                  -1.0f,
                  4.0f,
                  /* sizeY= */ 11.0f - 10,
                  4.0f,
                  dilation,
              ),
          ModelTransform.pivot(4.0f, 14.0f, 7.0f),
      )
      root.addChild(
          EntityModelPartNames.RIGHT_HIND_LEG,
          ModelPartBuilder.create()
              .uv(48, 21)
              .cuboid(
                  -1.0f,
                  /* offsetY= */ -1.01f + 10,
                  -1.0f,
                  4.0f,
                  /* sizeY= */ 11.0f - 10,
                  4.0f,
                  dilation,
              ),
          ModelTransform.pivot(-4.0f, 14.0f, 7.0f),
      )

      return modelData
    }

    @Override
    @JvmStatic
    fun getBabyHorseModelData(dilation: Dilation): ModelData =
        BABY_TRANSFORMER.apply(this.getBabyModelData(dilation))

    @Override
    @JvmStatic
    fun getBabyModelData(dilation: Dilation): ModelData {
      val modelData = getModelData(dilation)
      val root = modelData.root

      // Horseshoes are 1 px tall. So for `radiusY`, instead of 50% * 11.0 = 5.5, we have 50% * 1.0.
      val babyDilation = dilation.add(0.0f, /* radiusY= */ 1.0f / 2, 0.0f)

      // Foal's legs, with `offsetY` increased by 150% * 10 = 15 and `sizeY` set to 1.
      // This positions the 1 px-high horseshoes on the 11 px-high legs.
      root.addChild(
          EntityModelPartNames.LEFT_FRONT_LEG,
          ModelPartBuilder.create()
              .uv(48, 21)
              .mirrored()
              .cuboid(
                  -3.0f,
                  /* offsetY= */ -1.01f + 15,
                  -1.9f,
                  4.0f,
                  /* sizeY= */ 1.0f,
                  4.0f,
                  babyDilation,
              ),
          ModelTransform.pivot(4.0f, 14.0f, -10.0f),
      )
      root.addChild(
          EntityModelPartNames.RIGHT_FRONT_LEG,
          ModelPartBuilder.create()
              .uv(48, 21)
              .cuboid(
                  -1.0f,
                  /* offsetY= */ -1.01f + 15,
                  -1.9f,
                  4.0f,
                  /* sizeY= */ 1.0f,
                  4.0f,
                  babyDilation,
              ),
          ModelTransform.pivot(-4.0f, 14.0f, -10.0f),
      )
      root.addChild(
          EntityModelPartNames.LEFT_HIND_LEG,
          ModelPartBuilder.create()
              .uv(48, 21)
              .mirrored()
              .cuboid(
                  -3.0f,
                  /* offsetY= */ -1.01f + 15,
                  -1.0f,
                  4.0f,
                  /* sizeY= */ 1.0f,
                  4.0f,
                  babyDilation,
              ),
          ModelTransform.pivot(4.0f, 14.0f, 7.0f),
      )
      root.addChild(
          EntityModelPartNames.RIGHT_HIND_LEG,
          ModelPartBuilder.create()
              .uv(48, 21)
              .cuboid(
                  -1.0f,
                  /* offsetY= */ -1.01f + 15,
                  -1.0f,
                  4.0f,
                  /* sizeY= */ 1.0f,
                  4.0f,
                  babyDilation,
              ),
          ModelTransform.pivot(-4.0f, 14.0f, 7.0f),
      )

      return modelData
    }
  }
}
