package lemcHacks.module.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lemcHacks.module.Module;
import lemcHacks.util.world.WorldUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class ArrowJuke extends Module {

    public ArrowJuke() {
        super("ArrowJuke", "Juke arrows lol", Category.COMBAT, true);
    }

    public void onTick() {
        for (Entity e : mc.world.getEntities()) {
            if (e.age > 75 || !(e instanceof ArrowEntity) || ((ArrowEntity) e).getOwner() == mc.player)
                continue;

            Box playerBox = mc.player.getBoundingBox().expand(0.3);
            List<Box> futureBoxes = new ArrayList<>(250);

            Box currentBox = e.getBoundingBox();
            Vec3d currentVel = e.getVelocity();

            for (int i = 0; i < 250; i++) {
                currentBox = currentBox.offset(currentVel);
                currentVel = currentVel.multiply(0.99, 0.94, 0.99);
                futureBoxes.add(currentBox);

                if (!mc.world.getOtherEntities(null, currentBox).isEmpty() || WorldUtil.doesBoxCollide(currentBox)) {
                    break;
                }
            }

            for (Box box : futureBoxes) {
                if (playerBox.intersects(box)) {
                    for (Vec3d vel : getMoveVecs(e.getVelocity())) {
                        Box newBox = mc.player.getBoundingBox().offset(vel);

                        if (!WorldUtil.doesBoxCollide(newBox) && futureBoxes.stream().noneMatch(playerBox.offset(vel)::intersects)) {
                            if (vel.y == 0) {
                                Entity veh = mc.player.getVehicle();
                                if (veh != null) {
                                    veh.setVelocity(vel);
                                } else {
                                    mc.player.setVelocity(vel);
                                }
                            } else {
                                Entity veh = mc.player.getVehicle();
                                if (veh != null) {
                                    veh.updatePosition(veh.getX()+vel.x, veh.getY()+vel.y, veh.getZ()+vel.z);
                                    mc.player.networkHandler.sendPacket(
                                            new VehicleMoveC2SPacket(veh));
                                    return;
                                }
                                mc.player.updatePosition(mc.player.getX() + vel.x, mc.player.getY() + vel.y, mc.player.getZ() + vel.z);
                                mc.player.networkHandler.sendPacket(
                                        new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false));
                            }

                            return;
                        }
                    }
                }
            }
        }
    }

    private List<Vec3d> getMoveVecs(Vec3d arrowVec) {
        double speed = 1;

        List<Vec3d> list = new ArrayList<>(Arrays.asList(
                arrowVec.subtract(0, arrowVec.y, 0).normalize().multiply(speed).rotateY((float) -Math.toRadians(90f)),
                arrowVec.subtract(0, arrowVec.y, 0).normalize().multiply(speed).rotateY((float) Math.toRadians(90f))));

        Collections.shuffle(list);

        return list;
    }
}


//public void onTick(EventTick envent) {
//	for (Entity e : mc.world.getEntities()) {
//		if (e.age > 75 || !(e instanceof ArrowEntity) || ((ArrowEntity) e).getOwner() == mc.player)
//			continue;
//
//		int mode = getSetting(0).asMode().getMode();
//		int steps = getSetting(2).asSlider().getValueInt();
//
//		Box playerBox = mc.player.getBoundingBox().expand(0.3);
//		List<Box> futureBoxes = new ArrayList<>(steps);
//
//		Box currentBox = e.getBoundingBox();
//		Vec3d currentVel = e.getVelocity();
//
//		for (int i = 0; i < steps; i++) {
//			currentBox = currentBox.offset(currentVel);
//			currentVel = currentVel.multiply(0.99, 0.94, 0.99);
//			futureBoxes.add(currentBox);
//
//			if (!mc.world.getOtherEntities(null, currentBox).isEmpty() || WorldUtils.doesBoxCollide(currentBox)) {
//				break;
//			}
//		}
//
//		for (Box box: futureBoxes) {
//			if (playerBox.intersects(box)) {
//				for (Vec3d vel : getMoveVecs(e.getVelocity())) {
//					Box newBox = mc.player.getBoundingBox().offset(vel);
//
//					if (!WorldUtils.doesBoxCollide(newBox) && futureBoxes.stream().noneMatch(playerBox.offset(vel)::intersects)) {
//						if (mode == 0 && vel.y == 0) {
//							mc.player.setVelocity(vel);
//						} else {
//							mc.player.updatePosition(mc.player.getX() + vel.x, mc.player.getY() + vel.y, mc.player.getZ() + vel.z);
//							mc.player.networkHandler.sendPacket(
//									new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false));
//						}
//
//						return;
//					}
//				}
//			}
//		}
//	}