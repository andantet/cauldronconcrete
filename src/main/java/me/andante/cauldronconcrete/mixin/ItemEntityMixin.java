package me.andante.cauldronconcrete.mixin;

import me.andante.cauldronconcrete.block.ConcretePowderBlockAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ConcretePowderBlock;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Shadow public abstract ItemStack getStack();
    @Shadow public abstract void setStack(ItemStack stack);

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        ItemEntity $this = ItemEntity.class.cast(this);
        if (!$this.isRemoved()) {
            ItemStack stack = this.getStack();
            Item item = stack.getItem();
            if (item instanceof BlockItem) {
                Block block = ((BlockItem) item).getBlock();
                if (block instanceof ConcretePowderBlock) {
                    BlockState state = $this.world.getBlockState($this.getBlockPos());
                    if ((state.getBlock() instanceof LeveledCauldronBlock && state.get(Properties.LEVEL_3) > 0) || $this.isWet()) {
                        NbtCompound tag = stack.writeNbt(new NbtCompound());
                        tag.putString("id", Registry.ITEM.getId(((ConcretePowderBlockAccessor) block).cauldronconcrete_getBlock().asItem()).toString());

                        this.setStack(ItemStack.fromNbt(tag));
                    }
                }
            }
        }
    }
}
