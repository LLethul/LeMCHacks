package lemcHacks.ui.screens.clickgui;

import java.util.ArrayList;
import java.util.List;

import lemcHacks.module.Module.Category;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class ClickGUI extends Screen {
	
	protected ClickGUI() {
		super(Text.literal("ClickGUI"));
		Frames = new ArrayList<>();
		
		int off = 20;
		for (Category cat : Category.values()) {
			Frames.add(new Frame(cat, off, 20, 75, 15));
			off += 100;
		}
	}
	
	public static final ClickGUI Instance = new ClickGUI();
	private List<Frame> Frames;
	
	@Override
	public void render(MatrixStack matrices, int mX, int mY, float delta) {
		for (Frame frame : Frames) {
			frame.render(matrices, mX, mY, delta);
			frame.updatePosition(mX, mY);
		}
		super.render(matrices, mX, mY, delta);
	}
	
	@Override
	public boolean mouseClicked(double mX, double mY, int btn) {
		for (Frame frame : Frames) {
			frame.mouseClicked(mX, mY, btn);
		}
		return super.mouseClicked(mX, mY, btn);
	}
	
	@Override
	public boolean mouseReleased(double mX, double mY, int btn) {
		for (Frame frame : Frames) {
			frame.mouseReleased(mX, mY, btn);
		}
		return super.mouseReleased(mX, mY, btn);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
	    for (Frame frame : Frames) {
	        frame.keyPressed(keyCode);
	    }
	    return super.keyPressed(keyCode, scanCode, modifiers);
	}

	
}
