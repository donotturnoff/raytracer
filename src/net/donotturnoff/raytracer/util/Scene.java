package net.donotturnoff.raytracer.util;

import net.donotturnoff.raytracer.entity.Entity;
import net.donotturnoff.raytracer.light.Light;
import net.donotturnoff.raytracer.util.Camera;

import java.awt.Color;

public class Scene {
	
	private int width, height;
	private Entity[] entities;
	private Light[] lights;
	private Camera camera;
	private Color ambient;
	private Color backgroundColor;
	private double viewportWidth, viewportHeight, viewportDepth;
	private int recursionLimit;
	private int supersampleGrid, supersampleStochastic;
	
	public Scene(int width, int height, Entity[] entities, Light[] lights, Camera camera, Color ambient, Color backgroundColor, double viewportWidth, double viewportHeight, double viewportDepth, int recursionLimit, int supersampleGrid, int supersampleStochastic) throws IllegalArgumentException {
		if (width <= 0) {
			throw new IllegalArgumentException("Width must be positive");
		} else if (height <= 0) {
			throw new IllegalArgumentException("Height must be positive");
		} else if (viewportWidth <= 0) {
			throw new IllegalArgumentException("Viewport width must be positive");
		} else if (viewportHeight <= 0) {
			throw new IllegalArgumentException("Viewport height must be positive");
		} else if (viewportDepth <= 0) {
			throw new IllegalArgumentException("Viewport depth must be positive");
		} else if (recursionLimit <= 0) {
			throw new IllegalArgumentException("Recursion limit must be positive");
		} else if (supersampleGrid <= 0) {
			throw new IllegalArgumentException("Amount of grid sample points per pixel must be positive");
		} else if (supersampleStochastic < 0) {
			throw new IllegalArgumentException("Amount of stochastic sample points per pixel must be zero or positive");
		} else {
			this.width = width;
			this.height = height;
			this.ambient = ambient;
			this.entities = entities;
			this.lights = lights;
			this.camera = camera;
			this.backgroundColor = backgroundColor;
			this.viewportWidth = viewportWidth;
			this.viewportHeight = viewportHeight;
			this.viewportDepth = viewportDepth;
			this.recursionLimit = recursionLimit;
			this.supersampleGrid = supersampleGrid;
			this.supersampleStochastic = supersampleStochastic;
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Entity[] getEntities() {
		return entities;
	}
	
	public Light[] getLights() {
		return lights;
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public Color getAmbient() {
		return ambient;
	}
	
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	
	public double getViewportWidth() {
		return viewportWidth;
	}
	
	public double getViewportHeight() {
		return viewportHeight;
	}
	
	public double getViewportDepth() {
		return viewportDepth;
	}
	
	public int getRecursionLimit() {
		return recursionLimit;
	}
	
	public int getSupersampleGrid() {
		return supersampleGrid;
	}
	
	public int getSupersampleStochastic() {
		return supersampleStochastic;
	}
}
