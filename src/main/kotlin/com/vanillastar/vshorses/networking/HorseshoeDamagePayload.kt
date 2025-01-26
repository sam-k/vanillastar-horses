package com.vanillastar.vshorses.networking

import com.vanillastar.vshorses.entity.VSHorseEntity
import com.vanillastar.vshorses.utils.getModIdentifier
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.passive.AbstractHorseEntity
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload

data class HorseshoeDamagePayload(val entityId: Int) :
    ModNetworkingPayload.Client<HorseshoeDamagePayload> {
  companion object : ModNetworkingPayload.ClientCompanion<HorseshoeDamagePayload> {
    val DAMAGE_HORSESHOE_PACKET_ID = getModIdentifier("damage_horseshoe")

    override val id: CustomPayload.Id<HorseshoeDamagePayload> =
        CustomPayload.Id(DAMAGE_HORSESHOE_PACKET_ID)

    override val codec: PacketCodec<RegistryByteBuf, HorseshoeDamagePayload> =
        PacketCodec.tuple(
            PacketCodecs.INTEGER,
            HorseshoeDamagePayload::entityId,
            ::HorseshoeDamagePayload,
        )

    override val callback: ModNetworkingServerCallback<HorseshoeDamagePayload> =
        { payload, context ->
          {
            val entity = context.player().world.getEntityById(payload.entityId)
            if (entity is AbstractHorseEntity && entity is VSHorseEntity) {
              entity.`vshorses$getHorseshoeInventory`().stack.damage(1, entity, EquipmentSlot.FEET)
            }
          }
        }
  }

  override val companion = Companion

  override fun getId(): CustomPayload.Id<HorseshoeDamagePayload> = companion.id
}
