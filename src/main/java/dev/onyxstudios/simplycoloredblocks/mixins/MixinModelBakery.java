package dev.onyxstudios.simplycoloredblocks.mixins;

import dev.onyxstudios.simplycoloredblocks.SimplyColoredBlocks;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.profiler.IProfiler;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ModelBakery.class)
public abstract class MixinModelBakery {

    private static ResourceLocation COLORED_BLOCK_LOC = new ResourceLocation(SimplyColoredBlocks.MODID, "block/colored_block");

    @Shadow
    @Final
    private Map<ResourceLocation, IUnbakedModel> unbakedModels;

    @Inject(method = "processLoading", at = @At("HEAD"), remap = false)
    public void processLoading(IProfiler profilerIn, int maxMipmapLevel, CallbackInfo ci) {
        addModelToCache(COLORED_BLOCK_LOC);
    }

    @Inject(method = "getUnbakedModel", at = @At("HEAD"), cancellable = true)
    public void getUnbakedModel(ResourceLocation modelLocation, CallbackInfoReturnable<IUnbakedModel> cir) {
        if(modelLocation.getNamespace().equals(SimplyColoredBlocks.MODID) && modelLocation.getPath().startsWith("colored_block")) {
            cir.setReturnValue(unbakedModels.get(COLORED_BLOCK_LOC));
        }
    }

    @Shadow
    void addModelToCache(ResourceLocation locationIn) {
        throw new IllegalStateException("Mixin failed to shadow addModelToCache(ResourceLocation)");
    }
}
