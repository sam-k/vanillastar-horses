package com.vanillastar.vshorses.item

import com.google.common.collect.ImmutableSet
import com.vanillastar.vshorses.entity.VSHorseEntity
import com.vanillastar.vshorses.entity.isHorselike
import com.vanillastar.vshorses.utils.getModIdentifier
import net.fabricmc.fabric.api.item.v1.EnchantingContext
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemGroups
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.world.event.GameEvent
import kotlin.jvm.optionals.getOrNull

abstract class HorseshoeItem: ModItem, Item(
  Settings().maxDamage(195)  // Same as iron boots
) {
  override val id = getModIdentifier("horseshoe")
  override val itemGroup: RegistryKey<ItemGroup> = ItemGroups.COMBAT

  /**
   * Creates modifier for making horses faster when horseshoe is equipped.
   */
  fun getSpeedModifierWithId(id: Identifier) = EntityAttributeModifier(
    id, 0.15, EntityAttributeModifier.Operation.ADD_VALUE
  )
}

@JvmField
val HORSESHOE_ITEM = object: HorseshoeItem() {
  private val primaryEnchantments = ImmutableSet.of(
    Enchantments.DEPTH_STRIDER,
    Enchantments.FEATHER_FALLING,
    Enchantments.UNBREAKING,
  )

  private val acceptableEnchantments = ImmutableSet.copyOf(
    setOf(
      Enchantments.FROST_WALKER,
      Enchantments.SOUL_SPEED,
      Enchantments.MENDING,
      Enchantments.BINDING_CURSE,
      Enchantments.VANISHING_CURSE,
    ) union primaryEnchantments
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
      entity.`vshorses$setHorseshoeInInventory`(stack.split(1))
      entity.world.emitGameEvent(entity, GameEvent.EQUIP, entity.pos)
    }
    return ActionResult.success(user.world.isClient)
  }

  override fun canRepair(stack: ItemStack, ingredient: ItemStack) =
    ingredient.isOf(Items.COPPER_INGOT)

  override fun isEnchantable(stack: ItemStack) = true

  override fun getEnchantability() = 9  // Same as iron armor

  override fun canBeEnchantedWith(
    stack: ItemStack,
    enchantment: RegistryEntry<Enchantment>,
    context: EnchantingContext,
  ) = (when (context) {
    EnchantingContext.PRIMARY -> primaryEnchantments
    EnchantingContext.ACCEPTABLE -> acceptableEnchantments
  }).contains(enchantment.key.getOrNull())
}
