package lemcHacks.mixins;

import lemcHacks.module.ModuleManager;
import lemcHacks.module.render.Xray;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "me.jellysquid.mods.sodium.client.render.occlusion.BlockOcclusionCache")
public class BlockOcclusionCacheMixin {
    @Inject(at = @At("HEAD"), method = "shouldDrawSide", cancellable = true, remap = false)
    private boolean xray(BlockState state, BlockView world, BlockPos pos, Direction side, CallbackInfoReturnable<Boolean> cir) {
        if (ModuleManager.Instance.getModule(Xray.class).isEnabled()) {
            boolean blockVisible = Xray.blocks.contains(state.getBlock());
            if (state.isOf(Blocks.BEDROCK)) {
                if (ModuleManager.Instance.getModule(Xray.class).bedrock.isEnabled()) {
                    blockVisible=true;
                } else
                    blockVisible=false;
            } else {
                cir.setReturnValue(blockVisible);
                return blockVisible;
            }
        }
        return true;
    }
}


