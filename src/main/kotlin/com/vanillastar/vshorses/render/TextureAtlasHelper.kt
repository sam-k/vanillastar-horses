package com.vanillastar.vshorses.render

import com.vanillastar.vshorses.utils.getModIdentifier
import net.minecraft.util.Identifier

/** Builds the ID for the generated texture atlas file corresponding to the given atlas ID. */
fun getTextureAtlasId(atlasId: Identifier) = getModIdentifier("textures/atlas/${atlasId.path}.png")
