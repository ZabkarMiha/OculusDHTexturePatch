package org.miha.oculusdhtexturepatch.mixin;

import com.google.common.collect.ImmutableList;
import net.irisshaders.iris.compat.dh.DHCompat;
import net.irisshaders.iris.gl.shader.StandardMacros;
import net.irisshaders.iris.helpers.StringPair;
import net.minecraftforge.fml.loading.LoadingModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = StandardMacros.class, remap = false)
public abstract class StandardMacrosMixin {
    @Inject(method = "createStandardEnvironmentDefines", at = @At("RETURN"),
            cancellable = true, remap = false)
    private static void oculusdhtexturepatch$addTextureDefine(
            CallbackInfoReturnable<ImmutableList<StringPair>> cir) {
        if (LoadingModList.get().getModFileById("distanthorizons") == null
                || !DHCompat.hasRenderingEnabled()) {
            return;
        }

        ImmutableList<StringPair> current = cir.getReturnValue();
        if (current.stream().anyMatch(pair -> pair.key().equals("DISTANT_HORIZONS_TEXTURES"))) {
            return;
        }

        cir.setReturnValue(ImmutableList.<StringPair>builder()
                .addAll(current)
                .add(new StringPair("DISTANT_HORIZONS_TEXTURES", ""))
                .build());
    }
}
