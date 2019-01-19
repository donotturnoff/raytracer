import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class SurfaceMap {
	protected int width, height;
	protected int[][] data;
	
	public SurfaceMap() throws IllegalArgumentException {
		this(new int[1][1]);
	}
	
	public SurfaceMap(int width, int height) throws IllegalArgumentException {
		this(new int[height][width]);
	}
	
	public SurfaceMap(int[][] data) throws IllegalArgumentException {
		if (data.length > 0) {
			if (data[0].length > 0) {
				this.data = data;
				this.height = data.length;
				this.width = data[0].length;
			} else {
				throw new IllegalArgumentException("Width must be positive");
			}
		} else {
			throw new IllegalArgumentException("Height must be positive");
		}
	}
	
	protected int getWidth() {
		return width;
	}
	
	protected int getHeight() {
		return height;
	}
	
	protected int[][] getData() {
		return data;
	}
	
	protected int getDatum(int x, int y) {
		return data[y][x];
	}
	
	public String toString() {
		StringBuilder info = new StringBuilder();
		info.append("SurfaceMap");
		info.append("[width=");
		info.append(width);
		info.append(",height=");
		info.append(height);
		info.append("]");
		return info.toString();
	}
	
	public BufferedImage toImage() {
		int[] data1D = new int[width*height];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int index = y*width + x;
				data1D[index] = data[y][x];
			}
		}
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		final int[] imageArray = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(data1D, 0, imageArray, 0, data1D.length);
		return image;
	}
}
