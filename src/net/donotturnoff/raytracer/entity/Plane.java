package net.donotturnoff.raytracer.entity;

import net.donotturnoff.raytracer.material.Material;
import net.donotturnoff.raytracer.maths.Rotation;
import net.donotturnoff.raytracer.maths.Vector;
import net.donotturnoff.raytracer.util.BoundingBox;

public class Plane extends Entity {
	
	private Vector normal;
	private Vector point;
	
	public Plane(Vector normal, Vector point, Material material) throws IllegalArgumentException {
		this("", normal, point, material, new BoundingBox());
	}
	
	public Plane(String name, Vector normal, Vector point, Material material) throws IllegalArgumentException {
		this(name, normal, point, material, new BoundingBox());
	}
	
	public Plane(Vector normal, Vector point, Material material, BoundingBox boundingBox) throws IllegalArgumentException {
		this("", normal, point, material, boundingBox);
	}
	
	public Plane(String name, Vector normal, Vector point, Material material, BoundingBox boundingBox) throws IllegalArgumentException {
		
		if (normal.components() != 3) {
			throw new IllegalArgumentException("Normal must have 3 components");
		} else {
			this.name = name;
			this.normal = normal;
			this.point = point;
			this.translation = point;
			this.rotation = new Rotation();
			this.material = material;
			this.boundingBox = boundingBox;
		}
	}
	
	public double getIntersectionParameter(Vector origin, Vector direction) {
		System.out.println(origin + " " + direction);
		return origin.getComponent(2) / direction.getComponent(2);
	}
	
	public Vector getNormal() {
		return normal;
	}
	
	public Vector getNormal(Vector intersection) {
		return normal;
	}
	
	public Vector getSurfaceCoordinates(Vector intersection) {
		double[] array = intersection.getArray();
		return new Vector(array[0], array[1]);
	}
	
	public String toString() {
		StringBuilder info = new StringBuilder();
		info.append("Plane");
		info.append("[name=\"");
		info.append(name);
		info.append("\", normal=");
		info.append(normal);
		info.append("\", point=");
		info.append(point);
		info.append(", material=");
		info.append(material);
		info.append(", boundingbox=");
		info.append(boundingBox);
		info.append("]");
		return info.toString();
	}
}
