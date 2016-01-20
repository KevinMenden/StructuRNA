package Presenter;

import Model3D.*;
import PDBParser.Atom;
import PDBParser.PDBFile;
import View.View;
import javafx.event.Event;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * Created by kevin_000 on 19.01.2016.
 * This Class contains methods for creating the 3D structure of the molecule
 * and to handle this structure
 */
public class Presenter3D {

    //Mouse positions when mouse is pressed
    double mousePosX, mousePosY;

    //Translatin and Rotations
    public Rotate cameraRotateX = new Rotate(0, new Point3D(1, 0, 0));
    public Rotate cameraRotateY = new Rotate(0, new Point3D(0, 1, 0));
    public Translate cameraTranslate = new Translate(0, 0, -1000);

    //The Group in with all the molecules
    public Group structureGroup = new Group();

    //The atoms as loaded from the pdb file
    private Atom[] atoms;
    //Setter for atoms
    public void setAtoms(Atom[] atoms) {
        this.atoms = atoms;
    }

    //Materials for different coloring of bases
    PhongMaterial uracilMaterial = new PhongMaterial(Color.GREEN);
    PhongMaterial adenineMaterial = new PhongMaterial(Color.RED);
    PhongMaterial cytosineMaterial = new PhongMaterial(Color.BLUE);
    PhongMaterial guanineMaterial = new PhongMaterial(Color.YELLOW);

    public Presenter3D() {};

    /**
     * Save the current position of cursor if mouse is clicked
     * Method gets MouseEvent from Controller
     * @param event
     */
    public void handleMousePressedEvent(MouseEvent event){
        mousePosX = event.getSceneX();
        mousePosY = event.getSceneY();
    }

    /**
     * Handle rotating and zooming of the 3D object
     * Gets MouseEvent and PersepectiveCamera from Controller
     * @param event
     * @param camera
     */
    public void handleMouseDraggedEvent(MouseEvent event, PerspectiveCamera camera){
        double dY = event.getSceneY() - mousePosY;
        double dX = event.getSceneX() - mousePosX;
        //Zoom if shift is down
        if (event.isShiftDown()){
            cameraTranslate.setZ(cameraTranslate.getZ() + dY);
        }
        //Else rotate the camera around the object
        else{
            cameraRotateY.setAngle(cameraRotateY.getAngle() + dX);
            cameraRotateX.setAngle(cameraRotateX.getAngle() - dY);
        }
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

        //Atom[] atoms = this.pdbFile.getAtoms();
        //Get rid of old stuff
        structureGroup.getChildren().clear();

        Guanine guanine = new Guanine();
        Adenine adenine = new Adenine();
        Cytosine cytosine = new Cytosine();
        Uracil uracil = new Uracil();
        Ribose ribose = new Ribose();

        //Starting values
        String currentBase = atoms[0].getBase();
        int resdiueNumber = atoms[0].getResidueNumber();

        //Create spheres for every phosphate atom and add them to structure
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
            }
        }
        //Connect all phosphate atoms with cylinders
        int counter = 0;
        Point3D origin = null;
        for (Atom atom : atoms) {
            if (atom.getElement().equals("P")) {
                if (counter >= 1){
                    Point3D target = atom.getPoint();
                    Cylinder cylinder = createConnection(origin, target);
                    this.structureGroup.getChildren().add(cylinder);
                }
                origin = atom.getPoint();
                counter += 1;
            }
        }

        //Connect all phosphates with sugars
        connectPhosphatesWithSugars(atoms);

        //Connect all bases with sugars
        connectBasesWithSugars(atoms);

        /*
        CREATE MESH VIEWS FOR ALL BASES AND SUGARS
         */
        for (Atom atom : atoms) {
            //Check if base has changed or if last element is reached
            //Add coordinates of each atom to the corresponding base
            if (atom.getResidueNumber() == resdiueNumber && (!atom.equals(atoms[atoms.length-1]))){
                cytosine.fillCoordinates(atom);
                adenine.fillCoordinates(atom);
                guanine.fillCoordinates(atom);
                uracil.fillCoordinates(atom);
                ribose.fillCoordinates(atom);
            } else{
                //Make Mesh for ribose
                ribose.makeMesh();
                structureGroup.getChildren().add(ribose.getMeshView());
                //Add the right base to the view if new base is reached
                switch (currentBase){
                    case "C":
                        cytosine.setMaterial(cytosineMaterial);
                        cytosine.makeMesh();
                        structureGroup.getChildren().add(cytosine.getMeshView());
                        ribose.makeTooltip(cytosine.getNucleotideInfo());
                        cytosine = new Cytosine();
                        break;
                    case "G":
                        guanine.setMaterial(guanineMaterial);
                        guanine.makeMesh();
                        structureGroup.getChildren().add(guanine.getMeshView());
                        ribose.makeTooltip(guanine.getNucleotideInfo());
                        guanine = new Guanine();
                        break;
                    case "U":
                        uracil.setMaterial(uracilMaterial);
                        uracil.makeMesh();
                        structureGroup.getChildren().add(uracil.getMeshView());
                        ribose.makeTooltip(uracil.getNucleotideInfo());
                        uracil = new Uracil();
                        break;
                    case "A":
                        adenine.setMaterial(adenineMaterial);
                        adenine.makeMesh();
                        structureGroup.getChildren().add(adenine.getMeshView());
                        ribose.makeTooltip(adenine.getNucleotideInfo());
                        adenine = new Adenine();
                        break;
                    default: break;
                }
                //Update values and bases
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
        line.setMaterial(new PhongMaterial(Color.BROWN));

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
        line.setMaterial(new PhongMaterial(Color.RED));

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

}
