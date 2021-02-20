package net.donotturnoff.raytracer.material;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Texture extends SurfaceMap {
	private String path;
	
	public Texture(String path) throws IOException {
		this.path = path;
		generateData();
	}
	
	private void generateData() throws IOException {
		BufferedImage image = ImageIO.read(new File(path));
		width = image.getWidth();
		height = image.getHeight();
		data = new int[height][width];
		
		final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		final boolean alphaChannel = image.getAlphaRaster() != null;
		final int pixelLength = alphaChannel ? 4 : 3;
		
		for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
			int argb = 0;
			if (alphaChannel) {
				argb += (((int) pixels[pixel] & 0xff) << 24);		// Alpha
				argb += ((int) pixels[pixel + 1] & 0xff);			// Blue
				argb += (((int) pixels[pixel + 2] & 0xff) << 8);	// Green
				argb += (((int) pixels[pixel + 3] & 0xff) << 16);	// Red
			} else {
				argb += -16777216;									// Alpha 255
				argb += ((int) pixels[pixel] & 0xff);				// Blue
				argb += (((int) pixels[pixel + 1] & 0xff) << 8);	// Green
				argb += (((int) pixels[pixel + 2] & 0xff) << 16);	// Red
			}
			data[row][col] = argb;
			col++;
			if (col == width) {
				col = 0;
				row++;
			}
		}
	}
	
	public String toString() {
		StringBuilder info = new StringBuilder();
		info.append("Texture");
		info.append("[width=");
		info.append(width);
		info.append(",height=");
		info.append(height);
		info.append(",path=\"");
		info.append(path);
		info.append("\"]");
		return info.toString();
	}
}
