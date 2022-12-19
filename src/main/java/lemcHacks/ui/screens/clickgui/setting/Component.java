package lemcHacks.ui.screens.clickgui.setting;

import lemcHacks.module.settings.Setting;
import lemcHacks.ui.screens.clickgui.ModuleButton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class Component {
	public Setting setting;
	public ModuleButton parent;
	public int offset;
	public MinecraftClient Client = MinecraftClient.getInstance();
	
	public Component(Setting setting, ModuleButton parent, int offset) {
		this.setting = setting;
		this.parent = parent;
		this.offset = offset;
	}
	
	public void render(MatrixStack matrices, int mX, int mY, float delta) {
		
	}
	
	public boolean isHovered(double mouseX, double mouseY) {
        return mouseX > parent.parent.x && mouseX < parent.parent.x+ parent.parent.width && mouseY > parent.parent.y +parent.offset + offset && mouseY < parent.parent.y + parent.offset + offset + parent.parent.height;
    }
	
	
	public void mouseClicked(double mX, double mY, int btn) {
		
	}
	
	public void mouseReleased(double mX, double mY, int btn) {
		
	}
	
	public void keyPressed(int key) {
    }
}
