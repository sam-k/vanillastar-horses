package com.vanillastar.vshorses.mixin.render;

import com.vanillastar.vshorses.render.TextureAtlasHelperKt;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.vanillastar.vshorses.render.HorseArmorTrimAtlasKt.HORSE_ARMOR_TRIM_ENTITY_ATLAS;
import static com.vanillastar.vshorses.render.HorseArmorTrimAtlasKt.HORSE_ARMOR_TRIM_ITEM_ATLAS;
import static com.vanillastar.vshorses.utils.LoggerHelperKt.getMixinLogger;

@Mixin(BakedModelManager.class)
@Environment(EnvType.CLIENT)
public abstract class BakedModelManagerMixin {
  /**
   * Map of all atlas texture IDs to corresponding atlas IDs.
   */
  @Unique
  private static final Map<Identifier, Identifier> TEXTURE_TO_ATLAS_MAP =
    Stream.of(HORSE_ARMOR_TRIM_ENTITY_ATLAS, HORSE_ARMOR_TRIM_ITEM_ATLAS)
      .collect(Collectors.toUnmodifiableMap(TextureAtlasHelperKt::getTextureAtlasId,
        Function.identity()
      ));

  @Unique
  private static final Logger LOGGER = getMixinLogger();

  @Shadow
  @Final
  @Mutable
  private static Map<Identifier, Identifier> LAYERS_TO_LOADERS;

  @Inject(method = "<clinit>", at = @At("TAIL"))
  private static void addHorseArmorAtlas(CallbackInfo ci) {
    LAYERS_TO_LOADERS = Stream.concat(LAYERS_TO_LOADERS.entrySet().stream(),
        TEXTURE_TO_ATLAS_MAP.entrySet().stream()
      )
      .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey,
        Map.Entry::getValue
      ));

    TEXTURE_TO_ATLAS_MAP.forEach((textureId, atlasId) -> LOGGER.info("Registered sprite atlas {} with {}",
      atlasId,
      textureId
    ));
  }
}
