package me.andante.cauldronconcrete.mixin;

import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ConcretePowderBlock;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(LeveledCauldronBlock.class)
@Debug(export = true)
public abstract class LeveledCauldronBlockMixin extends AbstractCauldronBlock {
    public LeveledCauldronBlockMixin(Settings settings, Map<Item, CauldronBehavior> behaviorMap) {
        super(settings, behaviorMap);
    }

    @Inject(method = "onEntityCollision", at = @At("HEAD"))
    private void convertConcreteItems(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (!world.isClient && entity instanceof ItemEntity item && item.getStack().getItem() instanceof BlockItem blockItem
                && blockItem.getBlock() instanceof ConcretePowderBlock block && this.isEntityTouchingFluid(state, pos, entity)) {
            NbtCompound tag = item.getStack().writeNbt(new NbtCompound());
            tag.putString("id", Registry.ITEM.getId(((ConcretePowderBlockAccessor) block).getHardenedState().getBlock().asItem()).toString());

            item.setStack(ItemStack.fromNbt(tag));
            var itemPos = item.getPos();
            ((ServerWorld)world).spawnParticles(ParticleTypes.BUBBLE, itemPos.x, itemPos.y, itemPos.z, 20, 0, 0, 0, 0.1);
            world.playSound(null, pos, SoundEvents.BLOCK_BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundCategory.BLOCKS, 0.5F, 1.0F);
        }
    }
}
