package com.vanillastar.vshorses.mixin.block;

import static com.vanillastar.vshorses.item.HorseshoeItemKt.HORSESHOE_ITEM;
import static com.vanillastar.vshorses.utils.LoggerHelperKt.getMixinLogger;

import com.vanillastar.vshorses.entity.VSHorseEntity;
import java.util.List;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Box;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DispenserBehavior.class)
public interface DispenserBehaviorMixin {
  @Inject(method = "registerDefaults", at = @At("TAIL"))
  private static void registerHorseshoeBehavior(CallbackInfo ci) {
    Logger LOGGER = getMixinLogger();

    DispenserBlock.registerBehavior(HORSESHOE_ITEM, new FallibleItemDispenserBehavior() {
      @Override
      public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        // Closing this resource will close the server.
        @SuppressWarnings("resource")
        List<LivingEntity> candidates = pointer
            .world()
            .getEntitiesByClass(
                LivingEntity.class,
                new Box(pointer.pos().offset(pointer.state().get(DispenserBlock.FACING))),
                entity -> entity instanceof VSHorseEntity vsHorseEntity
                    && vsHorseEntity.vshorses$canBeShoed()
                    && !vsHorseEntity.vshorses$isShoed());
        if (candidates.isEmpty()) {
          return super.dispenseSilently(pointer, stack);
        }
        ((VSHorseEntity) candidates.getFirst())
            .vshorses$getHorseshoeInventory()
            .setStack(stack.split(1));
        this.setSuccess(true);
        return stack;
      }
    });
    LOGGER.info("Registered dispenser behavior for item {}", HORSESHOE_ITEM.getId());
  }
}
