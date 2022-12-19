package lemcHacks.ui.screens.clickgui.setting;

import java.awt.Color;

import lemcHacks.module.settings.BooleanSetting;
import lemcHacks.module.settings.Setting;
import lemcHacks.ui.screens.clickgui.ModuleButton;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

public class CheckBox extends Component {

	private BooleanSetting boolset = (BooleanSetting)setting;

	public CheckBox(Setting setting, ModuleButton parent, int offset) {
		super(setting, parent, offset);
		this.boolset = (BooleanSetting)setting;
	}

	@Override
	public void render(MatrixStack matrices, int mX, int mY, float delta) {
		DrawableHelper.fill(matrices, parent.parent.x, parent.parent.y + parent.offset + offset, parent.parent.x + parent.parent.width, parent.parent.y + parent.offset + offset + parent.parent.height, new Color(0,0,0,160).getRGB());
		int offsetY = ((parent.parent.height / 2) - Client.textRenderer.fontHeight / 2);
		Client.textRenderer.drawWithShadow(matrices, boolset.getName() + ": " + boolset.isEnabled(), parent.parent.x + offsetY, parent.parent.y + parent.offset + offset + offsetY, -1);
		super.render(matrices, mX, mY, delta);
	}

	@Override
	public void mouseClicked(double mX, double mY, int btn) {
		if (isHovered(mX, mY)) {
			if (btn == 0) {
				boolset.Toggle();
			}
		}
		super.mouseClicked(mX, mY, btn);
	}

	@Override
	public void mouseReleased(double mX, double mY, int btn) {
		// TODO Auto-generated method stub
		super.mouseReleased(mX, mY, btn);
	}

}
