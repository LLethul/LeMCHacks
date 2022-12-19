package lemcHacks.module.settings;

public class Setting {

	private String Name;
	private boolean Visible = true;
	
	public Setting(String Name) {
		this.Name = Name;
	}
	
	public boolean isVisible() {
		return Visible;
	}
	public void setVisible(boolean visible) {
		Visible = visible;
	}
	public String getName() {
		return Name;
	}
	
	
	
}
