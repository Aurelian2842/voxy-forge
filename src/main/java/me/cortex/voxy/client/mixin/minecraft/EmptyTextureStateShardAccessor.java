package me.cortex.voxy.client.mixin.minecraft;

import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(targets = "net.minecraft.client.renderer.RenderStateShard$EmptyTextureStateShard")
public interface EmptyTextureStateShardAccessor {

    // 这里使用 @Invoker 或 @Accessor 均可
    @Invoker("cutoutTexture")
    Optional<ResourceLocation> voxy$getCutoutTexture();
}