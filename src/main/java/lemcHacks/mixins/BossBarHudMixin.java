package lemcHacks.mixins;

import lemcHacks.module.ModuleManager;
import lemcHacks.module.render.NoRender;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossBarHud.class)
public class BossBarHudMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void disableBossBarRendering(MatrixStack matrices, CallbackInfo ci)
    {
        if (ModuleManager.Instance.getModule(NoRender.class).isEnabled() && ModuleManager.Instance.getModule(NoRender.class).bossbar.isEnabled())
        {
            ci.cancel();
        }
    }
}
