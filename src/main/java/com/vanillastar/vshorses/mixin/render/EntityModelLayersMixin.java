package com.vanillastar.vshorses.mixin.render;

import static com.vanillastar.vshorses.render.HorseshoeEntityModel.HORSESHOE_BABY_MODEL;
import static com.vanillastar.vshorses.render.HorseshoeEntityModel.HORSESHOE_MODEL;
import static com.vanillastar.vshorses.utils.LoggerHelperKt.getMixinLogger;

import java.util.Set;
import java.util.stream.Stream;
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

@Mixin(EntityModelLayers.class)
@Environment(EnvType.CLIENT)
public abstract class EntityModelLayersMixin {
  @Unique
  private static final Logger LOGGER = getMixinLogger();

  @Unique
  private static final Set<EntityModelLayer> HORSESHOE_MODEL_LAYERS =
      Set.of(HORSESHOE_MODEL, HORSESHOE_BABY_MODEL);

  @Shadow
  @Final
  private static Set<EntityModelLayer> LAYERS;

  @Inject(method = "getLayers", at = @At("HEAD"))
  private static void addHorseshoeModelLayer(CallbackInfoReturnable<Stream<EntityModelLayer>> cir) {
    for (EntityModelLayer horseshoeModelLayer : HORSESHOE_MODEL_LAYERS) {
      // Add model layer to private field `LAYERS` the first time it is accessed.
      if (!LAYERS.contains(horseshoeModelLayer)) {
        LAYERS.add(horseshoeModelLayer);
        LOGGER.info("Registered entity model layer {}", horseshoeModelLayer);
      }
    }
  }
}
