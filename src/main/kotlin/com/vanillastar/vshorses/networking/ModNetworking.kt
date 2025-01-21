package com.vanillastar.vshorses.networking

import com.vanillastar.vshorses.utils.ModRegistry
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

abstract class ModNetworking : ModRegistry() {
  private val clientPayloads = listOf(HorseshoeDamagePayload)

  private fun <TPayload : ModNetworkingPayload> registerClientPayload(
      payloadCompanion: ModNetworkingPayload.ClientCompanion<TPayload>
  ) {
    PayloadTypeRegistry.playC2S().register(payloadCompanion.id, payloadCompanion.codec)
    logger.info("Registered client payload {}", payloadCompanion.id.id)
  }

  private fun <TPayload : ModNetworkingPayload> registerServerReceiver(
      payloadCompanion: ModNetworkingPayload.ClientCompanion<TPayload>
  ) {
    ServerPlayNetworking.registerGlobalReceiver(payloadCompanion.id) { payload, context ->
      context.server().execute(payloadCompanion.callback(payload, context))
    }
    logger.info("Registered server receiver for client payload {}", payloadCompanion.id.id)
  }

  override fun initialize() {
    clientPayloads.forEach {
      registerClientPayload(it)
      registerServerReceiver(it)
    }
  }
}

@JvmField val MOD_NETWORKING = object : ModNetworking() {}
