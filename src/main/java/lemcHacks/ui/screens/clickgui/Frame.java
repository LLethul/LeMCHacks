package lemcHacks.ui.screens.clickgui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import lemcHacks.module.Module.Category;
import lemcHacks.module.ModuleManager;
import lemcHacks.ui.screens.clickgui.setting.Component;
import lemcHacks.util.FontRenderer;
import lemcHacks.util.Rainbow;
import lemcHacks.module.Module;
import lemcHacks.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class Frame {
	
	public int x,y, width, height, dragX, dragY;
	public Category Category;
	public boolean Dragging = false, extended;
	private static Rainbow rc = new Rainbow();
	
	private List<ModuleButton> buttons;
	
	public MinecraftClient Client = MinecraftClient.getInstance();
	
	public Frame(Category cat, int x, int y, int width, int height) {
		Category = cat;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		buttons = new ArrayList<>();
		
		int offset = height;
		for (Module mod : ModuleManager.Instance.getModulesInCategory(Category)) {
			buttons.add(new ModuleButton(mod, this, offset));
			offset += height;
		}
	}
	
	public void render(MatrixStack matrices, int mX, int mY, float delta) {
		DrawableHelper.fill(matrices, x, y, x + width, y + height, Color.black.getRGB());
		
		int offset =((height / 2) - Client.textRenderer.fontHeight / 2);

        Client.textRenderer.drawWithShadow(matrices, Category.name, x + 2, y + ((height / 2) - Client.textRenderer.fontHeight / 2), -1);
        Client.textRenderer.drawWithShadow(matrices, extended ? "-" : "+", x + width- offset- 2 - Client.textRenderer.getWidth("+"), y + ((height / 2) - Client.textRenderer.fontHeight / 2), -1);

		for (ModuleButton btn : buttons) {
			if (extended) {
				btn.render(matrices, mX, mY, delta);
			}
		}
		
		rc.update(10);
	}

	public void mouseClicked(double mX, double mY, int btn) {
		// TODO Auto-generated method stub
		if (isHovered(mX, mY)) {
			if (btn == 0) {
				Dragging = true;
				dragX = (int)(mX - x);
				dragY = (int)(mY - y);
			} else if (btn == 1) {
				extended = !extended;
			}
		}
		
		for (ModuleButton btna : buttons) {
			if (extended) {
				btna.mouseClicked(mX, mY, btn);
			}
		}
	}
	
	public void mouseReleased(double mX, double mY, int btn) {
		if (btn == 0 && Dragging == true) Dragging = false;
		
		for (ModuleButton btna : buttons) {
			if (extended) {
				btna.mouseReleased(mX, mY, btn);
			}
		}
	}
	
	public boolean isHovered(double mX, double mY) {
		return mX > x && mX < x + width && mY > y && mY < y + height;
	}
	
	public void updatePosition(double mX, double mY) {
		if (Dragging) {
			x = (int)(mX - dragX);
			y = (int)(mY - dragY);
		}
	}

	public void updateButtons() {
        int offset = height;
        for (ModuleButton button : buttons) {
            button.offset = offset;
            offset += height;

            if (button.extended) {
                for (Component component : button.components) {
                    if (component.setting.isVisible()) offset += height;
                }
            }
        }
    }
	
	public void keyPressed(int key) {
        for (ModuleButton mb : buttons) {
            mb.keyPressed(key);
        }
	}

	
}
