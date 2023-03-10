package lemcHacks.module.movement;

import lemcHacks.event.events.ClientMoveEvent;
import lemcHacks.eventbus.LeMCSubscribe;
import lemcHacks.module.Module;
import lemcHacks.module.ModuleManager;
import lemcHacks.module.combat.Surround;
import lemcHacks.module.settings.*;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.math.Vec3d;

public class Speed extends Module {

    public ModeSetting speedMode = new ModeSetting("Mode","Strafe", "Boost", "Strafe");
    public NumberSetting speedSetting = new NumberSetting("Speed", 0.15, 0.55, 0.31, 0.01);
    public BooleanSetting strafeJumping = new BooleanSetting("StrafeJump", true);

    public Speed() {
        super("Speed", "fast fast", Category.MOVEMENT, true);
        addSettings(speedMode, speedSetting, strafeJumping);
    }

    @Override
    public void onTick() {
        if (!isEnabled()) return;
        if (speedMode.getIdx()==0) {
            mc.player.setVelocity(mc.player.getVelocity().x * 1.1, mc.player.getVelocity().y, mc.player.getVelocity().z * 1.1);
        }


        if (speedMode.getIdx()==1) {
            if ((mc.player.forwardSpeed != 0 || mc.player.sidewaysSpeed != 0)) {
                if (!mc.player.isSprinting()) {
                    mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
                }

                mc.player.setVelocity(new Vec3d(0, mc.player.getVelocity().y, 0));
                mc.player.updateVelocity((float)speedSetting.getValueFloat(),
                        new Vec3d(mc.player.sidewaysSpeed, 0, mc.player.forwardSpeed));

                double vel = Math.abs(mc.player.getVelocity().getX()) + Math.abs(mc.player.getVelocity().getZ());

                if (strafeJumping.isEnabled() && vel >= 0.12 && mc.player.isOnGround()) {
                    if (ModuleManager.Instance.getModule(Surround.class).isEnabled()) ModuleManager.Instance.getModule(Surround.class).toggle();
                    mc.player.updateVelocity(vel >= 0.3 ? 0.0f : 0.15f, new Vec3d(mc.player.sidewaysSpeed, 0, mc.player.forwardSpeed));
                    mc.player.jump();
                }
            }
        }
        super.onTick();
    }

    @LeMCSubscribe
    public void onMove(ClientMoveEvent event) {
        if (mc.player.forwardSpeed==0 && mc.player.sidewaysSpeed == 0) {
            event.setVec(new Vec3d(0, event.getVec().y, 0));
        }
    }
}