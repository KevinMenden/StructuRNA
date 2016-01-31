package Presenter;

import Model3D.*;
import PDBParser.Atom;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.ArrayList;

/**
 * Created by kevin_000 on 19.01.2016.
 * This Class contains methods for creating the 3D structure of the molecule
 * and to handle this structure
 */
public class Presenter3D {

    //Sequence length
    private int sequenceLength = 0;
    public void setSequenceLength(int sequenceLength) {this.sequenceLength = sequenceLength;}

    //SubScene
    SubScene subScene;

    //PerspectiveCamera
    PerspectiveCamera camera;

    //molecule assembler
    MoleculeAssembler moleculeAssembler;

    //StackPane for 3D
    //public StackPane structurePane;
    public Pane structurePane;
    //Setter for strucutrePane
    public void setStructurePane(Pane structurePane) {
        this.structurePane = structurePane;
    }

    //initial camer position (zooming)
    private final double CAMERA_CENTER_Z = -200;

    //Translatin and Rotations
    public Rotate cameraRotateX = new Rotate(0, new Point3D(1, 0, 0));
    public Rotate cameraRotateY = new Rotate(0, new Point3D(0, 1, 0));
    public Translate cameraTranslate = new Translate(0, 0, CAMERA_CENTER_Z);

    //The Group in with all the molecules
    public Group structureGroup = new Group();
    //MeshView group for all nucleotides
    private MoleculeMesh[] nucleotides;
    public MoleculeMesh[] getNucleotides() {        return nucleotides;    }
    public void setNucleotides(MoleculeMesh[] nucleotides) {        this.nucleotides = nucleotides;    }

    //The atoms as loaded from the pdb file, centered
    private Atom[] atoms;
    //Setter for atoms
    public void setAtoms(Atom[] atoms) {
        this.atoms = atoms;
        centerAllAtoms(this.atoms);
    }
    public Atom[] getAtoms() {
        return atoms;
    }

    //Materials for different coloring of bases
    PhongMaterial uracilMaterial = new PhongMaterial(Color.GREEN);
    PhongMaterial adenineMaterial = new PhongMaterial(Color.RED);
    PhongMaterial cytosineMaterial = new PhongMaterial(Color.BLUE);
    PhongMaterial guanineMaterial = new PhongMaterial(Color.YELLOW);

    public Presenter3D() {};

    /*
    Build the 3D structure with the MoleculeAssembler
    Set upt he structure group and the SubScene
    Add structure handling to the scene
     */
    public void make3DStructure(){

        //Assemble the molecules
        moleculeAssembler = new MoleculeAssembler(this.atoms, this.sequenceLength);
        moleculeAssembler.setAdenineMaterial(adenineMaterial);
        moleculeAssembler.setGuanineMaterial(guanineMaterial);
        moleculeAssembler.setCytosineMaterial(cytosineMaterial);
        moleculeAssembler.setUracilMaterial(uracilMaterial);
        moleculeAssembler.assembleMolecules();
        this.structureGroup = moleculeAssembler.getStructureGroup();
        this.nucleotides = moleculeAssembler.getNucleotides();

        //Initialize SubScene and camera

        subScene = new SubScene(structureGroup, 426, 553, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.BLACK);
        camera = new PerspectiveCamera(true);
        camera.setFarClip(10000.0);
        camera.setNearClip(0.1);
        //Add Transforms to stucture and camera
        structureGroup.getTransforms().addAll(cameraRotateX, cameraRotateY);
        camera.getTransforms().addAll(cameraTranslate);
        subScene.setCamera(camera);
        //Bind subscene to its pane
        subScene.widthProperty().bind(structurePane.widthProperty());
        subScene.heightProperty().bind(structurePane.heightProperty());

        //Set up handling of the structure
        MousHandler3D.addMouseHandler(structurePane, structureGroup, cameraRotateX, cameraRotateY, cameraTranslate);

    }

