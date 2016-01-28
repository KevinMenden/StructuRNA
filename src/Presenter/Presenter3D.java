package Presenter;

import Model3D.*;
import PDBParser.Atom;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
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

    //StackPane for 3D
    //public StackPane structurePane;
    public AnchorPane structurePane;
    //Setter for strucutrePane
    public void setStructurePane(AnchorPane structurePane) {
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
    Set up all the action events for moving the 3D structure
    Initialize the SubScene and set up the camera
     */
    public void setActionEvents(){

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



    /**
     * Returns the size of a Nucleotide when given its type
     * @param moleculeType
     * @return
     */
        private int getMoleculeSize(String moleculeType){
        int moleculeSize = 0;

        switch (moleculeType){
            case "C":
                moleculeSize = 18;
                break;
            case "A":
                moleculeSize = 27;
                break;
            case "U":
                moleculeSize = 18;
                break;
            case "G":
                moleculeSize = 27;
                break;
            case "T":
                moleculeSize = 18;
                break;
            default:
                System.out.println("Could not identify molecule type");
                break;
        }

        return moleculeSize;
    }


    /**
     * For every molecule in the PDB file make a shape and add it
     * to the group
     * Returns the group with all molecules in it
     */
    public void makeMolecules(){
        //Center all atom coordinates
        centerAllAtoms(this.atoms);

        //SET UP ARRAY - TESTING STAGE
        nucleotides = new MoleculeMesh[sequenceLength];


        //Atom[] atoms = this.pdbFile.getAtoms();
        //Get rid of old stuff
        structureGroup.getChildren().clear();

        //Initialize every residue
        Guanine guanine = new Guanine();
        Adenine adenine = new Adenine();
        Cytosine cytosine = new Cytosine();
        Uracil uracil = new Uracil();
        Ribose ribose = new Ribose();

        //Starting values
        String currentBase = atoms[0].getBase();
        int resdiueNumber = atoms[0].getResidueNumber();
        int counter = 0;
        Point3D origin = null;
        /*
        Create spheres for every phosphate atom and add them to structure
        Add connections between the phosphates and add them as well
         */
        for (Atom atom : atoms) {
            if (atom.getElement().equals("P")){
                Phosphate phosphate = new Phosphate();
                //Install tooltip
                Tooltip tooltip = new Tooltip(atom.getBase() + atom.getResidueNumber());
                tooltip.install(phosphate, tooltip);
                //position the phosphate and add it to structureGroup
                phosphate.setTranslateX(atom.getCoordinates()[0]);
                phosphate.setTranslateY(atom.getCoordinates()[1]);
                phosphate.setTranslateZ(atom.getCoordinates()[2]);
                this.structureGroup.getChildren().add(phosphate);

                //Make connection
                if (counter >= 1){
                    Point3D target = atom.getPoint();
                    Cylinder cylinder = createConnection(origin, target);
                    this.structureGroup.getChildren().add(cylinder);
                }
                //update variables
                origin = atom.getPoint();
                counter++;
            }
        }

        //Connect all phosphates with sugars
        connectPhosphatesWithSugars(atoms);

        //Connect all bases with sugars
        connectBasesWithSugars(atoms);
        counter = 0;
        /*
        CREATE MESH VIEWS FOR ALL BASES AND SUGARS
         */
        for (Atom atom : atoms) {
            //If Atom is part of the structure, make a Sphere representation and add it to the Structure group
            if (atom.isPartOfStructure()) {structureGroup.getChildren().add(atom.getAtomSphere());}

            //Check if base has changed or if last element is reached
            //If not, add coordinates of each atom to the corresponding base
            if (atom.getResidueNumber() == resdiueNumber && (!atom.equals(atoms[atoms.length-1]))){
                cytosine.fillCoordinates(atom);
                adenine.fillCoordinates(atom);
                guanine.fillCoordinates(atom);
                uracil.fillCoordinates(atom);
                ribose.fillCoordinates(atom);
            } else{
                //NEW BASE REACHED OR EOF
                //Add the right base to the view
                //Make Tooltips, set Materials
                switch (currentBase){
                    case "C":
                        cytosine.setMaterial(cytosineMaterial);
                        ribose.setMaterial(cytosineMaterial);
                        cytosine.makeMesh();
                        structureGroup.getChildren().add(cytosine.getMeshView());
                        nucleotides[counter] = cytosine.getMeshView() ;
                        ribose.makeTooltip(cytosine.getNucleotideInfo());
                        cytosine = new Cytosine();
                        break;
                    case "G":
                        guanine.setMaterial(guanineMaterial);
                        ribose.setMaterial(guanineMaterial);
                        guanine.makeMesh();
                        structureGroup.getChildren().add(guanine.getMeshView());
                        nucleotides[counter] = guanine.getMeshView();
                        ribose.makeTooltip(guanine.getNucleotideInfo());
                        guanine = new Guanine();
                        break;
                    case "U":
                        uracil.setMaterial(uracilMaterial);
                        ribose.setMaterial(uracilMaterial);
                        uracil.makeMesh();
                        structureGroup.getChildren().add(uracil.getMeshView());
                        nucleotides[counter] = uracil.getMeshView();
                        ribose.makeTooltip(uracil.getNucleotideInfo());
                        uracil = new Uracil();
                        break;
                    case "A":
                        adenine.setMaterial(adenineMaterial);
                        ribose.setMaterial(adenineMaterial);
                        adenine.makeMesh();
                        structureGroup.getChildren().add(adenine.getMeshView());
                        nucleotides[counter] = adenine.getMeshView();
                        ribose.makeTooltip(adenine.getNucleotideInfo());
                        adenine = new Adenine();
                        break;
                    default: break;
                }

                //Make Mesh for ribose
                ribose.makeMesh();
                structureGroup.getChildren().add(ribose.getMeshView());

                //Update values and bases
                counter++;
                ribose = new Ribose();
                currentBase = atom.getBase();
                resdiueNumber = atom.getResidueNumber();
                cytosine.fillCoordinates(atom);
                adenine.fillCoordinates(atom);
                guanine.fillCoordinates(atom);
                uracil.fillCoordinates(atom);
                ribose.fillCoordinates(atom);
            }
        }
    }

    /**
     * Create a 3D cylinder that connects to points
     * Code taken from: Rahel Lüthy, www.netzwerg.ch
     * @param origin
     * @param target
     * @return
     */
    public Cylinder createConnection(Point3D origin, Point3D target){
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = target.subtract(origin);
        double height = diff.magnitude();

        Point3D mid = target.midpoint(origin);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        Cylinder line = new Cylinder(0.3, height);
        line.setMaterial(new PhongMaterial(Color.LIGHTGOLDENRODYELLOW));

        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

        return line;
    }

    /**
     * Create a thinner connection between phosphate and sugars
     * COLOR: BLUE
     * @param origin
     * @param target
     * @return
     */
    public Cylinder createSugarConnection(Point3D origin, Point3D target) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = target.subtract(origin);
        double height = diff.magnitude();

        Point3D mid = target.midpoint(origin);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        Cylinder line = new Cylinder(0.1, height);
        line.setMaterial(new PhongMaterial(Color.LIGHTCYAN));

        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

        return line;
    }

    /*
Connect all Phosphates with the right sugars
 */
    private void connectPhosphatesWithSugars(Atom[] atoms){
        Point3D cThreePrime = null;
        Point3D cThreePrimeOld = null;
        Point3D cFourPrime = null;
        Point3D cFifePrime = null;
        Point3D phosphate = null;
        Cylinder cylinder = null;
        int resideNumber = 1;
        //Go through all atoms
        for (Atom atom : atoms) {
            //If new residue is reached, make connections
            if (atom.getResidueNumber() > resideNumber && resideNumber > 2){
                cylinder = createSugarConnection(phosphate, cFifePrime);
                this.structureGroup.getChildren().add(cylinder);
                cylinder = createSugarConnection(cFifePrime, cFourPrime);
                this.structureGroup.getChildren().add(cylinder);
                if (cThreePrimeOld != null) {
                    cylinder = createSugarConnection(cThreePrimeOld, phosphate);
                    this.structureGroup.getChildren().add(cylinder);
                }
            }
            //Save necessary 3D points of atoms that are connected
            switch (atom.getIdentity()){
                case "C3'":
                    cThreePrimeOld = cThreePrime;
                    cThreePrime = atom.getPoint();
                    break;
                case "C4'":
                    cFourPrime = atom.getPoint();
                    break;
                case "C5'":
                    cFifePrime = atom.getPoint();
                    break;
                case "P":
                    phosphate = atom.getPoint();
                    break;
                default: break;
            }
            //Update current residue number
            resideNumber = atom.getResidueNumber();
        }
    }

    /**
     * Connect all bases with the corresponding sugars
     * @param atoms
     */
    private void connectBasesWithSugars(Atom[] atoms){
        Point3D cOnePrime = null;
        Point3D guanineAtom = null;
        Point3D adenineAtom = null;
        Point3D uracilAtom = null;
        Point3D cytosineAtom = null;
        int residueNumber = atoms[0].getResidueNumber();
        String residueType = atoms[0].getBase();
        Cylinder cylinder = null;

        //Go through all atoms of molecule
        for (Atom atom : atoms) {
            //If new nucleotide is reached, create a connection
            if (residueNumber != atom.getResidueNumber()){
                switch (residueType){
                    case "G":
                        cylinder = createSugarConnection(cOnePrime, guanineAtom);
                        break;
                    case "A":
                        cylinder = createSugarConnection(cOnePrime, adenineAtom);
                        break;
                    case "C":
                        cylinder = createSugarConnection(cOnePrime, cytosineAtom);
                        break;
                    case "U":
                        cylinder = createSugarConnection(cOnePrime, uracilAtom);
                        break;
                    default:
                        System.out.println("No matching residue type");
                        break;
                }
                this.structureGroup.getChildren().add(cylinder);
            }
            switch (atom.getIdentity()){
                case "C1'":
                    cOnePrime = atom.getPoint();
                    break;
                case "N9":
                    guanineAtom = atom.getPoint();
                    adenineAtom = atom.getPoint();
                    break;
                case "N1":
                    cytosineAtom = atom.getPoint();
                    uracilAtom = atom.getPoint();
                default: break;
            }
            //Update residueNumber and residueType for next iteration
            residueNumber = atom.getResidueNumber();
            residueType = atom.getBase();
        }
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

    public void centerStructure(){
        cameraTranslate.setZ(CAMERA_CENTER_Z);
        cameraRotateX.setAngle(0);
        cameraRotateY.setAngle(0);
        structureGroup.setTranslateX(0);
        structureGroup.setTranslateY(0);
    }

}
