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
import net.minecraft.data.client.ItemModelGenerator.TrimMaterial
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.VanillaRecipeProvider
import net.minecraft.item.AnimalArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.ArmorMaterials
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.SmithingTrimRecipe
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.ItemTags
import net.minecraft.util.Identifier

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
    /** Recreates the item ID suffixed with the trim material. */
    private fun getModIdentifierWithTrim(
        itemModelGenerator: ItemModelGenerator,
        itemId: Identifier,
        trimMaterialName: String,
    ) = getModIdentifier(itemModelGenerator.suffixTrim(itemId, trimMaterialName).path)

    /** Recreates the item ID suffixed with the trim material. */
    private fun getModIdentifierWithTrim(
        itemModelGenerator: ItemModelGenerator,
        itemId: Identifier,
        trimMaterial: TrimMaterial,
        armorMaterial: RegistryEntry<ArmorMaterial>,
    ) =
        getModIdentifierWithTrim(
            itemModelGenerator,
            itemId,
            trimMaterial.getAppliedName(armorMaterial),
        )

    /** Generates JSON models for the horse armor item, with trim information. */
    private fun generateHorseArmorItemModel(
        itemModelGenerator: ItemModelGenerator,
        modelId: Identifier,
        textureId: Identifier,
        textureOverlayId: Identifier?,
        armorMaterial: RegistryEntry<ArmorMaterial>,
    ) {
      val (model, textureMap) =
          if (textureOverlayId == null) {
            Pair(Models.GENERATED, TextureMap.layer0(textureId))
          } else {
            Pair(Models.GENERATED_TWO_LAYERS, TextureMap.layered(textureId, textureOverlayId))
          }
      model.upload(modelId, textureMap, itemModelGenerator.writer) { id, textures ->
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
              getModIdentifierWithTrim(itemModelGenerator, id, trimMaterial, armorMaterial)
                  .toString(),
          )
          overrideJsonObj.add("predicate", predicateJsonObj)
          overridesJsonArr.add(overrideJsonObj)
        }
        jsonObj.add("overrides", overridesJsonArr)
        jsonObj
      }
    }

    /** Generates JSON models for trim overlays on horse armor items. */
    private fun generateHorseArmorTrimItemModels(
        itemModelGenerator: ItemModelGenerator,
        modelId: Identifier,
        textureId: Identifier,
        textureOverlayId: Identifier?,
        armorMaterial: RegistryEntry<ArmorMaterial>,
    ) {
      for (trimMaterial in ItemModelGenerator.TRIM_MATERIALS) {
        val trimMaterialName = trimMaterial.getAppliedName(armorMaterial)
        val trimTextureId = getModIdentifierWithTrim(itemModelGenerator, modelId, trimMaterialName)
        val trimTextureOverlayId =
            getModIdentifier("trims/items/horse_armor_trim_$trimMaterialName")

        val (model, textureMap) =
            if (textureOverlayId == null) {
              Pair(Models.GENERATED_TWO_LAYERS, TextureMap.layered(textureId, trimTextureOverlayId))
            } else {
              Pair(
                  Models.GENERATED_THREE_LAYERS,
                  TextureMap.layered(textureId, textureOverlayId, trimTextureOverlayId),
              )
            }
        model.upload(trimTextureId, textureMap, itemModelGenerator.writer)
      }
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {
      Registries.ITEM.filterIsInstance<AnimalArmorItem>()
          .filter { it.type == AnimalArmorItem.Type.EQUESTRIAN }
          .forEach {
            val modelId = ModelIds.getItemModelId(it)
            val textureId = TextureMap.getId(it)
            val textureOverlayId =
                if (it.material.value() == ArmorMaterials.LEATHER.value()) {
                  TextureMap.getSubId(it, "_overlay")
                } else null

            generateHorseArmorItemModel(
                itemModelGenerator,
                modelId,
                textureId,
                textureOverlayId,
                it.material,
            )
            generateHorseArmorTrimItemModels(
                itemModelGenerator,
                modelId,
                textureId,
                textureOverlayId,
                it.material,
            )
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
