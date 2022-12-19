package lemcHacks.mixins;
import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import lemcHacks.module.ModuleManager;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {

    @Inject(method = "getLuminance", at = @At("HEAD"), cancellable = true)
    public void getLuminance(CallbackInfoReturnable<Integer> cir) {
        if (ModuleManager.Instance.getModule(lemcHacks.module.render.Xray.class).isEnabled()) {
            cir.setReturnValue(15);
        }
    }
}