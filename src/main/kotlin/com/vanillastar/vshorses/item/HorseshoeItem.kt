package com.vanillastar.vshorses.item

import com.google.common.collect.ImmutableSet
import com.vanillastar.vshorses.entity.VSHorseEntity
import com.vanillastar.vshorses.entity.isHorselike
import com.vanillastar.vshorses.utils.getModIdentifier
import kotlin.jvm.optionals.getOrNull
import net.fabricmc.fabric.api.item.v1.EnchantingContext
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemGroups
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.ItemTags
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.event.GameEvent

/** Modifiers to apply when horseshoe is equipped. */
private val HORSESHOE_ITEM_MODIFIERS =
    AttributeModifiersComponent.builder()
        .add(
            EntityAttributes.GENERIC_MOVEMENT_SPEED,
            EntityAttributeModifier(
                getModIdentifier("horseshoe_speed_boost"),
                0.15,
                EntityAttributeModifier.Operation.ADD_VALUE,
            ),
            AttributeModifierSlot.FEET,
        )
        .build()

abstract class HorseshoeItem :
    ModItem, Item(Settings().maxDamage(195).attributeModifiers(HORSESHOE_ITEM_MODIFIERS)) {
  override val id = getModIdentifier("horseshoe")
  override val itemGroup: RegistryKey<ItemGroup> = ItemGroups.COMBAT

  /** Equipment slot this item belongs in. */
  val equipmentSlot = EquipmentSlot.FEET
}

@JvmField
val HORSESHOE_ITEM =
    object : HorseshoeItem() {
      /** Enchantments applicable to boots but prohibited on horseshoes. */
      private val PROHIBITED_BOOTS_ENCHANTMENTS =
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
          hand: Hand?,
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
        return ActionResult.success(user.world.isClient)
      }

      override fun canRepair(stack: ItemStack, ingredient: ItemStack) =
          ingredient.isOf(Items.COPPER_INGOT)

      override fun isEnchantable(stack: ItemStack) = true

      override fun getEnchantability() = 9 // Same as iron armor

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
        if (PROHIBITED_BOOTS_ENCHANTMENTS.contains(enchantment.key.getOrNull())) {
          return false
        }

        val definition = enchantment.value().definition()
        return (when (context) {
              EnchantingContext.PRIMARY ->
                  definition.primaryItems().orElse(definition.supportedItems())
              EnchantingContext.ACCEPTABLE -> definition.supportedItems()
            })
            .stream()
            .anyMatch { entry -> entry.isIn(ItemTags.FOOT_ARMOR_ENCHANTABLE) }
      }
    }
