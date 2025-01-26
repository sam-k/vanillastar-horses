package com.vanillastar.vshorses.networking

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload

typealias ModNetworkingServerCallback<TPayload> =
    (TPayload, ServerPlayNetworking.Context) -> () -> Unit

sealed interface ModNetworkingPayload : CustomPayload {
  interface Companion<TPayload : ModNetworkingPayload> {
    val id: CustomPayload.Id<TPayload>
    val codec: PacketCodec<RegistryByteBuf, TPayload>
  }

  interface ClientCompanion<TPayload : ModNetworkingPayload> : Companion<TPayload> {
    val callback: ModNetworkingServerCallback<TPayload>
  }

  interface Client<TPayload : ModNetworkingPayload> : ModNetworkingPayload {
    val companion: ClientCompanion<TPayload>
  }
}
