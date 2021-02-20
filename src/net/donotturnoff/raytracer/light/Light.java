package net.donotturnoff.raytracer.light;

import java.awt.Color;

public class Light {
	
	protected String name;
	protected Color color;
	
	public Light(Color color) throws IllegalArgumentException {
		this("", color);
	}
	
	public Light(String name, Color color) throws IllegalArgumentException {
		this.name = name;
		this.color = color;
	}
	
	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return color;
	}
}
