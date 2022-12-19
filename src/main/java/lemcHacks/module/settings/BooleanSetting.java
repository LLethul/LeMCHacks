package lemcHacks.module.settings;

public class BooleanSetting extends Setting {
	
	public boolean Enabled;

	public BooleanSetting(String Name, boolean dfEnabled) {
		super(Name);
		this.Enabled = dfEnabled;
	}

	public void Toggle() {
		this.Enabled = !this.Enabled;
	}

	public boolean isEnabled() {
		return Enabled;
	}

	public void setEnabled(boolean enabled) {
		Enabled = enabled;
	}
	
}
