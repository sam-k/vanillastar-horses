package com.vanillastar.vshorses.mixin.render;

import com.vanillastar.vshorses.entity.VSHorseEntity;
import com.vanillastar.vshorses.render.HorseshoeFeatureRenderer;
import com.vanillastar.vshorses.render.VSHorseEntityRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.AbstractHorseEntityRenderer;
import net.minecraft.client.render.entity.AgeableMobEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.AbstractHorseEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingHorseEntityRenderState;
import net.minecraft.entity.passive.AbstractHorseEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("deprecation")
@Mixin(AbstractHorseEntityRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class AbstractHorseEntityRendererMixin<
        TEntity extends AbstractHorseEntity,
        TState extends LivingHorseEntityRenderState,
        TModel extends AbstractHorseEntityModel<TState>>
    extends AgeableMobEntityRenderer<TEntity, TState, TModel> {
  private AbstractHorseEntityRendererMixin(
      EntityRendererFactory.Context ctx, TModel model, TModel babyModel, float scale) {
    super(ctx, model, babyModel, scale);
  }

  @Inject(method = "<init>", at = @At("TAIL"))
  private void addHorseshoeFeatureRenderer(
      @NotNull EntityRendererFactory.Context ctx,
      EntityModel<TState> model,
      EntityModel<TState> babyModel,
      float scale,
      CallbackInfo ci) {
    this.addFeature(
        new HorseshoeFeatureRenderer<>(this, ctx.getModelLoader(), ctx.getEquipmentRenderer()));
  }

  @Inject(
      method =
          "updateRenderState(Lnet/minecraft/entity/passive/AbstractHorseEntity;Lnet/minecraft/client/render/entity/state/LivingHorseEntityRenderState;F)V",
      at = @At("TAIL"))
  private void updateHorseshoeRenderState(
      TEntity entity, TState state, float tickDelta, CallbackInfo ci) {
    if (entity instanceof VSHorseEntity vsHorseEntity
        && state instanceof VSHorseEntityRenderState vsHorseEntityRenderState) {
      vsHorseEntityRenderState.vshorses$setHorseshoe(
          vsHorseEntity.vshorses$getHorseshoeInventory().getStack());
    }
  }
}
