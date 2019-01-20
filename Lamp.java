import java.awt.Color;
import java.util.Arrays;

public class Lamp extends Light {
	
	private double[] location;
	private double radius;
	
	public Lamp(Color color, double intensity, double[] location) throws IllegalArgumentException {
		this("", color, intensity, location, 0);
	}
	
	public Lamp(Color color, double intensity, double[] location, double radius) throws IllegalArgumentException {
		this("", color, intensity, location, radius);
	}
	
	public Lamp(String name, Color color, double intensity, double[] location, double radius) throws IllegalArgumentException {
		super(name, color, intensity);
		if (location.length == 3) {
			if (radius >= 0) {
				this.location = location;
				this.radius = radius;
			} else {
				throw new IllegalArgumentException("Radius cannot be negative");
			}
		} else {
			throw new IllegalArgumentException("Location array must have 3 components");
		}
	}
	
	public double[] getLocation() {
		return location;
	}
	
	public double getRadius() {
		return radius;
	}
	
	public String toString() {
		StringBuilder infoBuilder = new StringBuilder();
		infoBuilder.append("Lamp");
		infoBuilder.append("[name=\"");
		infoBuilder.append(name);
		infoBuilder.append("\",color=");
		infoBuilder.append(color);
		infoBuilder.append(",intensity=");
		infoBuilder.append(intensity);
		infoBuilder.append(",location=");
		infoBuilder.append(Arrays.toString(location));
		infoBuilder.append(",radius=");
		infoBuilder.append(radius);
		infoBuilder.append("]");
		return infoBuilder.toString();
	}
}
