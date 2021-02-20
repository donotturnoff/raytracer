package net.donotturnoff.raytracer.light;

import net.donotturnoff.raytracer.light.Light;
import net.donotturnoff.raytracer.maths.Vector;

import java.awt.Color;

public class Spotlight extends Light {
	
	private Vector direction;
	
	public Spotlight(Color color, Vector direction) throws IllegalArgumentException {
		this("", color, direction);
	}
	
	public Spotlight(String name, Color color, Vector direction) throws IllegalArgumentException {
		super(name, color);
		
		if (direction.components() == 3) {
			if (!direction.isZero()) {
				this.direction = direction;
			} else {
				throw new IllegalArgumentException("Direction vector cannot be zero vector");
			}
		} else {
			throw new IllegalArgumentException("Direction vector must have 3 components");
		}
	}
	
	public Vector getDirection() {
		return direction;
	}
	
	public String toString() {
		StringBuilder infoBuilder = new StringBuilder();
		infoBuilder.append("Spotlight");
		infoBuilder.append("[name=\"");
		infoBuilder.append(name);
		infoBuilder.append("\",color=");
		infoBuilder.append(color);
		infoBuilder.append(",direction=");
		infoBuilder.append(direction);
		infoBuilder.append("]");
		return infoBuilder.toString();
	}
}
