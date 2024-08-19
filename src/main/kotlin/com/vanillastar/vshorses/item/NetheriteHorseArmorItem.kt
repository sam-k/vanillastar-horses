package com.vanillastar.vshorses.item

import com.vanillastar.vshorses.utils.getModIdentifier
import net.minecraft.item.AnimalArmorItem
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemGroups
import net.minecraft.registry.RegistryKey

abstract class NetheriteHorseArmorItem :
    ModItem,
    AnimalArmorItem(
        ArmorMaterials.NETHERITE,
        Type.EQUESTRIAN,
        false,
        // Same durability as netherite armor.
        Settings().maxDamage(ArmorItem.Type.BODY.getMaxDamage(37)),
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
