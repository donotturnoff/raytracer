package net.donotturnoff.raytracer.entity;

import net.donotturnoff.raytracer.material.Material;
import net.donotturnoff.raytracer.maths.Rotation;
import net.donotturnoff.raytracer.maths.Vector;
import net.donotturnoff.raytracer.util.BoundingBox;

public class Sphere extends Entity {
	
	private double radius;
	
	public Sphere(Vector center, double radius, Material material) throws IllegalArgumentException {
		this("", center, radius, new Rotation(), material, new BoundingBox());
	}
	
	public Sphere(String name, Vector center, double radius, Material material) throws IllegalArgumentException {
		this(name, center, radius, new Rotation(), material, new BoundingBox());
	}
	
	public Sphere(Vector center, double radius, Rotation rotation, Material material) throws IllegalArgumentException {
		this("", center, radius, rotation, material, new BoundingBox());
	}
	
	public Sphere(String name, Vector center, double radius, Rotation rotation, Material material) throws IllegalArgumentException {
		this(name, center, radius, rotation, material, new BoundingBox());
	}
	
	public Sphere(Vector center, double radius, Rotation rotation, Material material, BoundingBox boundingBox) throws IllegalArgumentException {
		this("", center, radius, rotation, material, boundingBox);
	}
	
	public Sphere(String name, Vector center, double radius, Rotation rotation, Material material, BoundingBox boundingBox) throws IllegalArgumentException {
		
		if (center.components() != 3) {
			throw new IllegalArgumentException("Center must have 3 components");
		} else if (radius < 0) {
			throw new IllegalArgumentException("Radius must be 0 or positive");
		} else if (radius < 0) {
			throw new IllegalArgumentException("Radius must be 0 or positive");
		} else {
			this.name = name;
			this.translation = center;
			this.rotation = rotation;
			this.radius = radius;
			this.material = material;
			this.boundingBox = boundingBox;
		}
	}
	
	public double getIntersectionParameter(Vector origin, Vector direction) {
		double k1 = direction.dot(direction);
		double k2 = 2 * origin.dot(direction);
		double k3 = origin.dot(origin) - radius*radius;
		
		double discriminant = k2*k2 - 4*k1*k3;
		
		if (discriminant < 0) {
			return Double.MAX_VALUE;
		} else {
			double s = Math.sqrt(discriminant);
			double t1 = (-k2 + s) / (2*k1);
			double t2 = (-k2 - s) / (2*k1);
			
			if (t1 < 0) {
				t1 = Double.MAX_VALUE;
			}
			
			if (t2 < 0) {
				t2 = Double.MAX_VALUE;
			}
			
			double min = Math.min(t1, t2);
			double max = Math.max(t1, t2);
			
			if (boundingBoxContains(origin, direction, min)) {
				return min;
			} else if (boundingBoxContains(origin, direction, max)) {
				return max;
			} else {
				return Double.MAX_VALUE;
			}
		}
	}
	
	public Vector getNormal(Vector intersection) {
		return intersection;
	}
	
	public Vector getSurfaceCoordinates(Vector intersection) {
		Vector unit = intersection.normalize();
		double ix = unit.getComponent(0);
		double iy = unit.getComponent(1);
		double iz = unit.getComponent(2);
		double x = 0.5 + Math.atan2(iz, ix)/(2*Math.PI);
		double y = 0.5 - Math.asin(iy)/Math.PI;
		return new Vector(x, y);
	}
	
	public String toString() {
		StringBuilder info = new StringBuilder();
		info.append("Sphere");
		info.append("[name=\"");
		info.append(name);
		info.append("\",center=");
		info.append(translation);
		info.append(",radius=");
		info.append(radius);
		info.append(",material=");
		info.append(material);
		info.append(",boundingbox=");
		info.append(boundingBox);
		info.append("]");
		return info.toString();
	}
}
