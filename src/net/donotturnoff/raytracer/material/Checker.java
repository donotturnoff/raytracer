package net.donotturnoff.raytracer.material;

import java.awt.Color;

public class Checker extends SurfaceMap {
	private int checker;
	private Color color1, color2;
	
	public Checker(int width, int height, int checker, Color color1, Color color2) throws IllegalArgumentException {
		super(width, height);
		if (checker > 0) {
			this.checker = checker;
			this.color1 = color1;
			this.color2 = color2;
			generateData();
		} else {
			throw new IllegalArgumentException("Checker must be positive");
		}
	}
	
	private void generateData() {
		int rgb1 = color1.getRGB();
		int rgb2 = color2.getRGB();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				data[y][x] = isColor1(x, y) ? rgb1 : rgb2;
			}
		}
	}
	
	private boolean isColor1(int x, int y) {
		return !(((x / checker) % 2 == 0) ^ ((y / checker) % 2 == 0));
	}
	
	public String toString() {
		StringBuilder info = new StringBuilder();
		info.append("Checker");
		info.append("[width=");
		info.append(width);
		info.append(",height=");
		info.append(height);
		info.append(",checker=");
		info.append(checker);
		info.append(",color1=");
		info.append(color1);
		info.append(",color2=");
		info.append(color2);
		info.append("]");
		return info.toString();
	}
}
