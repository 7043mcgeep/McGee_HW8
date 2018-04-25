import java.io.IOException;

import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial; 
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

/**
 * 
 * @author Patrick J. McGee
 * 
 * A 3D rendition of my first 2D assignment.
 * Pretty sunset included.
 * + Functional, rotating sun.
 * + Working headlights (Press 'H').
 *
 */
public class CoolCar extends Application {

	private PerspectiveCamera camera;
	private Group cameraDolly;
	private final double cameraQuantity = 10.0;
	private final double sceneWidth = 900;
	private final double sceneHeight = 900;

	private double mousePosX;
	private double mousePosY;
	private double mouseOldX;
	private double mouseOldY;
	private double mouseDeltaX;
	private double mouseDeltaY;
	
	Image sunset = new Image("file:sunset.jpg");
	Image mtn1 = new Image("file:red.jpg");
	Image mtn2 = new Image("file:mtn_sand_light.jpg");
	Image mtn3 = new Image("file:mtn_sand_dark.jpg");
	Image mtn4 = new Image("file:rock.jpg");
	Image ground = new Image("file:dirt.jpg");
	Image sun = new Image("file:sun.png");
	
	private void constructWorld(Group root) {
		//AmbientLight light = new AmbientLight(Color.WHITE);
		AmbientLight light = new AmbientLight(Color.rgb(153, 153, 153));
		root.getChildren().add(light);

		PointLight pl = new PointLight();
		pl.setTranslateX(100);
		pl.setTranslateY(-100);
		pl.setTranslateZ(-100);
		root.getChildren().add(pl);

		final PhongMaterial greenMaterial = new PhongMaterial();
		greenMaterial.setDiffuseColor(Color.FORESTGREEN);
		greenMaterial.setSpecularColor(Color.LIMEGREEN);
		final PhongMaterial yelMaterial = new PhongMaterial();
		yelMaterial.setDiffuseColor(Color.YELLOW);
		yelMaterial.setSpecularColor(Color.ORANGE);
		final PhongMaterial redMaterial = new PhongMaterial();
		redMaterial.setDiffuseColor(Color.RED);
		redMaterial.setSpecularColor(Color.TOMATO);
		final PhongMaterial blueMaterial = new PhongMaterial();
		blueMaterial.setDiffuseColor(Color.BLUE);
		blueMaterial.setSpecularColor(Color.WHITE);
		final PhongMaterial lbrownMat = new PhongMaterial();
		lbrownMat.setDiffuseColor(Color.rgb(153, 94, 39));
		lbrownMat.setSpecularColor(Color.rgb(219, 142, 70));
		final PhongMaterial brownMat = new PhongMaterial();
		brownMat.setDiffuseColor(Color.rgb(76, 36, 0));
		brownMat.setSpecularColor(Color.rgb(132, 62, 0));
		
		Box xAxis = new Box(1000, 400, 2000);
		final PhongMaterial gnd = new PhongMaterial();
		gnd.setDiffuseMap(ground);
		gnd.setSpecularColor(Color.WHITE);
		xAxis.setMaterial(gnd);
		xAxis.setTranslateY(190);

		final Sphere sphere = new Sphere(30);
		final PhongMaterial sun_mat = new PhongMaterial();
		sun_mat.setDiffuseMap(sun);
		sun_mat.setSpecularColor(Color.WHITE);
		sphere.setMaterial(sun_mat);
		sphere.setTranslateX(0);
		sphere.setTranslateY(-210);
		sphere.setTranslateZ(300);

		Box car = new Box(20, 5, 20);
		car.setMaterial(redMaterial);

		car.setTranslateX(0);
		car.setTranslateY(-10);
		car.setTranslateZ(-600);
		
		// Example from JavaFX for Dummies
		TriangleMesh pyramidMesh = new TriangleMesh();
		// define (a trivial) texture map
		pyramidMesh.getTexCoords().addAll(
				0.5f, 0,
				0, 0.5f,
				1, 0.5f,
				0, 1,
				1, 1
				);
		// define vertices
		float h = 200;                    // Height
		float s = 400;                    // Base hypotenuse
		pyramidMesh.getPoints().addAll(
		        0,    0,    0,            // Point 0 - Top
		        0,    h,    -s/2,         // Point 1 - Front
		        -s/2, h,    0,            // Point 2 - Left
		        s/2,  h,    0,            // Point 3 - Right
		        0,    h,    s/2           // Point 4 - Back
		    );
		// define faces
		pyramidMesh.getFaces().addAll(
		        0,0,  2,1,  1,2,          // Front left face
		        0,0,  1,1,  3,1,          // Front right face
		        0,0,  3,1,  4,2,          // Back right face
		        0,0,  4,1,  2,2,          // Back left face
		        4,1,  1,4,  2,2,          // Bottom left face
		        4,1,  3,3,  1,4           // Bottom right face
		    ); 
		pyramidMesh.getFaceSmoothingGroups().addAll(
				1, 2, 3, 4, 5, 5);
		MeshView pyramid = new MeshView(pyramidMesh);
		//pyramid.setDrawMode(DrawMode.LINE);
		final PhongMaterial pyrMaterial = new PhongMaterial();
		pyrMaterial.setDiffuseMap(mtn1);
		pyrMaterial.setSpecularColor(Color.WHITE);
		pyramid.setMaterial(pyrMaterial);
		pyramid.setTranslateX(-50);
		pyramid.setTranslateY(-210);
		pyramid.setTranslateZ(200);
		root.getChildren().add(pyramid);
		
		// Another large mountain of a different texture
		MeshView pyr2 = new MeshView(pyramidMesh);
		//pyramid.setDrawMode(DrawMode.LINE);
		final PhongMaterial py2_map = new PhongMaterial();
		py2_map.setDiffuseMap(mtn2);
		py2_map.setSpecularColor(Color.WHITE);
		pyr2.setMaterial(py2_map);
		pyr2.setTranslateX(150);
		pyr2.setTranslateY(-210);
		pyr2.setTranslateZ(450);
		root.getChildren().add(pyr2);
		
		// Define a second, smaller size of a mountain.
		TriangleMesh smallPyr = new TriangleMesh();
		// define (a trivial) texture map
		smallPyr.getTexCoords().addAll(
				0.5f, 0,
				0, 0.5f,
				1, 0.5f,
				0, 1,
				1, 1
				);
		// define vertices
		float h2 = 80;                    // Height
		float s2 = 160;                    // Base hypotenuse
		smallPyr.getPoints().addAll(
		        0,    0,    0,            // Point 0 - Top
		        0,    h2,    -s2/2,         // Point 1 - Front
		        -s2/2, h2,    0,            // Point 2 - Left
		        s2/2,  h2,    0,            // Point 3 - Right
		        0,    h2,    s2/2           // Point 4 - Back
		    );
		// define faces
		smallPyr.getFaces().addAll(
		        0,0,  2,1,  1,2,          // Front left face
		        0,0,  1,1,  3,1,          // Front right face
		        0,0,  3,1,  4,2,          // Back right face
		        0,0,  4,1,  2,2,          // Back left face
		        4,1,  1,4,  2,2,          // Bottom left face
		        4,1,  3,3,  1,4           // Bottom right face
		    ); 
		smallPyr.getFaceSmoothingGroups().addAll(
				1, 2, 3, 4, 5, 5);
		MeshView small_mtn = new MeshView(smallPyr);
		//pyramid.setDrawMode(DrawMode.LINE);
		final PhongMaterial lightMtn = new PhongMaterial();
		lightMtn.setDiffuseMap(mtn3);
		lightMtn.setSpecularColor(Color.WHITE);
		small_mtn.setMaterial(lightMtn);
		small_mtn.setTranslateX(200);
		small_mtn.setTranslateY(-90);
		small_mtn.setTranslateZ(200);
		root.getChildren().add(small_mtn);
		
		// Define another small mountain of a different texture
		MeshView sml_mtn2 = new MeshView(smallPyr);
		//pyramid.setDrawMode(DrawMode.LINE);
		final PhongMaterial rockMat = new PhongMaterial();
		rockMat.setDiffuseMap(mtn4);
		rockMat.setSpecularColor(Color.WHITE);
		sml_mtn2.setMaterial(rockMat);
		sml_mtn2.setTranslateX(-240);
		sml_mtn2.setTranslateY(-90);
		sml_mtn2.setTranslateZ(0);
		root.getChildren().add(sml_mtn2);
		
		ObjView drvr = new ObjView();
		try {
			drvr.load(ClassLoader.getSystemResource("chair.obj").toString());
		} catch (IOException e) {
			System.out.println("Trouble loading model");
			e.printStackTrace();
		}
		Group droid = drvr.getRoot();
		droid.setScaleX(70);
		droid.setScaleY(-70);
		droid.setScaleZ(-70);
		droid.setTranslateX(110);
		droid.setTranslateY(-150);
		
		root.getChildren().add(droid);
		for (Node n:droid.getChildren())
		{
			MeshView mv = (MeshView) n;
			Mesh m = ((MeshView) n).getMesh();
			//mv.setDrawMode(DrawMode.LINE);
			System.out.println(n);
			System.out.println(m);
			TriangleMesh tm = (TriangleMesh) m;
			System.out.println("Faces: "+tm.getFaceElementSize());
			System.out.println(tm.getFaces() );
			System.out.println(tm.getFaceSmoothingGroups());
			System.out.println("Normals: "+tm.getNormalElementSize());
			System.out.println(tm.getNormals());
			System.out.println("Points: "+tm.getPointElementSize());
			System.out.println(tm.getPoints());
			System.out.println("TexCoords: "+tm.getTexCoordElementSize());
			System.out.println(tm.getTexCoords());
		}
		
		// Add the main platform "xAxis"
		root.getChildren().addAll(xAxis);

		root.getChildren().addAll(sphere, car);

	}

