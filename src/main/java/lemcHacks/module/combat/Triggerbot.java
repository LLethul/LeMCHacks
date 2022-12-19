package lemcHacks.module.combat;

import lemcHacks.module.Module;
import lemcHacks.module.ModuleManager;
import lemcHacks.module.settings.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;


public class Triggerbot extends Module {
	
	public NumberSetting cps = new NumberSetting("CPS", 1,20,8,1);
	
    public Triggerbot() {
        super("TriggerBot", "clicks for your lazy ahh", Category.COMBAT, true);
    }

    public Entity getEntityInSight(double reach) {
        float tickDelta = 1f;
        Entity target = null;
        Entity entity = mc.getCameraEntity();
        if (entity != null) {
            if (mc.world != null) {
                double d = reach;
                mc.crosshairTarget = entity.raycast(d, tickDelta, false);
                Vec3d vec3d = entity.getCameraPosVec(tickDelta);
                double e = d;

                e *= e;
                if (mc.crosshairTarget != null) {
                    e = mc.crosshairTarget.getPos().squaredDistanceTo(vec3d);
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
    
    @Override
    public void onTick() {
    	if (!isEnabled()) return;
    	
    	int range = 3;
    	if (ModuleManager.Instance.getModule(Reach.class).isEnabled()) range = (int) ModuleManager.Instance.getModule(Reach.class).range.getValue();
    	
    	Entity target = getEntityInSight(range);
    	
    	if (target != null) {
    		boolean reachEnabled = ModuleManager.Instance.getModule(Reach.class).isEnabled();
    		
    		if (reachEnabled) {
    			ModuleManager.Instance.getModule(Reach.class).requestAttack(target);
    		} else {
    			 try {
    				mc.player.swingHand(Hand.MAIN_HAND);
        			PlayerInteractEntityC2SPacket packet = PlayerInteractEntityC2SPacket.attack(target, false);
        			mc.player.networkHandler.sendPacket(packet);
					Thread.sleep((int) Math.round(1000 / cps.getValue()));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    }
}