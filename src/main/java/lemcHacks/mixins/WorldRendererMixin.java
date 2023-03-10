package lemcHacks.mixins;

import lemcHacks.Client;
import lemcHacks.event.events.WorldRenderEvent;
import lemcHacks.module.ModuleManager;
import lemcHacks.module.render.ChestESP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Inject(at = {@At("RETURN")}, method = {"render"})
    private void onRenderWorld(MatrixStack matrixStack, float tickDelta, long limitTime, boolean renderBlockOutline,
                               Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f,
                               CallbackInfo info) {
        ModuleManager.Instance.getModule(ChestESP.class).onRender(matrixStack, tickDelta);
    }
}