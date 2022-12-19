package lemcHacks.event.events;

import lemcHacks.event.Event;

public class EventBlockBreakCooldown extends Event {

	private int cooldown;

	public EventBlockBreakCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

}