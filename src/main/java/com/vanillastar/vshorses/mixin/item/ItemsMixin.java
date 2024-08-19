package com.vanillastar.vshorses.mixin.item;

import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Items.class)
public abstract class ItemsMixin {
  @ModifyArgs(
      method = "<clinit>",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/item/AnimalArmorItem;<init>(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/item/AnimalArmorItem$Type;ZLnet/minecraft/item/Item$Settings;)V"))
  private static void interceptRegisterHorseArmorItem(@NotNull Args args) {
    RegistryEntry<ArmorMaterial> material = args.get(0);
    AnimalArmorItem.Type type = args.get(1);
    Item.Settings settings = args.get(3);

    if (type != AnimalArmorItem.Type.EQUESTRIAN) {
      return;
    }
    int maxDamageMultiplier =
        switch (material.getIdAsString()) {
          case "minecraft:leather" -> 5; // Same as leather armor
          case "minecraft:iron" -> 7; // Same as golden armor
          case "minecraft:gold" -> 15; // Same as iron armor
          case "minecraft:diamond" -> 33; // Same as diamond armor
          default -> 0; // Ignore
        };
    if (maxDamageMultiplier > 0) {
      settings.maxDamage(ArmorItem.Type.BODY.getMaxDamage(maxDamageMultiplier));
    }
  }
}
