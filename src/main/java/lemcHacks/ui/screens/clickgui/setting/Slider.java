package lemcHacks.ui.screens.clickgui.setting;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;

import lemcHacks.module.settings.NumberSetting;
import lemcHacks.module.settings.Setting;
import lemcHacks.ui.screens.clickgui.ModuleButton;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

public class Slider extends Component {

	private NumberSetting numset = (NumberSetting)setting;
	private boolean sliding = false;

	public Slider(Setting setting, ModuleButton parent, int offset) {
		super(setting, parent, offset);
		this.numset = (NumberSetting)setting;
	}

	@Override
	public void render(MatrixStack matrices, int mX, int mY, float delta) {
		DrawableHelper.fill(matrices, parent.parent.x, parent.parent.y + parent.offset + offset, parent.parent.x + parent.parent.width, parent.parent.y + parent.offset + offset + parent.parent.height, new Color(0,0,0,160).getRGB());
		
		double diff = Math.min(parent.parent.width, Math.max(0, mX - parent.parent.x));
		int renderWidth = (int)(parent.parent.width * (numset.getValue() - numset.getMin()) / (numset.getMax() - numset.getMin()));
		
		DrawableHelper.fill(matrices, parent.parent.x, parent.parent.y + parent.offset + offset, parent.parent.x + renderWidth, parent.parent.y + parent.offset + offset + parent.parent.height, Color.red.darker().getRGB());
		
		if (sliding) {
			if (diff == 0) {
				numset.setValue(numset.getMin());
			} else {
				numset.setValue(roundToPlace(((diff / parent.parent.width) * (numset.getMax() - numset.getMin()) + numset.getMin()), 2));
			}
		}
		
		int offsetY = ((parent.parent.height / 2) - Client.textRenderer.fontHeight / 2);
		Client.textRenderer.drawWithShadow(matrices, numset.getName() + ": " + roundToPlace(numset.getValue(), 1), parent.parent.x + offsetY, parent.parent.y + parent.offset + offset + offsetY, -1);
		super.render(matrices, mX, mY, delta);
	}

	@Override
	public void mouseClicked(double mX, double mY, int btn) {
		if (isHovered(mX, mY)) {
			if (btn == 0) {
				sliding = true;
			}
		}
		super.mouseClicked(mX, mY, btn);
	}

	@Override
	public void mouseReleased(double mX, double mY, int btn) {
		sliding = false;
		super.mouseReleased(mX, mY, btn);
	}
	
	private double roundToPlace(double value, int place) {
		if (place < 0) {
			return value;
		}
		
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(place, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}
