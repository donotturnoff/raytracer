package net.donotturnoff.raytracer.util;

import net.donotturnoff.raytracer.entity.Entity;
import net.donotturnoff.raytracer.light.Lamp;
import net.donotturnoff.raytracer.light.Light;
import net.donotturnoff.raytracer.light.Spotlight;
import net.donotturnoff.raytracer.material.Material;
import net.donotturnoff.raytracer.maths.Vector;

import java.awt.Color;

public class Ray {
	
	public static final double EPSILON = 0.1;
	
	private Scene scene;
	private Vector origin, direction;
	private double distance;
	private int depth;
	private Entity inside;
	
	public Ray(Scene scene, Vector origin, Vector direction, int depth, double distance, Entity inside) throws IllegalArgumentException {
		if (origin.components() != 3) {
			throw new IllegalArgumentException("Origin vector must have 3 components");
		} else if (direction.components() != 3) {
			throw new IllegalArgumentException("Direction vector must have 3 components");
		} else if (depth < 0 || depth > scene.getRecursionLimit()) {
			throw new IllegalArgumentException("Recursion depth must be between 0 and the recursion limit (" + scene.getRecursionLimit() + ") inclusive");
		} else if (distance < 0) {
			throw new IllegalArgumentException("Distance travelled must be 0 or positive");
		} else {
			this.scene = scene;
			this.origin = origin;
			this.direction = direction;
			this.depth = depth;
			this.distance = distance;
			this.inside = inside;
		}
	}
	
	public Vector getOrigin() {
		return origin;
	}
	
	public Vector getDirection() {
		return direction;
	}
	
	public Color getColor() {
		Color color = scene.getBackgroundColor();
		Entity entity = getNearestEntity();
		if (entity != null && depth < scene.getRecursionLimit()) {
			if (entity == inside) {
				/* If the intersection is with the entity which the ray is currently inside, the ray is leaving that net.donotturnoff.raytracer.entity. */
				inside = null;
			} else {
				inside = entity;
			}
			Vector entitySpaceOrigin = entity.rotateIntoEntitySpace(entity.translateIntoEntitySpace(origin));
			Vector entitySpaceDirection = entity.rotateIntoEntitySpace(direction);
			
			double intersectionParameter = entity.getIntersectionParameter(entitySpaceOrigin, entitySpaceDirection);
			Vector entitySpaceIntersection = entity.getIntersection(entitySpaceOrigin, entitySpaceDirection, intersectionParameter);
			Vector entitySpaceNormal = entity.getNormal(entitySpaceIntersection);
			
			Vector intersection = entity.translateIntoWorldSpace(entity.rotateIntoWorldSpace(entitySpaceIntersection));
			Vector normal = entity.rotateIntoWorldSpace(entitySpaceNormal);
			
			Vector transparencyMapSize = entity.getMaterial().getDecorationMap().getSize().difference(new Vector(1, 1));
			Vector transparencyMapCoordinates = entity.getSurfaceCoordinates(entitySpaceIntersection).hadamard(transparencyMapSize);
			double entityTransparency = 1 - (entity.getMaterial().getTransparency(transparencyMapCoordinates) / 255);
			
			Vector entityColorVector = addLighting(entity, intersection, entitySpaceIntersection, normal);
			entityColorVector = addReflection(entityColorVector, entity, intersectionParameter, intersection, normal);
			entityColorVector = addRefraction(entityColorVector, entity, intersectionParameter, intersection, normal, entityTransparency);
			color = entityColorVector.toColor();
		}
		return color;
	}
	
	public Entity getNearestEntity() {
		Entity nearestEntity = null;
		double nearestIntersection = Double.MAX_VALUE;
		for (Entity entity: scene.getEntities()) {
			Vector entitySpaceOrigin = entity.rotateIntoEntitySpace(entity.translateIntoEntitySpace(origin));
			Vector entitySpaceDirection = entity.rotateIntoEntitySpace(direction);
			double thisIntersection = entity.getIntersectionParameter(entitySpaceOrigin, entitySpaceDirection);
			if (thisIntersection < nearestIntersection) {
				nearestIntersection = thisIntersection;
				nearestEntity = entity;
			}
		}
		return nearestEntity;
	}
	
