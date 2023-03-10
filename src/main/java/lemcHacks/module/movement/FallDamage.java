package lemcHacks.module.movement;
import lemcHacks.module.Module;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class FallDamage extends Module {
	
    public FallDamage() {
        super("Fall Damage", "no more fall damage", Category.MOVEMENT, true);
    }

    @Override
    public void onTick() {
        if (mc.player.fallDistance > 2.5f) {
            if (mc.player.isFallFlying())
                return;
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
        }
    }
}