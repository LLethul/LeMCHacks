package lemcHacks.util;

import lemcHacks.util.Color;

public class Rainbow {
	private Color color;
	private float timer = 0f;

	public Rainbow() {
		this.color = new Color(255, 0, 0);
	}

	public void update(float timerIncrement) {
		if (timer >= (20 - timerIncrement)) {
			timer = 0f;
			this.color.setHSV(((this.color.hue + 1f) % 361), 1f, 1f);
		} else {
			timer++;
		}

	}

	public Color getColor() {
		return this.color;
	}
	public java.awt.Color getJavaColor() {
		return new java.awt.Color(this.color.r, this.color.g, this.color.b, 255);
	}
}