package com.vanillastar.vshorses.item

import com.google.common.collect.ImmutableSet
import com.vanillastar.vshorses.entity.HORSELIKE
import com.vanillastar.vshorses.entity.VSHorseEntity
import com.vanillastar.vshorses.entity.isHorselike
import com.vanillastar.vshorses.sound.MOD_SOUNDS
import com.vanillastar.vshorses.utils.getModIdentifier
import kotlin.jvm.optionals.getOrNull
import net.fabricmc.fabric.api.item.v1.EnchantingContext
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.component.type.EquippableComponent
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.item.equipment.ArmorMaterials
import net.minecraft.item.equipment.EquipmentType
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.ItemTags
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.event.GameEvent

val HORSESHOE_ITEM_METADATA =
    ModItemMetadata("horseshoe", ItemGroups.COMBAT) {
      it.maxCount(1)
          .maxDamage(EquipmentType.BOOTS.getMaxDamage(ArmorMaterials.IRON.durability))
          .repairable(Items.COPPER_INGOT)
          .enchantable(ArmorMaterials.IRON.enchantmentValue)
          .component(
              DataComponentTypes.EQUIPPABLE,
              EquippableComponent.builder(EquipmentSlot.FEET)
                  .equipSound(MOD_SOUNDS.equipHorseshoeSound)
                  .model(getModIdentifier("horseshoe"))
                  .allowedEntities(*HORSELIKE.toTypedArray())
                  .build(),
          )
          .attributeModifiers(
              AttributeModifiersComponent.builder()
                  .add(
                      EntityAttributes.MOVEMENT_SPEED,
                      EntityAttributeModifier(
                          getModIdentifier("horseshoe_speed_boost"),
                          0.15,
                          EntityAttributeModifier.Operation.ADD_VALUE,
                      ),
                      AttributeModifierSlot.FEET,
                  )
                  .build()
          )
    }

class HorseshoeItem(settings: Settings) : Item(settings) {
  /** Enchantments applicable to boots but prohibited on horseshoes. */
  private val prohibitedBootsEnchantments =
      ImmutableSet.of(
          Enchantments.PROTECTION,
          Enchantments.BLAST_PROTECTION,
          Enchantments.FIRE_PROTECTION,
          Enchantments.PROJECTILE_PROTECTION,
          Enchantments.THORNS,
      )

  override fun useOnEntity(
      stack: ItemStack,
      user: PlayerEntity,
      entity: LivingEntity,
      hand: Hand,
  ): ActionResult {
    if (!entity.isAlive || !isHorselike(entity) || entity !is VSHorseEntity) {
      return ActionResult.PASS
    }
    if (!entity.`vshorses$canBeShoed`() || entity.`vshorses$isShoed`()) {
      return ActionResult.PASS
    }

    if (!user.world.isClient) {
      entity.`vshorses$getHorseshoeInventory`().stack = stack.split(1)
      entity.world.emitGameEvent(entity, GameEvent.EQUIP, entity.pos)
    }
    return ActionResult.SUCCESS
  }

  /**
   * Adding enchantable horseshoes functions as a fix for:
   * - [MC-268935](https://bugs.mojang.com/browse/MC-268935)
   * - [MC-268936](https://bugs.mojang.com/browse/MC-268936)
   */
  override fun canBeEnchantedWith(
      stack: ItemStack,
      enchantment: RegistryEntry<Enchantment>,
      context: EnchantingContext,
  ): Boolean {
    if (prohibitedBootsEnchantments.contains(enchantment.key.getOrNull())) {
      return false
    }

    val definition = enchantment.value().definition()
    return (when (context) {
          EnchantingContext.PRIMARY -> definition.primaryItems().orElse(definition.supportedItems())
          EnchantingContext.ACCEPTABLE -> definition.supportedItems()
        })
        .stream()
        .anyMatch { it.isIn(ItemTags.FOOT_ARMOR_ENCHANTABLE) }
  }
}