	@Override
	public void start(Stage primaryStage) {

		// Build your Scene and Camera
		Group sceneRoot = new Group();
		constructWorld(sceneRoot);

		// Fourth parameter to indicate 3D world:
		Scene scene = new Scene(sceneRoot, sceneWidth, sceneHeight, true);
		ImagePattern pattern = new ImagePattern(sunset);
		scene.setFill(pattern);
		
		camera = new PerspectiveCamera(true);
		camera.setNearClip(0.1);
		camera.setFarClip(10000.0);
		scene.setCamera(camera);
		// translations through dolly
		cameraDolly = new Group();
		cameraDolly.setTranslateZ(-1000);
		cameraDolly.setTranslateX(0);
		cameraDolly.setTranslateY(-30);
		cameraDolly.getChildren().add(camera);
		sceneRoot.getChildren().add(cameraDolly);
		// rotation transforms
		Rotate xRotate = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
		Rotate yRotate = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
		camera.getTransforms().addAll(xRotate, yRotate);

		// Use keyboard to control camera position
		scene.setOnKeyPressed(event -> {
			double change = cameraQuantity;
			// What key did the user press?
			KeyCode keycode = event.getCode();

			Point3D delta = null;
			if (keycode == KeyCode.COMMA) {
				delta = new Point3D(0, 0, change);
			}
			if (keycode == KeyCode.PERIOD) {
				delta = new Point3D(0, 0, -change);
			}
			if (keycode == KeyCode.A) {
				delta = new Point3D(-change, 0, 0);
			}
			if (keycode == KeyCode.D) {
				delta = new Point3D(change, 0, 0);
			}
			if (keycode == KeyCode.W) {
				delta = new Point3D(0, -change, 0);
			}
			if (keycode == KeyCode.S) {
				delta = new Point3D(0, change, 0);
			}
			if (delta != null) {
				Point3D delta2 = camera.localToParent(delta);
				cameraDolly.setTranslateX(cameraDolly.getTranslateX() + delta2.getX());
				cameraDolly.setTranslateY(cameraDolly.getTranslateY() + delta2.getY());
				cameraDolly.setTranslateZ(cameraDolly.getTranslateZ() + delta2.getZ());

			}
		});

		// Use mouse to control camera rotation
		scene.setOnMousePressed(me -> {
			mousePosX = me.getSceneX();
			mousePosY = me.getSceneY();
		});

		scene.setOnMouseDragged(me -> {
			mouseOldX = mousePosX;
			mouseOldY = mousePosY;
			mousePosX = me.getSceneX();
			mousePosY = me.getSceneY();
			mouseDeltaX = (mousePosX - mouseOldX);
			mouseDeltaY = (mousePosY - mouseOldY);

			yRotate.setAngle(((yRotate.getAngle() - mouseDeltaX * 0.2) % 360 + 540) % 360 - 180); // +
			xRotate.setAngle(((xRotate.getAngle() + mouseDeltaY * 0.2) % 360 + 540) % 360 - 180); // -
		});

		primaryStage.setTitle("World1");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}