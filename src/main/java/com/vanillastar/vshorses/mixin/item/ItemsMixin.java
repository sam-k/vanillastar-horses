package com.vanillastar.vshorses.mixin.item;

import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.ArmorMaterial;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Items.class)
public abstract class ItemsMixin {
  @ModifyArgs(
      method = {
        "method_63968", // Factory lambda for `LEATHER_HORSE_ARMOR`
        "method_63970", // Factory lambda for `DIAMOND_HORSE_ARMOR`
        "method_63972", // Factory lambda for `GOLDEN_HORSE_ARMOR`
        "method_63974", // Factory lambda for `IRON_HORSE_ARMOR`
      },
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/item/AnimalArmorItem;<init>(Lnet/minecraft/item/equipment/ArmorMaterial;Lnet/minecraft/item/AnimalArmorItem$Type;Lnet/minecraft/registry/entry/RegistryEntry;ZLnet/minecraft/item/Item$Settings;)V"))
  private static void makeHorseArmorDamageable(@NotNull Args args) {
    AnimalArmorItem.Type type = args.get(1);
    if (type != AnimalArmorItem.Type.EQUESTRIAN) {
      return;
    }

    ArmorMaterial material = args.get(0);
    Item.Settings settings = args.get(4);

    args.set(3, true); // `boolean damageOnHurt`
    args.set(4, settings.enchantable(material.enchantmentValue())); // `Settings settings`
  }
}
