package lemcHacks.module.movement;

import org.lwjgl.glfw.GLFW;

import lemcHacks.module.Module;
import lemcHacks.module.settings.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class BoatFly extends Module {

	public MinecraftClient Client = MinecraftClient.getInstance();
	public NumberSetting spd = new NumberSetting("Speed", 0.1, 5, 0.1, 0.1);
	
	public BoatFly() {
		super("BloatFloat", "Boat Float, kinda like flying", Category.MOVEMENT, true);
		this.setKey(GLFW.GLFW_KEY_Y);
		addSetting(spd);
	}
	
	public void onTick() {
		if (!isEnabled()) return;
		Entity vehicle = mc.player.getVehicle();
		
		if (vehicle != null) {
			Vec3d forward = new Vec3d(0, 0, spd.getValue()).rotateY(-(float) Math.toRadians(vehicle.getYaw()));
            Vec3d strafe = forward.rotateY((float) Math.toRadians(90));
			
            if (mc.options.jumpKey.isPressed())
            	vehicle.setVelocity(vehicle.getVelocity().add(0, spd.getValue(), 0));
            if (mc.options.backKey.isPressed())
            	vehicle.setVelocity(vehicle.getVelocity().add(-forward.x, 0, -forward.z));
            if (mc.options.forwardKey.isPressed())
            	vehicle.setVelocity(vehicle.getVelocity().add(forward.x, 0, forward.z));
//            if (mc.options.leftKey.isPressed())
//            	vehicle.setVelocity(vehicle.getVelocity().add(strafe.x, 0, strafe.z));
//            if (mc.options.rightKey.isPressed())
//                vehicle.setVelocity(vehicle.getVelocity().add(-strafe.x, 0, -strafe.z));
		}
	}

}
