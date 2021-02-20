package net.donotturnoff.raytracer.light;

import net.donotturnoff.raytracer.maths.Vector;

import java.awt.Color;

public class Lamp extends Light {
	
	private Vector location;
	private double radius;
	
	public Lamp(Color color, Vector location) throws IllegalArgumentException {
		this("", color, location, 0);
	}
	
	public Lamp(Color color, Vector location, double radius) throws IllegalArgumentException {
		this("", color, location, radius);
	}
	
	public Lamp(String name, Color color, Vector location, double radius) throws IllegalArgumentException {
		super(name, color);
		if (location.components() == 3) {
			if (radius >= 0) {
				this.location = location;
				this.radius = radius;
			} else {
				throw new IllegalArgumentException("Radius cannot be negative");
			}
		} else {
			throw new IllegalArgumentException("Location vector must have 3 components");
		}
	}
	
	public Vector getLocation() {
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
		infoBuilder.append(",location=");
		infoBuilder.append(location);
		infoBuilder.append(",radius=");
		infoBuilder.append(radius);
		infoBuilder.append("]");
		return infoBuilder.toString();
	}
}
