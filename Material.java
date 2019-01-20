public class Material {
	
	private String name;
	private SurfaceMap decorationMap, reflectionMap, specularMap, bumpMap, refractionMap;
	
	public Material(SurfaceMap decorationMap, SurfaceMap reflectionMap, SurfaceMap specularMap, SurfaceMap bumpMap, SurfaceMap refractionMap) {
		this("", decorationMap, reflectionMap, specularMap, bumpMap, refractionMap);
	}
	
	public Material(String name, SurfaceMap decorationMap, SurfaceMap reflectionMap, SurfaceMap specularMap, SurfaceMap bumpMap, SurfaceMap refractionMap) {
		this.name = name;
		this.decorationMap = decorationMap;
		this.reflectionMap = reflectionMap;
		this.specularMap = specularMap;
		this.bumpMap = bumpMap;
		this.refractionMap = refractionMap;
	}
	
	public String getName() {
		return name;
	}
	
	public SurfaceMap getDecorationMap() {
		return decorationMap;
	}
	
	public SurfaceMap getReflectionMap() {
		return reflectionMap;
	}
	
	public SurfaceMap getSpecularMap() {
		return specularMap;
	}
	
	public SurfaceMap getBumpMap() {
		return bumpMap;
	}
	
	public SurfaceMap getRefractionMap() {
		return refractionMap;
	}
	
	public int getDecoration(int x, int y) {
		return decorationMap.getDatum(x, y);
	}
	
	public int getReflection(int x, int y) {
		return reflectionMap.getDatum(x, y);
	}
	
	public int getSpecular(int x, int y) {
		return specularMap.getDatum(x, y);
	}
	
	public int getBump(int x, int y) {
		return bumpMap.getDatum(x, y);
	}
	
	public int getRefraction(int x, int y) {
		return refractionMap.getDatum(x, y);
	}
	
	public String toString() {
		StringBuilder infoBuilder = new StringBuilder();
		infoBuilder.append("Material");
		infoBuilder.append("[name=\"");
		infoBuilder.append(name);
		infoBuilder.append("\",decoration=");
		infoBuilder.append(decorationMap);
		infoBuilder.append(",reflection=");
		infoBuilder.append(reflectionMap);
		infoBuilder.append(",specular=");
		infoBuilder.append(specularMap);
		infoBuilder.append(",bump=");
		infoBuilder.append(bumpMap);
		infoBuilder.append(",refraction=");
		infoBuilder.append(refractionMap);
		infoBuilder.append("]");
		return infoBuilder.toString();
	}
}
