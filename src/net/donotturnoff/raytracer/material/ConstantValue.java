package net.donotturnoff.raytracer.material;

import net.donotturnoff.raytracer.material.SurfaceMap;

public class ConstantValue extends SurfaceMap {
	private int value;
	
	public ConstantValue(int value) {
		this(1, 1, value);
	}
	
	public ConstantValue(int width, int height, int value) {
		super(width, height);
		this.value = value;
		generateData();
	}
	
	private void generateData() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				data[y][x] = value;
			}
		}
	}
	
	public String toString() {
		StringBuilder info = new StringBuilder();
		info.append("ConstantValue");
		info.append("[width=");
		info.append(width);
		info.append(", height=");
		info.append(height);
		info.append(", value=");
		info.append(value);
		info.append("]");
		return info.toString();
	}
}
