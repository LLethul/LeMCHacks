package lemcHacks.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lemcHacks.ui.HUD;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	
	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	public void renderHud(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
		HUD.Render(matrices, tickDelta);
	}
	
}
