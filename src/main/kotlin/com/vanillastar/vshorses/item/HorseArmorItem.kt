package com.vanillastar.vshorses.item

import com.vanillastar.vshorses.utils.getModIdentifier
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey

/** Tag containing all horse armor items. */
@JvmField
val HORSE_ARMOR: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, getModIdentifier("horse_armor"))
