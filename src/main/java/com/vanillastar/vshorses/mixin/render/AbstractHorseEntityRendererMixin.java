package com.vanillastar.vshorses.mixin.render;

import com.vanillastar.vshorses.render.HorseshoeFeatureRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.AbstractHorseEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.entity.passive.AbstractHorseEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractHorseEntityRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class AbstractHorseEntityRendererMixin<
        TEntity extends AbstractHorseEntity, TModel extends HorseEntityModel<TEntity>>
    extends MobEntityRenderer<TEntity, TModel> {
  private AbstractHorseEntityRendererMixin(
      EntityRendererFactory.Context ctx, TModel model, float scale) {
    super(ctx, model, scale);
  }

  @Inject(method = "<init>", at = @At("TAIL"))
  private void addHorseshoeFeatureRenderer(
      EntityRendererFactory.@NotNull Context ctx, TModel model, float scale, CallbackInfo ci) {
    this.addFeature(new HorseshoeFeatureRenderer<>(this, ctx.getModelLoader()));
  }
}
