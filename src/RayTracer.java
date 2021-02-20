import net.donotturnoff.raytracer.maths.Vector;
import net.donotturnoff.raytracer.util.Camera;
import net.donotturnoff.raytracer.util.Ray;
import net.donotturnoff.raytracer.util.Scene;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.Color;

public class RayTracer {
	
	private Scene scene;
	private Camera camera;
	private Vector origin;
	private boolean debug;
	private int threads;
	
	public RayTracer() {
		debug = false;
		threads = 1;
	}
	
	public RayTracer(boolean debug) {
		this.debug = debug;
		threads = 1;
	}
	
	public RayTracer(int threads) {
		debug = false;
		this.threads = threads;
	}
	
	public RayTracer(int threads, boolean debug) {
		this.debug = debug;
		this.threads = threads;
	}
	
	public BufferedImage trace(Scene scene) {
		this.scene = scene;
		camera = scene.getCamera();
		origin = camera.getLocation();
		
		int width = scene.getWidth();
		int height = scene.getHeight();
		int[][] data = new int[height][width];
		
		double scaleX = scene.getViewportWidth() / width;
		double scaleY = scene.getViewportHeight() / height;
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (debug) {
					System.out.println("Rendering (" + (x+1) + ", " + (y+1) + ")");
				}
				int r = 0;
				int g = 0;
				int b = 0;
				int samples = 0;
				
				double ox = x-width/2;
				double oy = y-height/2;
				
				int gridSize = scene.getSupersampleGrid();
				int stochasticPoints = scene.getSupersampleStochastic();
				for (int subY = 0; subY < gridSize; subY++) {
					for (int subX = 0; subX < gridSize; subX++) {
						double sx = (ox + (double) subX/gridSize)*scaleX;
						double sy = (oy + (double) subY/gridSize)*scaleY;
						int[] components = castRay(sx, sy);
						r += components[0];
						g += components[1];
						b += components[2];
						samples++;
					}
				}
				
				for (int sto = 0; sto < stochasticPoints; sto++) {
					double sx = (ox + Math.random())*scaleX;
					double sy = (oy + Math.random())*scaleY;
					int[] components = castRay(sx, sy);
					r += components[0];
					g += components[1];
					b += components[2];
					samples++;
				}
				
				r /= samples;
				g /= samples;
				b /= samples;
				
				data[height-y-1][x] = new Color(r, g, b).getRGB();
			}
		}
		
		return toImage(width, height, data);
	}
	
	private int[] castRay(double x, double y) {
		Vector direction = new Vector(x, y, scene.getViewportDepth());
		direction = camera.rotateIntoCameraSpace(direction);
		Ray ray = new Ray(scene, origin, direction, 0, 0, null);
		Color color = ray.getColor();
		return new int[]{color.getRed(), color.getGreen(), color.getBlue()};
	}
	
	private BufferedImage toImage(int width, int height, int[][] data) {
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
	
	public void setDebugging(boolean debug) {
		this.debug = debug;
	}
}
