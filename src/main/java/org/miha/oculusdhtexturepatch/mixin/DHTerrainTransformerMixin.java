package org.miha.oculusdhtexturepatch.mixin;

import io.github.douira.glsl_transformer.ast.node.TranslationUnit;
import io.github.douira.glsl_transformer.ast.query.Root;
import io.github.douira.glsl_transformer.ast.transform.ASTInjectionPoint;
import io.github.douira.glsl_transformer.ast.transform.ASTParser;
import net.irisshaders.iris.gl.shader.ShaderType;
import net.irisshaders.iris.pipeline.transform.parameter.Parameters;
import net.irisshaders.iris.pipeline.transform.transformer.DHTerrainTransformer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DHTerrainTransformer.class, remap = false)
public abstract class DHTerrainTransformerMixin {
    @Inject(method = "transform", at = @At("TAIL"), remap = false)
    private static void oculusdhtexturepatch$addFragmentTextureFunctions(
            ASTParser parser, TranslationUnit tree, Root root,
            Parameters parameters, CallbackInfo ci) {
        if (parameters.type.glShaderType != ShaderType.FRAGMENT) {
            return;
        }

        tree.parseAndInjectNodes(parser, ASTInjectionPoint.BEFORE_DECLARATIONS,
                "in vec3 iris_vBlockPos;",
                "flat in uvec2 iris_TexId;",
                "uniform sampler2D dhBlockAtlas;",
                "bool dh_hasTexture() { return iris_TexId.x != 0u; }",
                """
                vec2 dh_blockFaceUv() {
                    vec3 pos = fract(iris_vBlockPos);
                    switch (iris_TexId.y) {
                        case 0u: return vec2(pos.x, 1.0 - pos.z); // down
                        case 1u: return vec2(pos.x, pos.z); // up
                        case 2u: return vec2(1.0 - pos.x, 1.0 - pos.y); // north
                        case 3u: return vec2(pos.x, 1.0 - pos.y); // south
                        case 4u: return vec2(pos.z, 1.0 - pos.y); // west
                        default: return vec2(1.0 - pos.z, 1.0 - pos.y); // east
                    }
                }
                """,
                """
                vec4 dh_sampleTexture() {
                    ivec2 atlasSize = textureSize(dhBlockAtlas, 0);
                    vec2 tileOrigin = vec2(float(iris_TexId.x % 256u), float(iris_TexId.x / 256u)) * 16.0;
                    vec2 uv = (tileOrigin + dh_blockFaceUv() * 16.0) / vec2(atlasSize);
                    return texture(dhBlockAtlas, uv);
                }
                """);
    }

    // Oculus has already generated _vert_init() by the time this callback runs.
    // Appending statements to that AST function avoids replacing the entire shader source.
    @Inject(method = "injectVertInit", at = @At("TAIL"), remap = false)
    private static void oculusdhtexturepatch$addVertexTextureOutputs(
            ASTParser parser, TranslationUnit tree, Root root,
            Parameters parameters, CallbackInfo ci) {
        tree.parseAndInjectNodes(parser, ASTInjectionPoint.BEFORE_FUNCTIONS,
                "out vec3 iris_vBlockPos;",
                "flat out uvec2 iris_TexId;");

        tree.appendFunctionBody("_vert_init", parser.parseStatement(root,
                "iris_vBlockPos = vec3(vPosition.xyz);"));
        tree.appendFunctionBody("_vert_init", parser.parseStatement(root,
                "iris_TexId = uvec2(irisExtra.z | (irisExtra.w << 8u), irisExtra.y);"));
    }
}
