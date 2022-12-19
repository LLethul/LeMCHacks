package lemcHacks.mixins;

import lemcHacks.module.ModuleManager;
import lemcHacks.module.render.NoRender;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow private ShaderEffect shader;
    @Inject(method = "bobViewWhenHurt", at = @At("HEAD"), cancellable = true)
    public void disableHurtCam(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (ModuleManager.Instance.getModule(NoRender.class).isEnabled() && ModuleManager.Instance.getModule(NoRender.class).hurtcam.isEnabled()) ci.cancel();
    }
}