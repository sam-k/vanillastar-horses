package com.vanillastar.vshorses

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.vanillastar.vshorses.item.HORSE_ARMOR
import com.vanillastar.vshorses.utils.getModIdentifier
import java.util.concurrent.CompletableFuture
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.client.*
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.VanillaRecipeProvider
import net.minecraft.item.AnimalArmorItem
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.SmithingTrimRecipe
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import net.minecraft.registry.tag.ItemTags

class VSHorsesDataGenerator : DataGeneratorEntrypoint {
  /** Auto-generates all trim recipes for all horse armor. */
  private inner class HorseArmorTrimRecipeProvider(
      output: FabricDataOutput,
      completableFuture: CompletableFuture<WrapperLookup>,
  ) : FabricRecipeProvider(output, completableFuture) {
    override fun generate(exporter: RecipeExporter) {
      VanillaRecipeProvider.streamSmithingTemplates().forEach { template ->
        exporter.accept(
            template.id,
            SmithingTrimRecipe(
                Ingredient.ofItems(template.template),
                Ingredient.fromTag(HORSE_ARMOR),
                Ingredient.fromTag(ItemTags.TRIM_MATERIALS),
            ),
            null,
        )
      }
    }
  }

  private inner class TrimmedHorseArmorItemModelProvider(output: FabricDataOutput) :
      FabricModelProvider(output) {
    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {
      Registries.ITEM.filterIsInstance<AnimalArmorItem>()
          .filter { it.type == AnimalArmorItem.Type.EQUESTRIAN }
          .forEach {
            val modelId = ModelIds.getItemModelId(it)
            val textureId = TextureMap.getId(it)

            // Generate JSON models for the horse armor item, with trim information.
            Models.GENERATED.upload(
                modelId,
                TextureMap.layer0(textureId),
                itemModelGenerator.writer,
            ) { id, textures ->
              val jsonObj = Models.GENERATED_TWO_LAYERS.createJson(id, textures)
              val overridesJsonArr = JsonArray()
              for (trimMaterial in ItemModelGenerator.TRIM_MATERIALS) {
                val predicateJsonObj = JsonObject()
                predicateJsonObj.addProperty(
                    ItemModelGenerator.TRIM_TYPE.path,
                    trimMaterial.itemModelIndex,
                )
                val overrideJsonObj = JsonObject()
                overrideJsonObj.addProperty(
                    "model",
                    getModIdentifier(
                            itemModelGenerator
                                .suffixTrim(id, trimMaterial.getAppliedName(it.material))
                                .path
                        )
                        .toString(),
                )
                overrideJsonObj.add("predicate", predicateJsonObj)
                overridesJsonArr.add(overrideJsonObj)
              }
              jsonObj.add("overrides", overridesJsonArr)
              jsonObj
            }

            // Generate JSON models for trim overlays on horse armor items.
            for (trimMaterial in ItemModelGenerator.TRIM_MATERIALS) {
              val trimMaterialName = trimMaterial.getAppliedName(it.material)
              val trimTextureId =
                  getModIdentifier(itemModelGenerator.suffixTrim(modelId, trimMaterialName).path)
              val trimTextureOverlayId =
                  getModIdentifier("trims/items/horse_armor_trim_$trimMaterialName")

              Models.GENERATED_TWO_LAYERS.upload(
                  trimTextureId,
                  TextureMap.layered(textureId, trimTextureOverlayId),
                  itemModelGenerator.writer,
              )
            }
          }
    }

    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {}
  }

  override fun onInitializeDataGenerator(generator: FabricDataGenerator) {
    val pack = generator.createPack()
    pack.addProvider(::HorseArmorTrimRecipeProvider)
    pack.addProvider(::TrimmedHorseArmorItemModelProvider)
  }
}
