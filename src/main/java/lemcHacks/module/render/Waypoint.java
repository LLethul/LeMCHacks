package lemcHacks.module.render;

import lemcHacks.module.Module;
import lemcHacks.util.RenderUtils;
import lemcHacks.util.render.Renderer;
import lemcHacks.util.render.WorldRenderer;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class Waypoint extends Module {

    List<Vec3d> wayp = new ArrayList<>();
    public Waypoint() {
        super("Waypoint", "render waypoints", Category.RENDER, true);
        this.setKey(GLFW.GLFW_KEY_LEFT_BRACKET);
    }

    @Override
    public void onEnable() {
        wayp.add(mc.player.getPos());
        this.setEnabled(false);
    }

    @Override
    public void onTick() {
        for (Vec3d pos : wayp) {
            WorldRenderer.drawText(Text.of(pos.getX()+", "+pos.getZ()), pos.getX(), 100, pos.getZ(), 1, true);
        }
    }

}
