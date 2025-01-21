package com.vanillastar.vshorses.item

import net.minecraft.item.AnimalArmorItem
import net.minecraft.item.ItemGroups
import net.minecraft.item.equipment.ArmorMaterials
import net.minecraft.sound.SoundEvents

val NETHERITE_HORSE_ARMOR_ITEM_METADATA =
    ModItemMetadata("netherite_horse_armor", ItemGroups.COMBAT) {
      ArmorMaterials.NETHERITE.applyBodyArmorSettings(
          it.fireproof().enchantable(ArmorMaterials.NETHERITE.enchantmentValue),
          SoundEvents.ENTITY_HORSE_ARMOR,
          /* damageOnHurt = */ true,
          AnimalArmorItem.Type.EQUESTRIAN.allowedEntities,
      )
    }

class NetheriteHorseArmorItem(settings: Settings) :
    AnimalArmorItem(
        ArmorMaterials.NETHERITE,
        Type.EQUESTRIAN,
        SoundEvents.ENTITY_HORSE_ARMOR,
        /* damageOnHurt= */ true,
        settings,
    )
