package me.andante.cauldronconcrete.mixin;

import me.andante.cauldronconcrete.block.ConcretePowderBlockAccessor;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ConcretePowderBlock;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConcretePowderBlock.class)
public class ConcretePowderBlockMixin implements ConcretePowderBlockAccessor {
    private Block cauldronconcrete_hardened;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(Block hardened, AbstractBlock.Settings settings, CallbackInfo ci) {
        this.cauldronconcrete_hardened = hardened;
    }

    @NotNull
    @Override
    public Block cauldronconcrete_getBlock() {
        return this.cauldronconcrete_hardened;
    }
}
