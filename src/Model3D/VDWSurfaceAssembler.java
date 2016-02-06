package Model3D;

import PDBParser.Atom;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.ArrayList;

/**
 * Created by Kevin Menden  on 06.02.2016.
 *
 * Class contains method for modeling the Van der Waals surface of a molecule
 * given an array of its atoms
 */
public class VDWSurfaceAssembler {

    //Van der Waals radius for every atom
    private static final double H_VDW_RADIUS = 1.1;
    private static final double C_VDW_RADIUS = 1.7;
    private static final double O_VDW_RADIUS = 1.52;
    private static final double P_VDW_RADIUS = 1.8;
    private static final double N_VDW_RADIUS = 1.55;
    //Material for every atom
    private static final PhongMaterial H_MATERIAL = new PhongMaterial(Color.ANTIQUEWHITE);
    private static final PhongMaterial C_MATERIAL = new PhongMaterial(Color.GREENYELLOW);
    private static final PhongMaterial O_MATERIAL = new PhongMaterial(Color.RED);
    private static final PhongMaterial P_MATERIAL = new PhongMaterial(Color.web("943D00"));
    private static final PhongMaterial N_MATERIAL = new PhongMaterial(Color.BLUE);

    //Translation and Rotations
    private final double CAMERA_CENTER_Z = -200;
    public Rotate cameraRotateX = new Rotate(0, new Point3D(1, 0, 0));
    public Rotate cameraRotateY = new Rotate(0, new Point3D(0, 1, 0));
    public Translate cameraTranslate = new Translate(0, 0, CAMERA_CENTER_Z);

    //Important class objects
    private Group surfaceGroup;
    private ArrayList<Sphere> surfaceAtoms = new ArrayList<>();
    private Pane structurePane;
    private SubScene surfaceSubScene;
    private PerspectiveCamera camera;

    //Constructor
    public VDWSurfaceAssembler(){
        H_MATERIAL.setSpecularColor(Color.LIGHTGOLDENRODYELLOW);
        C_MATERIAL.setSpecularColor(Color.LIGHTGREEN);
        O_MATERIAL.setSpecularColor(Color.DARKRED);
        P_MATERIAL.setSpecularColor(Color.DARKORANGE);
        N_MATERIAL.setSpecularColor(Color.DARKBLUE);
    }



    public SubScene addVanDerWaalsSurface(Atom[] atoms){

        Sphere sphere;

        surfaceGroup = new Group();

        for (Atom atom : atoms){
            switch (atom.getElement()){
                case "P":
                    sphere = new Sphere(P_VDW_RADIUS);
                    sphere.setTranslateX(atom.getPoint().getX());
                    sphere.setTranslateY(atom.getPoint().getY());
                    sphere.setTranslateZ(atom.getPoint().getZ());
                    sphere.setMaterial(P_MATERIAL);
                    surfaceAtoms.add(sphere);
                    break;
                case "H":
                    sphere = new Sphere(H_VDW_RADIUS);
                    sphere.setTranslateX(atom.getPoint().getX());
                    sphere.setTranslateY(atom.getPoint().getY());
                    sphere.setTranslateZ(atom.getPoint().getZ());
                    sphere.setMaterial(H_MATERIAL);
                    surfaceAtoms.add(sphere);
                    break;
                case "C":
                    sphere = new Sphere(C_VDW_RADIUS);
                    sphere.setTranslateX(atom.getPoint().getX());
                    sphere.setTranslateY(atom.getPoint().getY());
                    sphere.setTranslateZ(atom.getPoint().getZ());
                    sphere.setMaterial(C_MATERIAL);
                    surfaceAtoms.add(sphere);
                    break;
                case "N":
                    sphere = new Sphere(N_VDW_RADIUS);
                    sphere.setTranslateX(atom.getPoint().getX());
                    sphere.setTranslateY(atom.getPoint().getY());
                    sphere.setTranslateZ(atom.getPoint().getZ());
                    sphere.setMaterial(N_MATERIAL);
                    surfaceAtoms.add(sphere);
                    break;
                case "O":
                    sphere = new Sphere(O_VDW_RADIUS);
                    sphere.setTranslateX(atom.getPoint().getX());
                    sphere.setTranslateY(atom.getPoint().getY());
                    sphere.setTranslateZ(atom.getPoint().getZ());
                    sphere.setMaterial(O_MATERIAL);
                    surfaceAtoms.add(sphere);
                    break;
            }
        }

        surfaceGroup.getChildren().addAll(surfaceAtoms);

        surfaceSubScene = new SubScene(surfaceGroup, 426, 553, true, SceneAntialiasing.BALANCED);
        surfaceSubScene.setFill(Color.BLACK);
        camera = new PerspectiveCamera(true);
        camera.setFarClip(10000.0);
        camera.setNearClip(0.1);
        //Add Transforms to stucture and camera
        surfaceGroup.getTransforms().addAll(cameraRotateX, cameraRotateY);
        camera.getTransforms().addAll(cameraTranslate);
        surfaceSubScene.setCamera(camera);
        //Bind subscene to its pane
        surfaceSubScene.widthProperty().bind(structurePane.widthProperty());
        surfaceSubScene.heightProperty().bind(structurePane.heightProperty());


        return surfaceSubScene;

    }

    public void switchOnMouseHandling(){
        MousHandler3D.addMouseHandler(structurePane, surfaceGroup, cameraRotateX, cameraRotateY, cameraTranslate);
    }




    /*
    GETTER AND SETTER
     */

    public static PhongMaterial getcMaterial() {
        return C_MATERIAL;
    }

    public static double getcVdwRadius() {
        return C_VDW_RADIUS;
    }

    public static PhongMaterial gethMaterial() {
        return H_MATERIAL;
    }

    public static double gethVdwRadius() {
        return H_VDW_RADIUS;
    }

    public static PhongMaterial getnMaterial() {
        return N_MATERIAL;
    }

    public static double getnVdwRadius() {
        return N_VDW_RADIUS;
    }

    public static PhongMaterial getoMaterial() {
        return O_MATERIAL;
    }

    public static double getoVdwRadius() {
        return O_VDW_RADIUS;
    }

    public static PhongMaterial getpMaterial() {
        return P_MATERIAL;
    }

    public static double getpVdwRadius() {
        return P_VDW_RADIUS;
    }

    public Pane getStructurePane() {
        return structurePane;
    }

    public void setStructurePane(Pane structurePane) {
        this.structurePane = structurePane;
    }

    public ArrayList<Sphere> getSurfaceAtoms() {
        return surfaceAtoms;
    }

    public void setSurfaceAtoms(ArrayList<Sphere> surfaceAtoms) {
        this.surfaceAtoms = surfaceAtoms;
    }

    public Group getSurfaceGroup() {
        return surfaceGroup;
    }

    public void setSurfaceGroup(Group surfaceGroup) {
        this.surfaceGroup = surfaceGroup;
    }

    public SubScene getSurfaceSubScene() {
        return surfaceSubScene;
    }

    public void setSurfaceSubScene(SubScene surfaceSubScene) {
        this.surfaceSubScene = surfaceSubScene;
    }

}
