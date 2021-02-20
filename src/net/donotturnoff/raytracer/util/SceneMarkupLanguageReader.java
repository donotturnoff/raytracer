package net.donotturnoff.raytracer.util;
/* TODO
 * - Use class' own default values if no value specified (i.e. use different constructor)
 * - Make more robust
 */

import net.donotturnoff.raytracer.entity.Plane;
import net.donotturnoff.raytracer.entity.Sphere;
import net.donotturnoff.raytracer.entity.Entity;
import net.donotturnoff.raytracer.light.Lamp;
import net.donotturnoff.raytracer.light.Light;
import net.donotturnoff.raytracer.light.Spotlight;
import net.donotturnoff.raytracer.material.*;
import net.donotturnoff.raytracer.maths.Rotation;
import net.donotturnoff.raytracer.maths.Vector;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import java.io.*;
import java.awt.Color;

public class SceneMarkupLanguageReader {
	
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	
	public SceneMarkupLanguageReader() throws ParserConfigurationException {
		factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
	}
	
	public Scene parse(String path) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException {
		File smlFile = new File(path);
		Document document = builder.parse(smlFile);
		//validate(document);
		Element root = document.getDocumentElement();
		int[] dimensions = parseDimensions(root);
		Entity[] entities = parseEntities(root);
		Light[] lights = parseLights(root);
		Camera camera = parseCamera(root);
		Color ambient = parseAmbient(root);
		Color background = parseBackground(root);
		int[] viewport = parseViewport(root);
		int recursionLimit = parseRecursionLimit(root);
		int[] supersamples = parseSupersamples(root);
		return new Scene(dimensions[0], dimensions[1], entities, lights, camera, ambient, background, viewport[0], viewport[1], viewport[2], recursionLimit, supersamples[0], supersamples[1]);
	}
	
	private void validate(Document document) {
		/*Schema schema = null;
		try {
			String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
			SchemaFactory factory = SchemaFactory.newInstance(language);
			schema = factory.newSchema(new File(name));
		} catch (Exception e) {
			e.printStackStrace();
		}
		Validator validator = schema.newValidator();
		validator.validate(new DOMSource(document));*/
	}
	
	private int[] parseDimensions(Element root) {
		int width = 500;
		int height = 500;
		Element dimensionsElement = (Element) root.getElementsByTagName("dimensions").item(0);
		if (dimensionsElement != null) {
			Element widthElement = (Element) dimensionsElement.getElementsByTagName("width").item(0);
			Element heightElement = (Element) dimensionsElement.getElementsByTagName("height").item(0);
			if (widthElement != null) {
				width = Integer.parseInt(widthElement.getTextContent());
			}
			if (heightElement != null) {
				height = Integer.parseInt(heightElement.getTextContent());
			}
		}
		return new int[]{width, height};
	}
	
	private Entity[] parseEntities(Element root) throws IOException {
		Element entitiesElement = (Element) root.getElementsByTagName("entities").item(0);
		if (entitiesElement != null) {
			NodeList sphereNodeList = entitiesElement.getElementsByTagName("sphere");
			NodeList planeNodeList = entitiesElement.getElementsByTagName("plane");
			Entity[] entities = new Entity[sphereNodeList.getLength() + planeNodeList.getLength()];
			int j = 0;
			for (int i = 0; i < sphereNodeList.getLength(); i++) {
				Element sphereElement = (Element) sphereNodeList.item(i);
				entities[j] = parseSphere(sphereElement);
				j++;
			}
			for (int i = 0; i < planeNodeList.getLength(); i++) {
				Element planeElement = (Element) planeNodeList.item(i);
				entities[j] = parsePlane(planeElement);
				j++;
			}
			return entities;
		} else {
			return new Entity[]{};
		}
	}
	
