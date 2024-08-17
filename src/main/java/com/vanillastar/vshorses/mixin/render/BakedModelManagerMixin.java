package com.vanillastar.vshorses.mixin.render;

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

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.vanillastar.vshorses.render.HorseArmorTrimsRenderLayerKt.HORSE_ARMOR_TRIMS_ATLAS_TEXTURE;
import static com.vanillastar.vshorses.utils.IdentiferHelperKt.getModIdentifier;
import static com.vanillastar.vshorses.utils.LoggerHelperKt.getMixinLogger;

@Mixin(BakedModelManager.class)
@Environment(EnvType.CLIENT)
public abstract class BakedModelManagerMixin {
  @Unique
  private static final Logger LOGGER = getMixinLogger();

  /**
   * Identifier for the atlas JSON file for horse armor trims.
   */
  @Unique
  private static final Identifier HORSE_ARMOR_TRIMS_ATLAS =
    getModIdentifier("horse_armor_trims");

  @Shadow
  @Final
  @Mutable
  private static Map<Identifier, Identifier> LAYERS_TO_LOADERS;

  @Inject(method = "<clinit>", at = @At("TAIL"))
  private static void addHorseArmorAtlas(CallbackInfo ci) {
    LAYERS_TO_LOADERS = Stream.concat(LAYERS_TO_LOADERS.entrySet().stream(),
        Stream.of(new AbstractMap.SimpleImmutableEntry<>(
          HORSE_ARMOR_TRIMS_ATLAS_TEXTURE,
          HORSE_ARMOR_TRIMS_ATLAS
        ))
      )
      .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey,
        Map.Entry::getValue
      ));

    LOGGER.info("Registered sprite atlas {} with {}",
      HORSE_ARMOR_TRIMS_ATLAS_TEXTURE,
      HORSE_ARMOR_TRIMS_ATLAS
    );
  }
}
