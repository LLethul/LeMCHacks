package lemcHacks.module.settings;

public class NumberSetting extends Setting {
	
	private double Min, Max, Increment, Value;

	public NumberSetting(String Name, double Min, double Max, double df, double Increment) {
		super(Name);
		this.Min = Min;
		this.Max = Max;
		this.Increment = Increment;
		this.Value = df;
	}
	
	public static double clamp(double value, double min, double max) {
		value = Math.max(min, value);
		value = Math.min(max, value);
		return value;
	}

	public double getValue() {
		return Value;
	}
	
	public double getIncrement() {
		return Increment;
	}
	
	public double getValueFloat() {
		return (float)Value;
	}
	
	public double getValueInt() {
		return (int)Value;
	}
	
	public void setValue(double value) {
		value = clamp(value, Min, Max);
		value = Math.round(value * (1.0 / Increment)) / (1.0 / Increment);
		Value = value;
	}
	
	public void increment(boolean positive) {
		if (positive) setValue(getValue() + getIncrement());
		else setValue(getValue() - getIncrement());
	}

	public double getMin() {
		return Min;
	}

	public double getMax() {
		return Max;
	}

}
