package com.vanillastar.vshorses.item

import com.vanillastar.vshorses.utils.getModIdentifier
import net.minecraft.item.*
import net.minecraft.registry.RegistryKey

abstract class NetheriteHorseArmorItem :
    ModItem,
    AnimalArmorItem(
        ArmorMaterials.NETHERITE,
        Type.EQUESTRIAN,
        false,
        Settings()
            .maxDamage(
                @Suppress("RemoveRedundantQualifierName")
                ArmorItem.Type.BODY.getMaxDamage(37) // Same durability as netherite
            )
            .fireproof(),
    ) {
  override val id = getModIdentifier("netherite_horse_armor")
  override val itemGroup: RegistryKey<ItemGroup> = ItemGroups.COMBAT
}

@JvmField
val NETHERITE_HORSE_ARMOR_ITEM =
    object : NetheriteHorseArmorItem() {
      override fun getEntityTexture() =
          getModIdentifier("textures/entity/horse/armor/horse_armor_netherite.png")
    }
