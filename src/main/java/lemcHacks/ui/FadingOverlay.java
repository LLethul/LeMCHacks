package lemcHacks.ui;

import java.awt.Color;

import net.minecraft.client.MinecraftClient;

public class FadingOverlay {
	private String message = "This is a fading overlay message";

	public void render() {
	    drawMessage();
	}

	@SuppressWarnings("resource")
	private void drawMessage() {
	    // set the font and color
	    MinecraftClient.getInstance().textRenderer.draw(message, 10, 10, (int)-1, false, null, null, false, new Color(0,0,0,0).getRGB(), 0);
	}

}