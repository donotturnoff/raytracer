package net.donotturnoff.raytracer.util;

import net.donotturnoff.raytracer.maths.Matrix;
import net.donotturnoff.raytracer.maths.Rotation;
import net.donotturnoff.raytracer.maths.Vector;

public class Camera {
	
	private Vector location;
	private Rotation rotation;
	
	public Camera() {
		this(new Vector(0, 0, 0), new Rotation());
	}
	
	public Camera(Vector location, Rotation rotation) {
		this.location = location;
		this.rotation = rotation;
	}
	
	public Vector getLocation() {
		return location;
	}
	
	public Rotation getRotation() {
		return rotation;
	}
	
	public Vector translateIntoCameraSpace(Vector vector) {
		return location.negate().sum(vector);
	}
	
	public Vector rotateIntoCameraSpace(Vector vector) {
		Matrix rotationMatrix = new Matrix(rotation.negate());
		return rotationMatrix.product(vector);
	}
	
	public Vector translateIntoWorldSpace(Vector vector) {
		return location.sum(vector);
	}
	
	public Vector rotateIntoWorldSpace(Vector vector) {
		Matrix rotationMatrix = new Matrix(rotation);
		return rotationMatrix.product(vector);
	}
}
