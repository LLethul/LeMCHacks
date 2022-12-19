package lemcHacks.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.MinecraftClient;

import lemcHacks.Client;

@Mixin(MinecraftClient.class)
public class MCCMixin {

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	public void onTick(CallbackInfo ci) {
		Client.Instance.onTick();
	}
	
}