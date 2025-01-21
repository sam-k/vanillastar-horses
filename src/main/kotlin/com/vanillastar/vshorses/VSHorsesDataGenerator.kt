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
import net.minecraft.component.DataComponentTypes
import net.minecraft.data.client.*
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.RecipeGenerator
import net.minecraft.data.server.recipe.SmithingTrimRecipeJsonBuilder
import net.minecraft.data.server.recipe.VanillaRecipeGenerator.streamSmithingTemplates
import net.minecraft.item.AnimalArmorItem
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import net.minecraft.registry.tag.ItemTags

class VSHorsesDataGenerator : DataGeneratorEntrypoint {
  /** Auto-generates all trim recipes for all horse armor. */
  private inner class HorseArmorTrimRecipeProvider(
      output: FabricDataOutput,
      completableFuture: CompletableFuture<WrapperLookup>,
  ) : FabricRecipeProvider(output, completableFuture) {
    override fun getRecipeGenerator(
        registryLookup: WrapperLookup,
        exporter: RecipeExporter,
    ): RecipeGenerator =
        object : RecipeGenerator(registryLookup, exporter) {
          private val itemLookup = registryLookup.getOrThrow(RegistryKeys.ITEM)

          override fun generate() {
            streamSmithingTemplates().forEach {
              SmithingTrimRecipeJsonBuilder.create(
                      Ingredient.ofItem(it.template),
                      Ingredient.fromTag(itemLookup.getOrThrow(HORSE_ARMOR)),
                      Ingredient.fromTag(itemLookup.getOrThrow(ItemTags.TRIM_MATERIALS)),
                      RecipeCategory.MISC,
                  )
                  .criterion("has_smithing_trim_template", this.conditionsFromItem(it.template))
                  .offerTo(exporter, it.id)
            }
          }
        }

    override fun getName(): String = "Horse Armor Trims"
  }

  private inner class TrimmedHorseArmorItemModelProvider(output: FabricDataOutput) :
      FabricModelProvider(output) {
    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {
      Registries.ITEM.filterIsInstance<AnimalArmorItem>()
          .filter { it.type == AnimalArmorItem.Type.EQUESTRIAN }
          .forEach {
            val modelId = ModelIds.getItemModelId(it)
            val textureId = TextureMap.getId(it)
            val equippableModelId = it.components.get(DataComponentTypes.EQUIPPABLE)?.model?.get()

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
                                .suffixTrim(id, trimMaterial.getAppliedName(equippableModelId))
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
              val trimMaterialName = trimMaterial.getAppliedName(equippableModelId)
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
