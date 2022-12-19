package lemcHacks.module.combat;

import net.minecraft.entity.passive.HorseEntity;
import org.w3c.dom.Text;

import lemcHacks.event.events.EventReach;
import lemcHacks.eventbus.LeMCSubscribe;
import lemcHacks.module.Module;

import lemcHacks.module.settings.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class Reach extends Module {
    public NumberSetting range = new NumberSetting("range", 3, 500, 35, 1);

    public Reach() {
        super("reach", "Reach hax, dumbass", Category.COMBAT, true);
        addSettings(range);
    }
    
    private void critical() {
        if (mc.player.isClimbing() || mc.player.isTouchingWater()
                || mc.player.hasStatusEffect(StatusEffects.BLINDNESS) || mc.player.hasVehicle()) {
            return;
        }

        boolean sprinting = mc.player.isSprinting();
        if (sprinting) {
            mc.player.setSprinting(false);
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
        }

        if (mc.player.isOnGround()) {
            double x = mc.player.getX();
            double y = mc.player.getY();
            double z = mc.player.getZ();

            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y + 0.0633, z, false));
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, false));
        }

        if (sprinting) {
            mc.player.setSprinting(true);
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
        }
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
    
    public void teleport(Vec3d from, Vec3d endPos){
    	double dist = Math.sqrt(mc.player.squaredDistanceTo(endPos.getX(), endPos.getY(), endPos.getZ()));
    	if (dist > range.getValue()) return;
    	double packetDist = 8.5;

        Entity veh = mc.player.getVehicle();
        Double tDist = Math.ceil(from.distanceTo(endPos) / packetDist);

        if (veh != null) {
            for (int i = 1; i <= tDist; i++) {
                Vec3d tPos = from.lerp(endPos, i/tDist);
                veh.updatePosition(tPos.x, tPos.y, tPos.z);
                veh.setOnGround(false);
                mc.player.updatePosition(tPos.x, tPos.y, tPos.z);
                VehicleMoveC2SPacket p = new VehicleMoveC2SPacket(veh);
                mc.player.networkHandler.sendPacket(p);

                if (i%4==0) {
                    try {
                        Thread.sleep((long)((1/20)*1000));
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

            return;
        }

    	for (int i = 1; i <= tDist; i++) {
    		Vec3d tPos = from.lerp(endPos, i/tDist);
    		PlayerMoveC2SPacket p = new PlayerMoveC2SPacket.PositionAndOnGround(tPos.x, tPos.y, tPos.z, false);
    		mc.player.networkHandler.sendPacket(p);
    		
    		if (i%4==0) {
    			try {
					Thread.sleep((long)((1/20)*1000));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    }
    
    @Override
    public void onTick() {
        var target = getEntityInSight(range.getValue());
        if (target != null && isEnabled()) {
        	Vec3d bfPos = mc.player.getPos();
        	Vec3d targPos = target.getPos();
        	double dist = Math.sqrt(mc.player.squaredDistanceTo(targPos.getX(), targPos.getY(), targPos.getZ()));
            dist = Math.floor(dist);
        	mc.inGameHud.setOverlayMessage(net.minecraft.text.Text.of("Found entity \""+target.getName().getString()+"\" (eName: "+target.getEntityName()+") ("+dist+"m)"), false);
            if (mc.options.attackKey.isPressed()) {
            	targPos = target.getPos().add(new Vec3d(2,0,2));
            	teleport(bfPos, targPos);
                //logger.info("attack entity!");
                PlayerInteractEntityC2SPacket packet = PlayerInteractEntityC2SPacket.attack(target, false);
                mc.player.networkHandler.sendPacket(packet);
                critical();
                mc.player.swingHand(mc.player.getActiveHand());
                
                teleport(targPos, bfPos);
            }
        } else if (target == null && isEnabled()) {
        	mc.inGameHud.setOverlayMessage(net.minecraft.text.Text.of(""), true);
        }
        
        //mc.inGameHud.setOverlayMessage(net.minecraft.text.Text.of(""), true);
    }
    
    @LeMCSubscribe
	public void onReach(EventReach event) {
		event.setReach(event.getReach() + (float)range.getValue());
	}
    
    public void requestAttack(Entity target) {
    	if (target != null && isEnabled()) {
        	Vec3d bfPos = mc.player.getPos();
        	Vec3d targPos = target.getPos();
        	double dist = Math.sqrt(mc.player.squaredDistanceTo(targPos.getX(), targPos.getY(), targPos.getZ()));
            dist = Math.floor(dist);
        	mc.inGameHud.setOverlayMessage(net.minecraft.text.Text.of("Found entity ("+dist+"m)"), false);
            if (mc.options.attackKey.isPressed()) {
            	targPos = target.getPos().add(new Vec3d(2,0,2));
            	teleport(bfPos, targPos);
                //logger.info("attack entity!");
                PlayerInteractEntityC2SPacket packet = PlayerInteractEntityC2SPacket.attack(target, false);
                mc.player.networkHandler.sendPacket(packet);
                critical();
                mc.player.swingHand(mc.player.getActiveHand());
                
                teleport(targPos, bfPos);
            }
        } else if (target == null && isEnabled()) {
        	mc.inGameHud.setOverlayMessage(net.minecraft.text.Text.of(""), true);
        }
    }
    
}