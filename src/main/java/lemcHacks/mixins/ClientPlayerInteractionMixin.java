package lemcHacks.mixins;

import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import lemcHacks.module.ModuleManager;
import lemcHacks.module.combat.Reach;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.network.ClientPlayerInteractionManager;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionMixin {
	@Inject(at = {@At("HEAD")},
			method = {"getReachDistance()F"},
			cancellable = true)
		private void onGetReachDistance(CallbackInfoReturnable<Float> ci)
		{
			ci.setReturnValue((float) ModuleManager.Instance.getModule(Reach.class).range.getValueFloat());
		}
		
		@Inject(at = {@At("HEAD")},
			method = {"hasExtendedReach()Z"},
			cancellable = true)
		private void hasExtendedReach(CallbackInfoReturnable<Boolean> cir)
		{
			cir.setReturnValue(true);
		}
}
