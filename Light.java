import java.awt.Color;

public class Light {
	
	protected String name;
	protected Color color;
	protected double intensity;
	
	public Light(Color color, double intensity) throws IllegalArgumentException {
		this("", color, intensity);
	}
	
	public Light(String name, Color color, double intensity) throws IllegalArgumentException {
		if (intensity >= 0) {
			this.name = name;
			this.color = color;
			this.intensity = intensity;
		} else {
			throw new IllegalArgumentException("Intensity cannot be negative");
		}
	}
	
	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return color;
	}
	
	public double getIntensity() {
		return intensity;
	}
}
