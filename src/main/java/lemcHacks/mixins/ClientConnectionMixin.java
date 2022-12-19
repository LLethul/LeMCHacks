package lemcHacks.mixins;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lemcHacks.Client;
import lemcHacks.event.events.PacketEvent;
import lemcHacks.module.ModuleManager;
import lemcHacks.module.liveoverflow.LOServerBorderBypass;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.s2c.play.*;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Shadow
    private Channel channel;

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void channelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo callback) {
        if (this.channel.isOpen() && packet != null) {
            PacketEvent.Read event = new PacketEvent.Read(packet);
            Client.Instance.eventBus.post(event);

            if (event.isCancelled()) {
                callback.cancel();
            }
        }
    }

    @Inject(method = "send(Lnet/minecraft/network/Packet;Lnet/minecraft/network/PacketCallbacks;)V", at = @At("HEAD"), cancellable = true)
    private void send(Packet<?> packet, PacketCallbacks packetCallback, CallbackInfo callback) {
        PacketEvent.Send event = new PacketEvent.Send(packet);
        Client.Instance.eventBus.post(event);

        if (event.isCancelled()) {
            callback.cancel();
        }
    }

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
