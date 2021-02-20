package net.donotturnoff.raytracer.util;

import net.donotturnoff.raytracer.maths.Matrix;
import net.donotturnoff.raytracer.maths.Rotation;
import net.donotturnoff.raytracer.maths.Vector;

public class BoundingBox {
	
	private Vector location;
	private Rotation rotation;
	private double width, height, depth;
	
	public BoundingBox() {
		this.location = new Vector(0, 0, 0);
		this.rotation = new Rotation();
		this.width = Double.MAX_VALUE;
		this.height = Double.MAX_VALUE;
		this.depth = Double.MAX_VALUE;
	}
	
	public BoundingBox(Vector location, Rotation rotation, double width, double height, double depth) throws IllegalArgumentException {
		if (location.components() != 3) {
			throw new IllegalArgumentException("Location must have 3 components");
		} else if (width < 0) {
			throw new IllegalArgumentException("Width must be zero or positive");
		} else if (height < 0) {
			throw new IllegalArgumentException("Height must be zero or positive");
		} else if (depth < 0) {
			throw new IllegalArgumentException("Depth must be zero or positive");
		} else {
			this.location = location;
			this.rotation = rotation;
			this.width = width;
			this.height = height;
			this.depth = depth;
		}
	}
	
	public Vector toBoundingBoxSpace(Vector vector) throws IllegalArgumentException {
		if (vector.components() == 3) {
			Matrix rotationMatrix = new Matrix(rotation.negate());
			return location.negate().sum(rotationMatrix.product(vector));
		} else {
			throw new IllegalArgumentException("Vector must have 3 components");
		}
	}
	
	public boolean contains(Vector point) throws IllegalArgumentException {
		if (point.components() == 3) {
			point = toBoundingBoxSpace(point);
			double x = point.getComponent(0);
			double y = point.getComponent(1);
			double z = point.getComponent(2);
			return ((x > -width/2 && x < width/2) && (y > -height/2 && y < height/2) && (z > -depth/2 && z < depth/2));
		} else {
			throw new IllegalArgumentException("Point vector must have 3 components");
		}
	}
	
	public String toString() {
		StringBuilder info = new StringBuilder();
		info.append("BoundingBox");
		info.append("[location=");
		info.append(location);
		info.append(",rotation=");
		info.append(rotation);
		info.append(",width=");
		info.append(width);
		info.append(",height=");
		info.append(height);
		info.append(",depth=");
		info.append(depth);
		info.append("]");
		return info.toString();
	}
}
