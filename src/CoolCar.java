import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
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
		
		Box xAxis = new Box(800, 10, 800);
		xAxis.setMaterial(greenMaterial);
//		Box yAxis = new Box(10, 200, 10);
//		yAxis.setMaterial(greenMaterial); 
		Box zAxis = new Box(10, 10, 200);
		zAxis.setMaterial(yelMaterial);

		final Sphere sphere = new Sphere(30);
		sphere.setMaterial(yelMaterial);
		// sphere.setDrawMode(DrawMode.LINE);

		sphere.setTranslateX(0);
		sphere.setTranslateY(-210);
		sphere.setTranslateZ(300);

		Box box = new Box(20, 5, 20);
		box.setMaterial(redMaterial);
		// box.setDrawMode(DrawMode.LINE);

		box.setTranslateX(0);
		box.setTranslateY(-5);
		box.setTranslateZ(-20);
		
		float[] points = 
			{	
				50, 0, 0,  // v0 (iv0 = 0)
				45, 10, 0, // v1 (iv1 = 1)
				55, 10, 0  // v2 (iv2 = 2)
			};
		
		float[] texCoords = 
			{ 	
				1.5f, 1.5f, // t0 (it0 = 0)
				1.0f, 2.0f, // t1 (it1 = 1)
				2.0f, 2.0f  // t2 (it2 = 2)
			};
		
		int[] faces = 
			{
				0, 0, 2, 2, 1, 1, // iv0, it0, iv2, it2, iv1, it1 (front face)
				0, 0, 1, 1, 2, 2  // iv0, it0, iv1, it1, iv2, it2 back face
			};
		
		// Create a TriangleMesh
		TriangleMesh mtn = new TriangleMesh();
		mtn.getPoints().addAll(points);
		mtn.getTexCoords().addAll(texCoords);
		mtn.getFaces().addAll(faces);
		
		MeshView pyramid = new MeshView(mtn);
		pyramid.setDrawMode(DrawMode.FILL);
		pyramid.setMaterial(lbrownMat);
		pyramid.setTranslateX(0);
		pyramid.setTranslateY(-40);
		pyramid.setTranslateZ(0);
		pyramid.setMesh(mtn);
		pyramid.setTranslateY(-15);
		// pyramid.setDrawMode(DrawMode.LINE);
		
		root.getChildren().addAll(xAxis, zAxis);

		root.getChildren().addAll(sphere, box, pyramid);

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
		cameraDolly.setTranslateZ(-500);
		cameraDolly.setTranslateX(0);
		cameraDolly.setTranslateY(-10);
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