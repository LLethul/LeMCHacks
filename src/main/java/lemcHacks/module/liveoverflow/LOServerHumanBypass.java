package lemcHacks.module.liveoverflow;

import lemcHacks.event.events.ClientMoveEvent;
import lemcHacks.event.events.PacketEvent;
import lemcHacks.eventbus.LeMCSubscribe;
import lemcHacks.mixins.PlayerMoveC2SPacketAccessor;
import lemcHacks.mixins.VehicleMoveC2SPacketAccessor;
import lemcHacks.module.Module;
import lemcHacks.util.RoundPosition;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class LOServerHumanBypass extends Module {
	public LOServerHumanBypass() {
	    super("LOServerBypass", "Bypass anti-human check for LO server.", Category.LIVEOVERFLOW, true);
		this.setEnabled(true);
	}

//	@LeMCSubscribe
//	public void onPacketSend(PacketEvent.Send p) {
//
//
//
//		if(p.getPacket() instanceof PlayerMoveC2SPacket){
//			double x = ((int)(((PlayerMoveC2SPacket) p.getPacket()).getX(mc.player.getX()) * 100)) / 100.0;
//			double z = ((int)(((PlayerMoveC2SPacket) p.getPacket()).getZ(mc.player.getZ()) * 100)) / 100.0;
//
//			((PlayerMoveC2SPacketAccessor) p.getPacket()).setX(RoundPosition.roundCoordinate(((PlayerMoveC2SPacket) p.getPacket()).getX((mc.player.getX()) * 100)));
//			((PlayerMoveC2SPacketAccessor) p.getPacket()).setZ(RoundPosition.roundCoordinate(((PlayerMoveC2SPacket) p.getPacket()).getZ((mc.player.getX()) * 100)));
//		}
//
//		if(!(mc.player.getVehicle() instanceof BoatEntity boat)){return;}
//
//		if(p.getPacket() instanceof VehicleMoveC2SPacket){
//			double boatx = ((int)(((VehicleMoveC2SPacket) p.getPacket()).getX() * 100)) / 100.0;
//			double boatz = ((int)(((VehicleMoveC2SPacket) p.getPacket()).getZ() * 100)) / 100.0;
//
//			((VehicleMoveC2SPacketAccessor) p).setX(boatx);
//			((VehicleMoveC2SPacketAccessor) p).setZ(boatz);
//		}
//
//	}
}
