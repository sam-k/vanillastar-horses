package com.vanillastar.vshorses.mixin.block;

import java.util.function.Supplier;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Saddleable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin extends Block {
  private LeavesBlockMixin(Settings settings) {
    super(settings);
  }

  /** Tests whether an entity currently intersects with leaves at the same Y-level. */
  @Unique
  private static boolean entityIntersectsLeavesAtLevel(BlockView world, Entity entity) {
    Box entityBox = entity.getBoundingBox();
    BlockPos minEntityBoxPos = BlockPos.ofFloored(entityBox.minX, entity.getY(), entityBox.minZ);
    BlockPos maxEntityBoxPos = BlockPos.ofFloored(entityBox.maxX, entity.getY(), entityBox.maxZ);
    BlockPos.Mutable testPos = new BlockPos.Mutable();

    for (int x = minEntityBoxPos.getX(); x <= maxEntityBoxPos.getX(); x++) {
      for (int z = minEntityBoxPos.getZ(); z <= maxEntityBoxPos.getZ(); z++) {
        testPos.set(x, entity.getY(), z);
        if (world.getBlockState(testPos).getBlock() instanceof LeavesBlock) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  protected VoxelShape getCollisionShape(
      BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    Supplier<VoxelShape> getSuper = () -> super.getCollisionShape(state, world, pos, context);

    // For leaves to have no collision with a shape...
    // ...Shape must be an entity.
    if (!(context instanceof EntityShapeContext entityContext)) {
      return getSuper.get();
    }

    // ...Entity must be saddled with a passenger.
    Entity entity = entityContext.getEntity();
    if (!(entity instanceof Saddleable saddleable)
        || !saddleable.canBeSaddled()
        || !entity.hasControllingPassenger()) {
      return getSuper.get();
    }

    // ...Entity must be below the leaves or, if at the same Y-level, currently intersecting with
    // other leaves at the same Y-level.
    if (entity.getY() > pos.getY()
        || (entity.getY() == pos.getY() && !entityIntersectsLeavesAtLevel(world, entity))) {
      return getSuper.get();
    }

    // ...Leaves must be above a non-collidable block.
    BlockPos posBelow = pos.offset(Direction.DOWN);
    if (((AbstractBlockAccessor) world.getBlockState(posBelow).getBlock()).getCollidable()) {
      return getSuper.get();
    }

    return VoxelShapes.empty();
  }
}