	private Sphere parseSphere(Element sphereElement) throws IOException {
		Element nameElement = (Element) sphereElement.getElementsByTagName("name").item(0);
		Element locationElement = (Element) sphereElement.getElementsByTagName("location").item(0);
		Element radiusElement = (Element) sphereElement.getElementsByTagName("radius").item(0);
		Element rotationElement = (Element) sphereElement.getElementsByTagName("rotation").item(0);
		Element materialElement = (Element) sphereElement.getElementsByTagName("material").item(0);
		Element boundingBoxElement = (Element) sphereElement.getElementsByTagName("boundingBox").item(0);
		String name = "";
		Vector location = parseLocation(locationElement);
		double radius = 1;
		Rotation rotation = parseRotation(rotationElement);
		Material material = parseMaterial(materialElement);
		BoundingBox boundingBox = parseBoundingBox(boundingBoxElement);
		if (nameElement != null) {
			name = nameElement.getTextContent();
		}
		if (radiusElement != null) {
			radius = Double.parseDouble(radiusElement.getTextContent());
		}
		Sphere sphere = new Sphere(name, location, radius, rotation, material, boundingBox);
		return sphere;
	}
	
	private Plane parsePlane(Element planeElement) throws IOException {
		Element nameElement = (Element) planeElement.getElementsByTagName("name").item(0);
		Element normalElement = (Element) planeElement.getElementsByTagName("normal").item(0);
		Element pointElement = (Element) planeElement.getElementsByTagName("point").item(0);
		Element materialElement = (Element) planeElement.getElementsByTagName("material").item(0);
		Element boundingBoxElement = (Element) planeElement.getElementsByTagName("boundingBox").item(0);
		String name = "";
		Vector normal = parseVector(normalElement);
		Vector point = parseVector(pointElement);
		Material material = parseMaterial(materialElement);
		BoundingBox boundingBox = parseBoundingBox(boundingBoxElement);
		if (nameElement != null) {
			name = nameElement.getTextContent();
		}
		Plane plane = new Plane(name, normal, point, material, boundingBox);
		return plane;
	}
	
	private Material parseMaterial(Element materialElement) throws IOException {
		Element nameElement = (Element) materialElement.getElementsByTagName("name").item(0);
		Element decorationMapElement = (Element) materialElement.getElementsByTagName("decorationMap").item(0);
		Element ambientMapElement = (Element) materialElement.getElementsByTagName("ambientMap").item(0);
		Element specularMapElement = (Element) materialElement.getElementsByTagName("specularMap").item(0);
		Element shineMapElement = (Element) materialElement.getElementsByTagName("shineMap").item(0);
		Element reflectionMapElement = (Element) materialElement.getElementsByTagName("reflectionMap").item(0);
		Element bumpMapElement = (Element) materialElement.getElementsByTagName("bumpMap").item(0);
		Element refractiveIndexElement = (Element) materialElement.getElementsByTagName("refractiveIndex").item(0);
		String name = "";
		SurfaceMap decorationMap = parseMap(decorationMapElement);
		SurfaceMap ambientMap = parseMap(ambientMapElement);
		SurfaceMap specularMap = parseMap(specularMapElement);
		SurfaceMap shineMap = parseMap(shineMapElement);
		SurfaceMap reflectionMap = parseMap(reflectionMapElement);
		SurfaceMap bumpMap = parseMap(bumpMapElement);
		double refractiveIndex = 1;
		if (nameElement != null) {
			name = nameElement.getTextContent();
		}
		if (refractiveIndexElement != null) {
			refractiveIndex = Double.parseDouble(refractiveIndexElement.getTextContent());
		}
		if (ambientMapElement == null) {
			ambientMap = decorationMap;
		}
		return new Material(name, decorationMap, ambientMap, specularMap, shineMap, reflectionMap, bumpMap, refractiveIndex);
	}
	
