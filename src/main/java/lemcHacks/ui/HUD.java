package lemcHacks.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Comparator;
import java.util.List;

import lemcHacks.module.Module;
import lemcHacks.module.ModuleManager;
import lemcHacks.util.Rainbow;

public class HUD {
	
	public static MinecraftClient Client = MinecraftClient.getInstance();
	private static Rainbow rc = new Rainbow();

	public static void Render(MatrixStack matrices, float tickDelta) {
		renderArrayList(matrices);
	}
	
	public static void renderArrayList(MatrixStack matrices) {
		int index = 1;
		int sWidth = Client.getWindow().getScaledWidth();
		//int sHeight = Client.getWindow().getScaledHeight();
		
		int textWidthl = Client.textRenderer.getWidth("'Le Minecraft Hackus");
		Client.textRenderer.drawWithShadow(matrices, "'Le Minecraft Hackus", (sWidth - 4) - textWidthl, 10 + (0 * Client.textRenderer.fontHeight), -1);
		
		for (Module mod : ModuleManager.Instance.getModules()) {
			//int color = 0xFF00de16;
			if (mod.isEnabled() == false) continue;
			int textWidth = Client.textRenderer.getWidth(mod.getName());
			Client.textRenderer.drawWithShadow(matrices, mod.getName(), (sWidth - 4) - textWidth, 10 + (index * Client.textRenderer.fontHeight), rc.getColor().getColorAsInt());
			index++;
		}
		
		rc.update(20);
	}
	
}
