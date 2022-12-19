package lemcHacks.module.movement;

import org.lwjgl.glfw.GLFW;

import lemcHacks.module.Module;
import lemcHacks.module.settings.KeybindSetting;
import net.minecraft.client.MinecraftClient;

public class Sprint extends Module {

	public MinecraftClient Client = MinecraftClient.getInstance();
	public KeybindSetting key = new KeybindSetting("Key", GLFW.GLFW_KEY_V);
	
	public Sprint() {
		super("Sprint", "Auto sprint", Category.MOVEMENT, true);
		this.key.setKey(GLFW.GLFW_KEY_V);
	}
	
	public void onTick() {
		Client.player.setSprinting(isEnabled());
		super.onTick();
	}

}