	private Vector getReflectedDirection(Vector direction, Vector intersection, Vector normal) {
		double c1 = -direction.dot(normal);
		Vector reflectedDirection = direction.sum(normal.product(2 * c1));
		return reflectedDirection;
	}
	
	private Vector getRefractedDirection(double intersectionParameter, Vector intersection, Vector normal, Entity entity) {
		//Not implemented yet
		return new Vector();
	}
	
	private Ray getReflectedRay(double intersectionParameter, Vector intersection, Vector normal) {
		Vector reflectedDirection = getReflectedDirection(direction, intersection, normal);
		Vector reflectedIntersection = intersection.sum(reflectedDirection.product(EPSILON));
		return new Ray(scene, reflectedIntersection, reflectedDirection, depth+1, distance+intersectionParameter, inside);
	}
	
	private Ray getRefractedRay(double intersectionParameter, Vector intersection, Vector normal, Entity entity) {
		
		double n1 = 1;
		double n2 = entity.getMaterial().getRefractiveIndex();
		double n = n1/n2;
		double c1 = direction.normalize().dot(normal.normalize());
		if (c1 > 0) {
			n = 1/n;
			normal = normal.negate();
		} else {
			c1 = -c1;
		}
		double k = 1 - n*n * (1 - c1*c1);
		if (k < 0) {
			return null;
		} else {
			double c2 = Math.sqrt(k);
			Vector refractedDirection = direction.product(n).sum(normal.product(n * c1 - c2));
			Vector refractedIntersection = intersection.sum(refractedDirection.product(EPSILON));
			return new Ray(scene, refractedIntersection, refractedDirection, depth+1, distance+intersectionParameter, entity);
		}
	}
		
	private Ray[] getShadowRays(Vector intersection) {
		Light[] lights = scene.getLights();
		Ray[] shadowRays = new Ray[lights.length];
		for (int i = 0; i < lights.length; i++) {
			Light light = lights[i];
			Vector shadowRayOrigin, shadowRayDirection;
			if (light instanceof Lamp) {
				shadowRayDirection = ((Lamp) light).getLocation().difference(intersection);
			} else {
				shadowRayDirection = ((Spotlight) light).getDirection().negate();
			}
			shadowRayOrigin = intersection.sum(shadowRayDirection.product(EPSILON));
			shadowRays[i] = new Ray(scene, shadowRayOrigin, shadowRayDirection, depth+1, distance, inside);
		}
		return shadowRays;
	}
	
	private Vector addReflection(Vector color, Entity entity, double intersectionParameter, Vector intersection, Vector normal) {
		Ray reflectedRay = getReflectedRay(intersectionParameter, intersection, normal);
		if (reflectedRay.getNearestEntity() == null) {
			return color;
		} else {
			Vector reflectionSize = entity.getMaterial().getReflectionMap().getSize();
			Vector reflectionCoordinates = entity.getSurfaceCoordinates(intersection).hadamard(reflectionSize);
			double reflectivity = entity.getMaterial().getReflection(reflectionCoordinates);
			Vector reflectedColor = new Vector(reflectedRay.getColor());
			return color.product(1-reflectivity).sum(reflectedColor.product(reflectivity));
		}
	}
	
	private Vector addRefraction(Vector color, Entity entity, double intersectionParameter, Vector intersection, Vector normal, double transparency) {
		if (transparency == 0) {
			return color;
		} else {
			Ray refractedRay = getRefractedRay(intersectionParameter, intersection, normal, entity);
			if (refractedRay == null) {
				return color;
			}
			Vector refractedColor = new Vector(refractedRay.getColor());
			return color.product(1-transparency).sum(refractedColor.product(transparency));
		}
	}
	
