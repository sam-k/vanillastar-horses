package com.vanillastar.vshorses.mixin.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;
import java.util.stream.Stream;

import static com.vanillastar.vshorses.render.HorseshoeModelLayerKt.HORSESHOE_MODEL_LAYER;
import static com.vanillastar.vshorses.utils.LoggerHelperKt.getMixinLogger;

@Mixin(EntityModelLayers.class)
@Environment(EnvType.CLIENT)
public abstract class EntityModelLayersMixin {
  @Unique
  private static final Logger LOGGER = getMixinLogger();

  @Shadow
  @Final
  private static Set<EntityModelLayer> LAYERS;

  @Inject(method = "getLayers", at = @At("HEAD"))
  private static void addHorseshoeModelLayer(CallbackInfoReturnable<Stream<EntityModelLayer>> cir) {
    // Add `HORSESHOE_MODEL_LAYER` to private field `LAYERS` the first time it
    // is accessed.
    if (LAYERS.contains(HORSESHOE_MODEL_LAYER)) {
      return;
    }

    if (LAYERS.add(HORSESHOE_MODEL_LAYER)) {
      LOGGER.info("Registered entity model layer {}", HORSESHOE_MODEL_LAYER);
    } else {
      throw new IllegalStateException("Duplicate registration for " +
        HORSESHOE_MODEL_LAYER);
    }
  }
}