    //NOTE WORKING  - MIGHT GET IMPLEMENTED
    public void makeBallAndStickModel(){
        //Assemble the molecules
        moleculeAssembler = new MoleculeAssembler(this.atoms, this.sequenceLength);
        moleculeAssembler.setAdenineMaterial(adenineMaterial);
        moleculeAssembler.setGuanineMaterial(guanineMaterial);
        moleculeAssembler.setCytosineMaterial(cytosineMaterial);
        moleculeAssembler.setUracilMaterial(uracilMaterial);
        moleculeAssembler.assembleBallAndStickModel();
        this.structureGroup = moleculeAssembler.getStructureGroup();
        //this.nucleotides = moleculeAssembler.getNucleotides();

        //Initialize SubScene and camera

        subScene = new SubScene(structureGroup, 426, 553, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.WHITE);
        camera = new PerspectiveCamera(true);
        camera.setFarClip(10000.0);
        camera.setNearClip(0.1);
        //Add Transforms to stucture and camera
        structureGroup.getTransforms().addAll(cameraRotateX, cameraRotateY);
        camera.getTransforms().addAll(cameraTranslate);
        subScene.setCamera(camera);
        //Bind subscene to its pane
        subScene.widthProperty().bind(structurePane.widthProperty());
        subScene.heightProperty().bind(structurePane.heightProperty());

        //Set up handling of the structure
        MousHandler3D.addMouseHandler(structurePane, structureGroup, cameraRotateX, cameraRotateY, cameraTranslate);
    }


    /**
     * Center Points around the middle
     *important to for rotation
     * @param points
     */
    public static ArrayList<Point3D> center(ArrayList<Point3D> points) {
        ArrayList<Point3D> result=new ArrayList<>(points.size());
        if (points.size() > 0) {
            double[] center = {0, 0, 0};

            for (Point3D point : points) {
                center[0] += point.getX();
                center[1] += point.getY();
                center[2] += point.getZ();
            }
            center[0] /= points.size();
            center[1] /= points.size();
            center[2] /= points.size();

            for (Point3D point : points) {
                result.add(point.subtract(new Point3D(center[0], center[1], center[2])));
            }
        }
        return result;
    }

    /**
     * Apply center() for all elements of atoms
     * @param atoms
     */
    private void centerAllAtoms(Atom[] atoms){
        ArrayList<Point3D> points = new ArrayList<>(atoms.length);
        //Add all points to the ArrayList
        for (Atom atom : atoms){
            points.add(atom.getPoint());
        }
        //Center the points
        points = center(points);
        int i = 0;
        for (Point3D point3D : points){
            atoms[i].setPoint(point3D);
            i++;
        }
        //Update the Array of Atoms
        //this.atoms = atoms;
    }




    /*
    Color all nucleotides differently and make the molecules again
     */
    public void colorByNucleotide(){
        //Different colors for every nucleotide
        uracilMaterial.setDiffuseColor(Color.GREEN);
        adenineMaterial.setDiffuseColor(Color.RED);
        cytosineMaterial.setDiffuseColor(Color.BLUE);
        guanineMaterial.setDiffuseColor(Color.YELLOW);
    }

    /*
    Color nucleotides by basetype (Purine - Pyrimidine)
     */
    public void colorByBasetype(){
        uracilMaterial.setDiffuseColor(Color.BLUE);
        adenineMaterial.setDiffuseColor(Color.YELLOW);
        cytosineMaterial.setDiffuseColor(Color.BLUE);
        guanineMaterial.setDiffuseColor(Color.YELLOW);
    }
    /*
    Bring structure back to center
     */
    public void centerStructure(){
        cameraTranslate.setZ(CAMERA_CENTER_Z);
        cameraRotateX.setAngle(0);
        cameraRotateY.setAngle(0);
        structureGroup.setTranslateX(0);
        structureGroup.setTranslateY(0);
    }

}
