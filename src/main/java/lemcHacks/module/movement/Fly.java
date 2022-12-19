package lemcHacks.module.movement;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import lemcHacks.module.Module;
import lemcHacks.module.settings.*;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.util.math.BlockPos;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
//
//if (antiKick.isEnabled()) {
//        antiKickCounter++;
//        if (antiKickCounter > 20 && mc.player.world.getBlockState(new BlockPos(mc.player.getPos().subtract(0,0.0433D,0))).isAir()) {
//        antiKickCounter = 0;
//        mc.player.setPos(mc.player.getX(), mc.player.getY()-0.0433D, mc.player.getZ());
//        }
//        }

public class Fly extends Module {

    public ModeSetting flightMode = new ModeSetting("Mode","Vanilla", "Vanilla");
    public NumberSetting speed = new NumberSetting("Speed", 0, 5, 1, 0.1);
    public BooleanSetting antiKick = new BooleanSetting("Anti kick", true);
    public static ClientPlayNetworkHandler networkHandler;
    int antiKickCounter = 0;

    public Fly() {
        super("Flight", "flying like bruh", Category.MOVEMENT, true);
        addSettings(flightMode, speed, antiKick);
    }


    @Override
    public void onTick() {
        if (!isEnabled())return;
        if (flightMode.getIdx()==0) {
            if (antiKick.isEnabled()) {
                antiKickCounter++;
                if (antiKickCounter > 20 && mc.player.world.getBlockState(new BlockPos(mc.player.getPos().subtract(0,0.0433D,0))).isAir()) {
                    antiKickCounter = 0;
                    mc.player.setPos(mc.player.getX(), mc.player.getY()-0.0433D, mc.player.getZ());
                }
            }
            mc.player.getAbilities().flying = true;
            mc.player.getAbilities().setFlySpeed((float) speed.getValueFloat());

        }
        super.onTick();
    }
    @Override
    public void onDisable() {
        mc.player.getAbilities().flying = false;
        super.onDisable();
    }
}