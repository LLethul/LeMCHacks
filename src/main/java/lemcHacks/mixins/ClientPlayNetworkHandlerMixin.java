package lemcHacks.mixins;

import lemcHacks.module.ModuleManager;
import lemcHacks.module.exploit.AntiHunger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldBorderInitializeS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onGameStateChange", at = @At("HEAD"), cancellable = true)
    public void cancelGameStateChange(GameStateChangeS2CPacket packet, CallbackInfo ci) {
        if (packet.getReason() == GameStateChangeS2CPacket.GAME_MODE_CHANGED
                || packet.getReason() == GameStateChangeS2CPacket.DEMO_MESSAGE_SHOWN
                || packet.getReason() == GameStateChangeS2CPacket.GAME_WON
        ) {
            ci.cancel();
        }
    }

    @Inject(method = "onWorldBorderInitialize", at = @At("HEAD"), cancellable = true)
    public void cancelWorldBorderInitialize(WorldBorderInitializeS2CPacket packet, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "onHealthUpdate", at = @At("HEAD"), cancellable = true)
    public void cancelFoodAndSaturation(HealthUpdateS2CPacket packet, CallbackInfo ci) {
        if (MinecraftClient.getInstance().player != null && ModuleManager.Instance.getModule(AntiHunger.class).isEnabled()) {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            player.updateHealth(packet.getHealth());
            player.getHungerManager().setFoodLevel(20);
            player.getHungerManager().setSaturationLevel(5.0F);
            ci.cancel();
        }
    }
}