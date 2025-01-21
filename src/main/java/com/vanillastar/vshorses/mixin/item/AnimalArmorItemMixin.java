package com.vanillastar.vshorses.mixin.item;

import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AnimalArmorItem.class)
public abstract class AnimalArmorItemMixin extends Item {
  private AnimalArmorItemMixin(Item.Settings settings) {
    super(settings);
  }

  @Override
  public boolean canBeEnchantedWith(
      ItemStack stack,
      @NotNull RegistryEntry<Enchantment> enchantment,
      @NotNull EnchantingContext context) {
    Enchantment.Definition definition = enchantment.value().definition();
    return (switch (context) {
          case PRIMARY -> definition.primaryItems().orElse(definition.supportedItems());
          case ACCEPTABLE -> definition.supportedItems();
        })
        .stream().anyMatch(entry -> entry.isIn(ItemTags.CHEST_ARMOR_ENCHANTABLE));
  }
}
