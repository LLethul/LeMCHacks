package lemcHacks.mixins;

import lemcHacks.module.ModuleManager;
import lemcHacks.module.liveoverflow.LOServerBorderBypass;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.s2c.play.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class CCMixin {


    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void onHandlePacket(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
        if (ModuleManager.Instance.getModule(LOServerBorderBypass.class).isEnabled()) {
            if ((packet instanceof WorldBorderCenterChangedS2CPacket || packet instanceof WorldBorderSizeChangedS2CPacket
                    || packet instanceof WorldBorderInitializeS2CPacket
                    || packet instanceof WorldBorderInterpolateSizeS2CPacket
                    || packet instanceof WorldBorderWarningBlocksChangedS2CPacket)) {
                ci.cancel();
            }
        }
    }
}