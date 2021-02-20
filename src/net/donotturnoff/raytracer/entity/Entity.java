package net.donotturnoff.raytracer.entity;

import net.donotturnoff.raytracer.material.Material;
import net.donotturnoff.raytracer.maths.Matrix;
import net.donotturnoff.raytracer.maths.Rotation;
import net.donotturnoff.raytracer.maths.Vector;
import net.donotturnoff.raytracer.util.BoundingBox;

public class Entity {
	
	protected String name;
	protected Material material;
	protected BoundingBox boundingBox;
	protected Vector translation;
	protected Rotation rotation;
	
	public String getName() {
		return name;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}
	
	public double getIntersectionParameter(Vector origin, Vector direction) {
		return Double.MAX_VALUE;
	}
	
	public Vector getIntersection(Vector origin, Vector direction, double intersectionParameter) {
		return origin.sum(direction.product(intersectionParameter));
	}
	
	public Vector getNormal(Vector intersection) {
		return new Vector(0, 0, 0);
	}
	
	public Vector getSurfaceCoordinates(Vector intersection) {
		return new Vector(0, 0);
	}
	
	public Vector translateIntoEntitySpace(Vector vector) {
		return translation.negate().sum(vector);
	}
	
	public Vector rotateIntoEntitySpace(Vector vector) {
		Matrix rotationMatrix = new Matrix(rotation.negate());
		return rotationMatrix.product(vector);
	}
	
	public Vector translateIntoWorldSpace(Vector vector) {
		return translation.sum(vector);
	}
	
	public Vector rotateIntoWorldSpace(Vector vector) {
		Matrix rotationMatrix = new Matrix(rotation);
		return rotationMatrix.product(vector);
	}
	
	public boolean boundingBoxContains(Vector origin, Vector direction, double intersectionParameter) {
		return boundingBox.contains(getIntersection(origin, direction, intersectionParameter));
	}
}
