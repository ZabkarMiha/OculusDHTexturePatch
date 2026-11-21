package org.miha.oculusdhtexturepatch.mixin;

import io.github.douira.glsl_transformer.ast.node.TranslationUnit;
import io.github.douira.glsl_transformer.ast.query.Root;
import io.github.douira.glsl_transformer.ast.transform.ASTInjectionPoint;
import io.github.douira.glsl_transformer.ast.transform.ASTParser;
import net.irisshaders.iris.gl.shader.ShaderType;
import net.irisshaders.iris.pipeline.transform.parameter.Parameters;
import net.irisshaders.iris.pipeline.transform.transformer.DHGenericTransformer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DHGenericTransformer.class, remap = false)
public abstract class DHGenericTransformerMixin {
    @Inject(method = "transform", at = @At("TAIL"), remap = false)
    private static void oculusdhtexturepatch$injectFallbacks(
            ASTParser parser, TranslationUnit tree, Root root,
            Parameters parameters, CallbackInfo ci) {
        if (parameters.type.glShaderType != ShaderType.FRAGMENT) {
            return;
        }

        tree.parseAndInjectNodes(parser, ASTInjectionPoint.BEFORE_DECLARATIONS,
                "bool dh_hasTexture() { return false; }",
                "vec4 dh_sampleTexture() { return vec4(1.0); }");
    }
}
