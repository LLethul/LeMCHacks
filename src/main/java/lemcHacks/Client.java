package lemcHacks;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import lemcHacks.module.ModuleManager;
import lemcHacks.module.combat.Reach;
import lemcHacks.ui.screens.clickgui.ClickGUI;
import lemcHacks.util.world.WorldUtil;
import lemcHacks.eventbus.LeMCEventBus;
import lemcHacks.module.Module;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.FontManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import lemcHacks.eventbus.handler.*;

import java.io.File;

public class Client implements ModInitializer {

	public static final Client Instance = new Client();
	public Logger logger = LogManager.getLogger(Client.class);
	public MinecraftClient Client = MinecraftClient.getInstance();
	public boolean RSUIEnabled = false;
	public LeMCEventBus eventBus = new LeMCEventBus(new InexactEventHandler("lemc"), logger);
	public static FontManager friendMang;
	
	@Override
	public void onInitialize() {
		logger.info("Hello, dickhead!");
	}
	
	public Entity getEntityInSight(double reach) {
        float tickDelta = 1f;
        Entity target = null;
        Entity entity = Client.getCameraEntity();
        if (entity != null) {
            if (Client.world != null) {
                double d = reach;
                Client.crosshairTarget = entity.raycast(d, tickDelta, false);
                Vec3d vec3d = entity.getCameraPosVec(tickDelta);
                double e = d;

                e *= e;
                if (Client.crosshairTarget != null) {
                    e = Client.crosshairTarget.getPos().squaredDistanceTo(vec3d);
                }

                Vec3d vec3d2 = entity.getRotationVec(1.0F);
                Vec3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
                float f = 1.0F;
                Box box = entity.getBoundingBox().stretch(vec3d2.multiply(d)).expand(1.0, 1.0, 1.0);
                EntityHitResult entityHitResult = ProjectileUtil.raycast(entity, vec3d, vec3d3, box, (entityx) -> {
                    return !entityx.isSpectator() && entityx.canHit();
                }, e);
                if (entityHitResult != null) {
                    Entity entity2 = entityHitResult.getEntity();
                    Vec3d vec3d4 = entityHitResult.getPos();
                    double g = vec3d.squaredDistanceTo(vec3d4);
                    if (entity2 instanceof LivingEntity || entity2 instanceof ItemFrameEntity) {
                        target = entity2;
                    }
                }
            }
        }
        
        return target;
    }
	
	private void toggleClickGUI() {
		if (RSUIEnabled == true) {
			RSUIEnabled = false;
			Client.setScreen(null);
		} else {
			RSUIEnabled = true;
			Client.setScreen(ClickGUI.Instance);
		}
	}
	
	public void onKeyPress(int key, int action) {
		if (action == GLFW.GLFW_PRESS) {
			for (Module mod : ModuleManager.Instance.getModules()) {
				if (key == mod.getKey()) {
					mod.toggle();
				}
			}
			
			if (key == GLFW.GLFW_MOUSE_BUTTON_2) {
				Entity target = getEntityInSight(ModuleManager.Instance.getModule(Reach.class).range.getValue());
				if (target != null) {
					// set position to aim at
		            double d = Client.player.getPos().add(0, Client.player.getEyeHeight(Client.player.getPose()), 0)
		                    .distanceTo(target.getBoundingBox().getCenter());
		            double x = target.getX() + (target.getX() - target.prevX) * d
		                    - Client.player.getX();
		            double y = target.getY() + (target.getY() - target.prevY) * d
		                    + target.getHeight() * 0.5 - Client.player.getY()
		                    - Client.player.getEyeHeight(Client.player.getPose());
		            double z = target.getZ() + (target.getZ() - target.prevZ) * d
		                    - Client.player.getZ();

		            // set yaw
		            Client.player.setYaw((float) Math.toDegrees(Math.atan2(z, x)) - 90);
		            WorldUtil.facePos(target.getX(), target.getY() + target.getHeight() / 2, target.getZ());
				}
			}
			
			if (key == GLFW.GLFW_KEY_RIGHT_SHIFT) {
				toggleClickGUI();
			}
		}
		
		if (key == GLFW.GLFW_KEY_RIGHT_SHIFT) {
			if (Client.currentScreen == ClickGUI.Instance) {
				RSUIEnabled = false;
				Client.setScreen(null);
			} else {
				RSUIEnabled = true;
				Client.setScreen(ClickGUI.Instance);
			}
		}

	}
	
	public void onTick() {
		if (Client.player !=  null) {
			for (Module mod : ModuleManager.Instance.getModules()) {
				mod.onTick();
			}
		}
	}

}
