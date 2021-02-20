package net.donotturnoff.raytracer.material;

import java.awt.Color;

public class BlockColor extends SurfaceMap {
	private Color color;
	
	public BlockColor(int value) throws IllegalArgumentException {
		this(1, 1, new Color(value, value, value));
	}
	
	public BlockColor(int r, int g, int b) throws IllegalArgumentException {
		this(1, 1, new Color(r, g, b));
	}
	
	public BlockColor(Color color) {
		this(1, 1, color);
	}
	
	public BlockColor(int width, int height, int r, int g, int b) throws IllegalArgumentException {
		this(width, height, new Color(r, g, b));
	}
	
	public BlockColor(int width, int height, Color color) {
		super(width, height);
		this.color = color;
		generateData();
	}
	
	private void generateData() {
		int rgb = color.getRGB();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				data[y][x] = rgb;
			}
		}
	}
	
	public String toString() {
		StringBuilder info = new StringBuilder();
		info.append("BlockColor");
		info.append("[width=");
		info.append(width);
		info.append(",height=");
		info.append(height);
		info.append(",color=");
		info.append(color);
		info.append("]");
		return info.toString();
	}
}
