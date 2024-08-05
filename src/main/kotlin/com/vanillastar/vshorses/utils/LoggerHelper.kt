package com.vanillastar.vshorses.utils

import com.vanillastar.vshorses.MOD_ID
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/** Creates a [Logger] for logging within the mod. */
fun getLogger(): Logger = LoggerFactory.getLogger(MOD_ID)

/** Creates a [Logger] for logging within the mod's mixins. */
fun getMixinLogger(): Logger = LoggerFactory.getLogger("$MOD_ID/Mixins")