	private Vector addLighting(Entity entity, Vector intersection, Vector entitySpaceIntersection, Vector normal) {
		Material material = entity.getMaterial();
		
		Vector surfaceCoordinates = entity.getSurfaceCoordinates(entitySpaceIntersection);
		Vector diffuseMapSize = entity.getMaterial().getDecorationMap().getSize().difference(new Vector(1, 1));
		Vector diffuseMapCoordinates = entity.getSurfaceCoordinates(entitySpaceIntersection).hadamard(diffuseMapSize);
		Vector ambientMapSize = entity.getMaterial().getAmbientMap().getSize().difference(new Vector(1, 1));
		Vector ambientMapCoordinates = entity.getSurfaceCoordinates(entitySpaceIntersection).hadamard(ambientMapSize);
		Vector specularMapSize = entity.getMaterial().getSpecularMap().getSize().difference(new Vector(1, 1));
		Vector specularMapCoordinates = entity.getSurfaceCoordinates(entitySpaceIntersection).hadamard(specularMapSize);
		Vector shineMapSize = entity.getMaterial().getShineMap().getSize().difference(new Vector(1, 1));
		Vector shineMapCoordinates = entity.getSurfaceCoordinates(entitySpaceIntersection).hadamard(shineMapSize);
		
		Vector phongAmbientColor = material.getAmbient(ambientMapCoordinates).hadamard(new Vector(scene.getAmbient()));
		Vector phongColor = phongAmbientColor;
		Vector viewVector = scene.getCamera().getLocation().difference(intersection);
		int shine = material.getShine(shineMapCoordinates);
		for (Light light: scene.getLights()) {
			Ray shadowRay;
			Vector shadowRayOrigin, shadowRayDirection;
			if (light instanceof Lamp) {
				shadowRayDirection = ((Lamp) light).getLocation().difference(intersection);
			} else {
				shadowRayDirection = ((Spotlight) light).getDirection().negate();
			}
			shadowRayOrigin = intersection.sum(shadowRayDirection.product(EPSILON));
			shadowRay = new Ray(scene, shadowRayOrigin, shadowRayDirection, depth+1, distance, inside);
			
			double shadowRayIntersectionParameter = Double.MAX_VALUE;
			Entity shadowRayEntity = shadowRay.getNearestEntity();
			if (shadowRayEntity != null) {
				Vector shadowRayEntitySpaceOrigin = shadowRayEntity.rotateIntoEntitySpace(shadowRayEntity.translateIntoEntitySpace(shadowRayOrigin));
				Vector shadowRayEntitySpaceDirection = shadowRayEntity.rotateIntoEntitySpace(shadowRayDirection);
				shadowRayIntersectionParameter = shadowRayEntity.getIntersectionParameter(shadowRayEntitySpaceOrigin, shadowRayEntitySpaceDirection);
			}
			
			if (shadowRayEntity == null || shadowRayIntersectionParameter > 1.0 || shadowRayIntersectionParameter < 0.0) {
				
				// Add check for transparent object
				
				Vector lightVector = shadowRayDirection.negate();
				double diffuseDot = normal.normalize().dot(shadowRayDirection.normalize());
				double specularDot = viewVector.normalize().dot(getReflectedDirection(lightVector, intersection, normal).normalize());
				Vector lightColor = new Vector(light.getColor());
				Vector phongDiffuseColor = material.getDiffuse(diffuseMapCoordinates).product(Math.max(0, diffuseDot));
				Vector phongSpecularColor = material.getSpecular(specularMapCoordinates).product(Math.pow(Math.max(0, specularDot), shine));
				phongColor = phongColor.sum(phongDiffuseColor.sum(phongSpecularColor).hadamard(lightColor));
			}
		}
		
		phongColor = phongColor.product(1.0/255);
		for (int i = 0; i < 3; i++) {
			if (phongColor.getComponent(i) > 255) {
				phongColor.setComponent(i, 255);
			}
		}
		return phongColor;
	}
	
	public String toString() {
		StringBuilder info = new StringBuilder();
		info.append("Ray");
		info.append("[origin=");
		info.append(origin);
		info.append(",direction=");
		info.append(direction);
		info.append(",distance=");
		info.append(distance);
		info.append(",depth=");
		info.append(depth);
		info.append("]");
		return info.toString();
	}
}
