package lemcHacks.event.events;
import lemcHacks.event.Event;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;

public class ClientMoveEvent extends Event {
    private MovementType type;
    private Vec3d vec;

    public ClientMoveEvent(MovementType type, Vec3d vec) {
        this.type = type;
        this.vec = vec;
        System.out.println("Packet move");
    }

    public MovementType getType() {
        return type;
    }

    public void setType(MovementType type) {
        this.type = type;
    }

    public Vec3d getVec() {
        return vec;
    }

    public void setVec(Vec3d vec) {
        this.vec = vec;
    }

}