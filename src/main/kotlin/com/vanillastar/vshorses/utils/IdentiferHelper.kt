package com.vanillastar.vshorses.utils

import com.vanillastar.vshorses.MOD_ID
import net.minecraft.util.Identifier

/** Creates an ID within this mod's namespace. */
fun getModIdentifier(name: String): Identifier = Identifier.of(MOD_ID, name)
