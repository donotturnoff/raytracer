package net.donotturnoff.raytracer.material;

import net.donotturnoff.raytracer.maths.Vector;

import java.awt.Color;

public class Material {
	
	private String name;
	private SurfaceMap decorationMap, ambientMap, specularMap, shineMap, reflectionMap, bumpMap;
	private double refractiveIndex;
	
	public Material(SurfaceMap decorationMap, SurfaceMap ambientMap, SurfaceMap specularMap, SurfaceMap shineMap, SurfaceMap reflectionMap) {
		this("", decorationMap, ambientMap, specularMap, shineMap, reflectionMap, new SurfaceMap(), 1);
	}
	
	public Material(String name, SurfaceMap decorationMap, SurfaceMap ambientMap, SurfaceMap specularMap, SurfaceMap shineMap, SurfaceMap reflectionMap) {
		this(name, decorationMap, ambientMap, specularMap, shineMap, reflectionMap, new SurfaceMap(), 1);
	}
	
	public Material(SurfaceMap decorationMap, SurfaceMap ambientMap, SurfaceMap specularMap, SurfaceMap shineMap, SurfaceMap reflectionMap, SurfaceMap bumpMap, double refractiveIndex) {
		this("", decorationMap, ambientMap, specularMap, shineMap, reflectionMap, bumpMap, refractiveIndex);
	}
	
	public Material(String name, SurfaceMap decorationMap, SurfaceMap ambientMap, SurfaceMap specularMap, SurfaceMap shineMap, SurfaceMap reflectionMap, SurfaceMap bumpMap, double refractiveIndex) {
		this.name = name;
		this.decorationMap = decorationMap;
		this.ambientMap = ambientMap;
		this.specularMap = specularMap;
		this.shineMap = shineMap;
		this.reflectionMap = reflectionMap;
		this.bumpMap = bumpMap;
		this.refractiveIndex = refractiveIndex;
	}
	
	public String getName() {
		return name;
	}
	
	public SurfaceMap getDecorationMap() {
		return decorationMap;
	}
	
	public SurfaceMap getAmbientMap() {
		return ambientMap;
	}
	
	public SurfaceMap getSpecularMap() {
		return specularMap;
	}
	
	public SurfaceMap getShineMap() {
		return shineMap;
	}
	
	public SurfaceMap getReflectionMap() {
		return reflectionMap;
	}
	
	public SurfaceMap getBumpMap() {
		return bumpMap;
	}
	
	public double getRefractiveIndex() {
		return refractiveIndex;
	}
	
	public Vector getDecoration(Vector position) {
		return new Vector(new Color(decorationMap.getDatum(position), true), true); //"true" arguments preserve alpha
	}
	
	public double getTransparency(Vector position) {
		return getDecoration(position).getComponent(3);
	}
	
	public Vector getAmbient(Vector position) {
		return new Vector(new Color(ambientMap.getDatum(position)));
	}
	
	public Vector getDiffuse(Vector position) {
		return new Vector(new Color(decorationMap.getDatum(position))); //Color of decoration map at given position but without transparency
	}
	
	public Vector getSpecular(Vector position) {
		return new Vector(new Color(specularMap.getDatum(position)));
	}
	
	public int getShine(Vector position) {
		return shineMap.getDatum(position);
	}
	
	public double getReflection(Vector position) {
		int reflectivity = reflectionMap.getDatum(position);
		if (reflectivity > 255) {
			return 1;
		} else if (reflectivity < 0) {
			return 0;
		} else {
			return reflectivity / 255.0;
		}
	}
	
	public Vector getBump(Vector position) {
		return new Vector(new Color(bumpMap.getDatum(position)));
	}
	
	public double getRefraction(Vector position) {
		return refractiveIndex;
	}
	
	public String toString() {
		StringBuilder info = new StringBuilder();
		info.append("Material");
		info.append("[name=\"");
		info.append(name);
		info.append("\", decoration=");
		info.append(decorationMap);
		info.append(", ambient=");
		info.append(ambientMap);
		info.append(", specular=");
		info.append(specularMap);
		info.append(", shine=");
		info.append(shineMap);
		info.append(", reflection=");
		info.append(reflectionMap);
		info.append(", bump=");
		info.append(bumpMap);
		info.append(", refraction=");
		info.append(refractiveIndex);
		info.append("]");
		return info.toString();
	}
}
