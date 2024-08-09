package com.vanillastar.vshorses

import com.vanillastar.vshorses.utils.getModIdentifier
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.VanillaRecipeProvider
import net.minecraft.item.Item
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.SmithingTrimRecipe
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey
import java.util.concurrent.CompletableFuture

private val HORSE_ARMOR: TagKey<Item> =
  TagKey.of(RegistryKeys.ITEM, getModIdentifier("horse_armor"))

class VSHorsesDataGenerator: DataGeneratorEntrypoint {
  private class HorseArmorTrimRecipeProvider(
    output: FabricDataOutput,
    completableFuture: CompletableFuture<WrapperLookup>
  ): FabricRecipeProvider(output, completableFuture) {
    override fun generate(exporter: RecipeExporter) {
      VanillaRecipeProvider.streamSmithingTemplates().forEach { template ->
        exporter.accept(
          template.id, SmithingTrimRecipe(
            Ingredient.ofItems(template.template),
            Ingredient.fromTag(HORSE_ARMOR),
            Ingredient.fromTag(ItemTags.TRIM_MATERIALS)
          ), null
        )
      }
    }
  }

  override fun onInitializeDataGenerator(generator: FabricDataGenerator) {
    val pack = generator.createPack()
    pack.addProvider(::HorseArmorTrimRecipeProvider)
  }
}
