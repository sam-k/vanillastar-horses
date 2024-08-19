package com.vanillastar.vshorses.mixin.item;

import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AnimalArmorItem.class)
public abstract class AnimalArmorItemMixin extends ArmorItem {
  @Shadow
  @Final
  private AnimalArmorItem.Type type;

  private AnimalArmorItemMixin(
      RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
    super(material, type, settings);
  }

  @Override
  public boolean isEnchantable(ItemStack stack) {
    return type == AnimalArmorItem.Type.EQUESTRIAN;
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
