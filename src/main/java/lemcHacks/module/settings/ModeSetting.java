package lemcHacks.module.settings;

import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting {
	
	public List<String> Modes;
	public String Mode;
	private int Idx;

	public ModeSetting(String Name, String dfMode, String... modes) {
		super(Name);
		this.Modes = Arrays.asList(modes);
		this.Mode = dfMode;
		this.Idx = Modes.indexOf(this.Mode);
	}

	public List<String> getModes() {
		return Modes;
	}

	public void setModes(List<String> modes) {
		Modes = modes;
	}

	public String getMode() {
		return Mode;
	}

	public void setMode(String mode) {
		Mode = mode;
		this.Idx = Modes.indexOf(this.Mode);
	}

	public int getIdx() {
		return Idx;
	}

	public void setIdx(int idx) {
		Idx = idx;
		Mode = Modes.get(idx);
	}
	
	public void Cycle() {
		if (Idx < Modes.size() - 1) {
			Idx++;
			Mode = Modes.get(Idx);
		} else if (Idx >= Modes.size() - 1) {
			Idx = 0;
			Mode = Modes.get(Idx);
		}
	}
	
	public boolean isMode(String mode) {
		return Mode == mode;
	}
	
}
