package org.miha.oculusdhtexturepatch.mixin;

import com.seibel.distanthorizons.api.DhApi;
import net.irisshaders.iris.compat.dh.IrisLodRenderProgram;
import net.irisshaders.iris.gl.IrisRenderSystem;
import org.joml.Matrix4fc;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL46C;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = IrisLodRenderProgram.class, remap = false)
public abstract class IrisLodRenderProgramMixin {
    @Shadow
    public abstract int tryGetUniformLocation2(CharSequence name);

    @Unique
    private int oculusdhtexturepatch$dhBlockAtlas = -1;

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void oculusdhtexturepatch$initializeAtlasUniform(CallbackInfo ci) {
        this.oculusdhtexturepatch$dhBlockAtlas =
                this.tryGetUniformLocation2("dhBlockAtlas");
    }

    @Inject(method = "fillUniformData", at = @At("TAIL"), remap = false)
    private void oculusdhtexturepatch$bindDhAtlas(
            Matrix4fc projection, Matrix4fc modelView,
            int worldYOffset, float partialTicks, CallbackInfo ci) {
        // The active shader program and Iris samplers/images are already updated.
        if (this.oculusdhtexturepatch$dhBlockAtlas != -1) {
            GL20.glUniform1i(this.oculusdhtexturepatch$dhBlockAtlas, 0);
        }

        var result = DhApi.Delayed.renderProxy.getDhBlockRatioAtlasTextureGlId();
        if (result.success) {
            IrisRenderSystem.bindTextureToUnit(GL46C.GL_TEXTURE_2D, 0, result.payload);
        }
    }
}
