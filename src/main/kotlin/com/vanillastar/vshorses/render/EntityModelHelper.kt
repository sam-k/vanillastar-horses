package com.vanillastar.vshorses.render

import net.minecraft.client.model.ModelPartBuilder
import net.minecraft.client.model.ModelPartData
import net.minecraft.client.model.ModelTransform

/** Adds an empty placeholder child to a parent model part. */
fun addEmptyChild(parent: ModelPartData, childName: String): ModelPartData =
  parent.addChild(
    childName, ModelPartBuilder.create(), ModelTransform.NONE
  )

/** Adds empty placeholder children to a parent model part. */
fun addEmptyChildren(parent: ModelPartData, childNames: Collection<String>) =
  childNames.stream().forEach { addEmptyChild(parent, it) }
