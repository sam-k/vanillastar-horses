package com.vanillastar.vshorses.utils

abstract class ModRegistry {
  val logger = getLogger()

  abstract fun initialize()
}
