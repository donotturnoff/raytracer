import net.donotturnoff.raytracer.util.Scene;
import net.donotturnoff.raytracer.util.SceneMarkupLanguageReader;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class Test {
	public static void main(String[] args) {
		try {
			/*net.donotturnoff.raytracer.material.Material blue = new net.donotturnoff.raytracer.material.Material("Blue", new net.donotturnoff.raytracer.material.Checker(10, 10, 1, Color.blue, Color.cyan), new net.donotturnoff.raytracer.material.BlockColor(0), new net.donotturnoff.raytracer.material.BlockColor(254));
			net.donotturnoff.raytracer.material.Material red = new net.donotturnoff.raytracer.material.Material("Red", new net.donotturnoff.raytracer.material.BlockColor(255, 0, 0));
			net.donotturnoff.raytracer.material.Material gray = new net.donotturnoff.raytracer.material.Material("Gray", new net.donotturnoff.raytracer.material.BlockColor(new Color(255, 255, 255, 40)), new net.donotturnoff.raytracer.material.BlockColor(180), new net.donotturnoff.raytracer.material.BlockColor(255), new net.donotturnoff.raytracer.material.SurfaceMap(), 1.4);
			net.donotturnoff.raytracer.material.Material pic = new net.donotturnoff.raytracer.material.Material("Pic", new net.donotturnoff.raytracer.material.Texture("earth.png"));
			
			net.donotturnoff.raytracer.entity.Sphere sphere1 = new net.donotturnoff.raytracer.entity.Sphere("1", new net.donotturnoff.raytracer.maths.Vector(0, 0, 11), 3, gray);
			net.donotturnoff.raytracer.entity.Sphere sphere2 = new net.donotturnoff.raytracer.entity.Sphere("2", new net.donotturnoff.raytracer.maths.Vector(1, 0, 16), 1, pic);
			net.donotturnoff.raytracer.entity.Sphere sphere3 = new net.donotturnoff.raytracer.entity.Sphere("3", new net.donotturnoff.raytracer.maths.Vector(-3, 0, 10), 1, blue);
			net.donotturnoff.raytracer.entity.Sphere sphere4 = new net.donotturnoff.raytracer.entity.Sphere("4", new net.donotturnoff.raytracer.maths.Vector(0, 3, 10), 1, red);
			net.donotturnoff.raytracer.entity.Sphere sphere5 = new net.donotturnoff.raytracer.entity.Sphere("5", new net.donotturnoff.raytracer.maths.Vector(-1, 0, 16), 1, blue);
			net.donotturnoff.raytracer.entity.Sphere sphere6 = new net.donotturnoff.raytracer.entity.Sphere("6", new net.donotturnoff.raytracer.maths.Vector(0, 0, 10), 1, blue);
			
			net.donotturnoff.raytracer.entity.Entity[] entities = new net.donotturnoff.raytracer.entity.Entity[]{sphere6};
			net.donotturnoff.raytracer.light.Light[] lights = new net.donotturnoff.raytracer.light.Light[]{new net.donotturnoff.raytracer.light.Lamp(Color.white, 1, new net.donotturnoff.raytracer.maths.Vector(-5, -5, -5))};
			
			net.donotturnoff.raytracer.util.Camera camera = new net.donotturnoff.raytracer.util.Camera(new net.donotturnoff.raytracer.maths.Vector(0, 0, 0), new net.donotturnoff.raytracer.maths.Rotation(0, 0, 0));
			
			net.donotturnoff.raytracer.util.Scene scene = new net.donotturnoff.raytracer.util.Scene(2000, 2000, entities, lights, camera, 1, Color.white, 1, 1, 1, 1, 8, 16);*/
			
			SceneMarkupLanguageReader smlr = new SceneMarkupLanguageReader();
			Scene scene = smlr.parse("scene.sml");
			RayTracer tracer = new RayTracer();
			BufferedImage image = tracer.trace(scene);
			ImageIO.write(image, "png", new File("assets/test.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