	private SurfaceMap parseMap(Element mapElement) throws IOException {
		if (mapElement != null) {
			NodeList nodes = mapElement.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node instanceof Element) {
					Element mapDataElement = (Element) node;
					String mapType = mapDataElement.getTagName();
					if (mapType.equals("constantValue")) {
						return parseConstantValue(mapDataElement);
					} else if (mapType.equals("blockColor")) {
						return parseBlockColor(mapDataElement);
					} else if (mapType.equals("checker")) {
						return parseChecker(mapDataElement);
					} else if (mapType.equals("texture")) {
						return parseTexture(mapDataElement);
					}
				}
			}
			return new SurfaceMap();
		} else {
			return new SurfaceMap();
		}
	}
	
	private ConstantValue parseConstantValue(Element constantValueElement) {
		int value = 1;
		int width = 1;
		int height = 1;
		Element valueElement = (Element) constantValueElement.getElementsByTagName("value").item(0);
		Element widthElement = (Element) constantValueElement.getElementsByTagName("width").item(0);
		Element heightElement = (Element) constantValueElement.getElementsByTagName("height").item(0);
		if (valueElement != null) {
			value = Integer.parseInt(valueElement.getTextContent());
		}
		if (widthElement != null) {
			width = Integer.parseInt(widthElement.getTextContent());
		}
		if (heightElement != null) {
			height = Integer.parseInt(heightElement.getTextContent());
		}
		return new ConstantValue(width, height, value);
	}
	
	private BlockColor parseBlockColor(Element blockColorElement) {
		Color color = parseColor(blockColorElement);
		int width = 1;
		int height = 1;
		Element widthElement = (Element) blockColorElement.getElementsByTagName("width").item(0);
		Element heightElement = (Element) blockColorElement.getElementsByTagName("height").item(0);
		if (widthElement != null) {
			width = Integer.parseInt(widthElement.getTextContent());
		}
		if (heightElement != null) {
			height = Integer.parseInt(heightElement.getTextContent());
		}
		return new BlockColor(width, height, color);
	}
	
	private Checker parseChecker(Element checkerElement) {
		Element color1Element = (Element) checkerElement.getElementsByTagName("color1").item(0);
		Element color2Element = (Element) checkerElement.getElementsByTagName("color2").item(0);
		Element checkerSizeElement = (Element) checkerElement.getElementsByTagName("checker").item(0);
		Element widthElement = (Element) checkerElement.getElementsByTagName("width").item(0);
		Element heightElement = (Element) checkerElement.getElementsByTagName("height").item(0);
		Color color1 = parseColor(color1Element);
		Color color2 = parseColor(color2Element);
		int checker = 1;
		int width = 2;
		int height = 2;
		if (checkerSizeElement != null) {
			checker = Integer.parseInt(checkerSizeElement.getTextContent());
		}
		if (widthElement != null) {
			width = Integer.parseInt(widthElement.getTextContent());
		}
		if (heightElement != null) {
			height = Integer.parseInt(heightElement.getTextContent());
		}
		return new Checker(width, height, checker, color1, color2);
	}
	
	private Texture parseTexture(Element textureElement) throws IOException {
		Element pathElement = (Element) textureElement.getElementsByTagName("path").item(0);
		String path = "";
		if (pathElement != null) {
			path = pathElement.getTextContent();
		}
		return new Texture(path);
	}
	
	private BoundingBox parseBoundingBox(Element boundingBoxElement) {
		if (boundingBoxElement != null) {
			Element locationElement = (Element) boundingBoxElement.getElementsByTagName("location").item(0);
			Element rotationElement = (Element) boundingBoxElement.getElementsByTagName("rotation").item(0);
			Element widthElement = (Element) boundingBoxElement.getElementsByTagName("width").item(0);
			Element heightElement = (Element) boundingBoxElement.getElementsByTagName("height").item(0);
			Element depthElement = (Element) boundingBoxElement.getElementsByTagName("depth").item(0);
			Vector location = parseLocation(locationElement);
			Rotation rotation = parseRotation(rotationElement);
			double width = Double.MAX_VALUE;
			double height = Double.MAX_VALUE;
			double depth = Double.MAX_VALUE;
			if (widthElement != null) {
				width = Double.parseDouble(widthElement.getTextContent());
			}
			if (heightElement != null) {
				height = Double.parseDouble(heightElement.getTextContent());
			}
			if (depthElement != null) {
				depth = Double.parseDouble(depthElement.getTextContent());
			}
			return new BoundingBox(location, rotation, width, height, depth);
		} else {
			return new BoundingBox();
		}
	}
	
	private Light[] parseLights(Element root) {
		Element lightsElement = (Element) root.getElementsByTagName("lights").item(0);
		if (lightsElement != null) {
			NodeList lampNodeList = lightsElement.getElementsByTagName("lamp");
			NodeList spotlightNodeList = lightsElement.getElementsByTagName("spotlight");

			Light[] lights = new Light[lampNodeList.getLength() + spotlightNodeList.getLength()];
			int j = 0;
			for (int i = 0; i < lampNodeList.getLength(); i++) {
				Element lampElement = (Element) lampNodeList.item(i);
				lights[j] = parseLamp(lampElement);
				j++;
			}
			for (int i = 0; i < spotlightNodeList.getLength(); i++) {
				Element spotlightElement = (Element) spotlightNodeList.item(i);
				lights[j] = parseSpotlight(spotlightElement);
				j++;
			}
			return lights;
		} else {
			return new Light[]{};
		}
	}
	
	private Lamp parseLamp(Element lampElement) {
		Element nameElement = (Element) lampElement.getElementsByTagName("name").item(0);
		Element colorElement = (Element) lampElement.getElementsByTagName("color").item(0);
		Element locationElement = (Element) lampElement.getElementsByTagName("location").item(0);
		Element radiusElement = (Element) lampElement.getElementsByTagName("radius").item(0);
		String name = "";
		Color color = parseColor(colorElement);
		Vector location = parseLocation(locationElement);
		double radius = 0;
		if (nameElement != null) {
			name = nameElement.getTextContent();
		}
		if (radiusElement != null) {
			radius = Double.parseDouble(radiusElement.getTextContent());
		}
		return new Lamp(name, color, location, radius);
	}
	
	private Spotlight parseSpotlight(Element spotlightElement) {
		Element nameElement = (Element) spotlightElement.getElementsByTagName("name").item(0);
		Element colorElement = (Element) spotlightElement.getElementsByTagName("color").item(0);
		Element directionElement = (Element) spotlightElement.getElementsByTagName("direction").item(0);
		String name = "";
		Color color = parseColor(colorElement);
		double intensity = 1;
		Vector direction = parseVector(directionElement);
		if (nameElement != null) {
			name = nameElement.getTextContent();
		}
		return new Spotlight(name, color, direction);
		
	}
	
	private Camera parseCamera(Element root) {
		Element cameraElement = (Element) root.getElementsByTagName("camera").item(0);
		if (cameraElement != null) {
			Element locationElement = (Element) cameraElement.getElementsByTagName("location").item(0);
			Element rotationElement = (Element) cameraElement.getElementsByTagName("rotation").item(0);
			Vector location = parseLocation(locationElement);
			Rotation rotation = parseRotation(rotationElement);
			return new Camera(location, rotation);
		} else {
			return new Camera(new Vector(0, 0, 0), new Rotation(0, 0, 0));
		}
	}
	
	private Vector parseLocation(Element locationElement) {
		double x = 0;
		double y = 0;
		double z = 0;
		if (locationElement != null) {
			Element xElement = (Element) locationElement.getElementsByTagName("x").item(0);
			Element yElement = (Element) locationElement.getElementsByTagName("y").item(0);
			Element zElement = (Element) locationElement.getElementsByTagName("z").item(0);
			if (xElement != null) {
				x = Double.parseDouble(xElement.getTextContent());
			}
			if (yElement != null) {
				y = Double.parseDouble(yElement.getTextContent());
			}
			if (zElement != null) {
				z = Double.parseDouble(zElement.getTextContent());
			}
		}
		return new Vector(x, y, z);
	}
	
	private Vector parseVector(Element vectorElement) {
		if (vectorElement != null) {
			NodeList componentNodeList = vectorElement.getElementsByTagName("component");
			double[] array = new double[componentNodeList.getLength()];
			for (int i = 0; i < componentNodeList.getLength(); i++) {
				Element componentElement = (Element) componentNodeList.item(i);
				array[i] = Integer.parseInt(componentElement.getTextContent());
			}
			return new Vector(array);
		} else {
			return new Vector();
		}
	}
	
	private Rotation parseRotation(Element rotationElement) {
		double x = 0;
		double y = 0;
		double z = 0;
		if (rotationElement != null) {
			Element xElement = (Element) rotationElement.getElementsByTagName("x").item(0);
			Element yElement = (Element) rotationElement.getElementsByTagName("y").item(0);
			Element zElement = (Element) rotationElement.getElementsByTagName("z").item(0);
			if (xElement != null) {
				x = Double.parseDouble(xElement.getTextContent());
			}
			if (yElement != null) {
				y = Double.parseDouble(yElement.getTextContent());
			}
			if (zElement != null) {
				z = Double.parseDouble(zElement.getTextContent());
			}
		}
		return new Rotation(x, y, z);
	}
	
	private Color parseAmbient(Element root) {
		Element ambientElement = (Element) root.getElementsByTagName("ambient").item(0);
		return parseColor(ambientElement);
	}
	
	private Color parseBackground(Element root) {
		Element backgroundElement = (Element) root.getElementsByTagName("background").item(0);
		return parseColor(backgroundElement);
	}
	
	private Color parseColor(Element colorElement) {
		int red = 0;
		int green = 0;
		int blue = 0;
		int alpha = 255;
		if (colorElement != null) {
			Element redElement = (Element) colorElement.getElementsByTagName("red").item(0);
			Element greenElement = (Element) colorElement.getElementsByTagName("green").item(0);
			Element blueElement = (Element) colorElement.getElementsByTagName("blue").item(0);
			Element alphaElement = (Element) colorElement.getElementsByTagName("alpha").item(0);
			if (redElement != null) {
				red = Integer.parseInt(redElement.getTextContent());
			}
			if (greenElement != null) {
				green = Integer.parseInt(greenElement.getTextContent());
			}
			if (blueElement != null) {
				blue = Integer.parseInt(blueElement.getTextContent());
			}
			if (alphaElement != null) {
				alpha = Integer.parseInt(alphaElement.getTextContent());
			}
		}
		return new Color(red, green, blue, alpha);
	}
	
	private int[] parseViewport(Element root) {
		int width = 1;
		int height = 1;
		int depth = 1;
		Element viewportElement = (Element) root.getElementsByTagName("viewport").item(0);
		if (viewportElement != null) {
			Element widthElement = (Element) viewportElement.getElementsByTagName("width").item(0);
			Element heightElement = (Element) viewportElement.getElementsByTagName("height").item(0);
			Element depthElement = (Element) viewportElement.getElementsByTagName("depth").item(0);
			if (widthElement != null) {
				width = Integer.parseInt(widthElement.getTextContent());
			}
			if (heightElement != null) {
				height = Integer.parseInt(heightElement.getTextContent());
			}
			if (depthElement != null) {
				depth = Integer.parseInt(depthElement.getTextContent());
			}
		}
		return new int[]{width, height, depth};
	}
	
	private int parseRecursionLimit(Element root) {
		int recursionLimit = 4;
		Element recursionLimitElement = (Element) root.getElementsByTagName("recursionLimit").item(0);
		if (recursionLimitElement != null) {
			recursionLimit = Integer.parseInt(recursionLimitElement.getTextContent());
		}
		return recursionLimit;
	}
	
	private int[] parseSupersamples(Element root) {
		int supersampleGrid = 4;
		int supersampleStochastic = 4;
		Element supersamplesElement = (Element) root.getElementsByTagName("supersamples").item(0);
		if (supersamplesElement != null) {
			Element gridElement = (Element) supersamplesElement.getElementsByTagName("gridWidth").item(0);
			Element stochasticElement = (Element) supersamplesElement.getElementsByTagName("stochasticPoints").item(0);
			if (gridElement != null) {
				supersampleGrid = Integer.parseInt(gridElement.getTextContent());
			}
			if (stochasticElement != null) {
				supersampleStochastic = Integer.parseInt(stochasticElement.getTextContent());
			}
		}
		return new int[]{supersampleGrid, supersampleStochastic};
	}
}
