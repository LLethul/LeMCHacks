package lemcHacks.module.combat;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Streams;

import lemcHacks.module.Module;
import lemcHacks.module.settings.*;
import lemcHacks.util.world.WorldUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class BowBot extends Module {

	public BooleanSetting shoot = new BooleanSetting("Auto Shoot", true);
	public NumberSetting charge = new NumberSetting("Charge", 0.1,1,0.5,0.1);
	public BooleanSetting aim = new BooleanSetting("Aim", false);
	
	public BowBot() {
		super("Bow Aimbot", "Aimbot.. dumbass", Category.COMBAT, true);
		addSettings(shoot, charge, aim);
	}
	
	public void onTick() {
        if (!(mc.player.getMainHandStack().getItem() instanceof RangedWeaponItem) || !mc.player.isUsingItem() || !isEnabled()) return;

        if (shoot.Enabled && BowItem.getPullProgress(mc.player.getItemUseTime()) >= charge.getValueFloat()) {
            mc.player.stopUsingItem();
            mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.UP));
        }

        // skidded from wurst no bully pls
        if (aim.Enabled) {
            List<Entity> targets = Streams.stream(mc.world.getEntities())
                    .filter(e -> e instanceof LivingEntity && e != mc.player)
                    .sorted((a, b) -> Float.compare(a.distanceTo(mc.player), b.distanceTo(mc.player))).collect(Collectors.toList());

            if (targets.isEmpty()) return;

            LivingEntity target = (LivingEntity) targets.get(0);

            // set velocity
            float velocity = (72000 - mc.player.getItemUseTimeLeft()) / 20F;
            velocity = (velocity * velocity + velocity * 2) / 3;

            if (velocity > 1) velocity = 1;

            // set position to aim at
            double d = mc.player.getPos().add(0, mc.player.getEyeHeight(mc.player.getPose()), 0)
                    .distanceTo(target.getBoundingBox().getCenter());
            double x = target.getX() + (target.getX() - target.prevX) * d
                    - mc.player.getX();
            double y = target.getY() + (target.getY() - target.prevY) * d
                    + target.getHeight() * 0.5 - mc.player.getY()
                    - mc.player.getEyeHeight(mc.player.getPose());
            double z = target.getZ() + (target.getZ() - target.prevZ) * d
                    - mc.player.getZ();

            // set yaw
            mc.player.setYaw((float) Math.toDegrees(Math.atan2(z, x)) - 90);

            // calculate needed pitch
            double hDistance = Math.sqrt(x * x + z * z);
            double hDistanceSq = hDistance * hDistance;
            float g = 0.006F;
            float velocitySq = velocity * velocity;
            float velocityPow4 = velocitySq * velocitySq;
            float neededPitch = (float) -Math.toDegrees(Math.atan((velocitySq - Math
                    .sqrt(velocityPow4 - g * (g * hDistanceSq + 2 * y * velocitySq)))
                    / (g * hDistance)));

            // set pitch
            if (Float.isNaN(neededPitch))
                WorldUtil.facePos(target.getX(), target.getY() + target.getHeight() / 2, target.getZ());
            else mc.player.setPitch(neededPitch);
        }
    }
	
}
