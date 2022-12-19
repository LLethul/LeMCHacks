package lemcHacks.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lemcHacks.Client;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.Keyboard;

@Mixin(Keyboard.class)
public class KBoardMixin {

	@Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
	private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
		Client.Instance.onKeyPress(key, action);
	}
}
