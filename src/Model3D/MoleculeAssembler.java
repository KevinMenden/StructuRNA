package Model3D;

import HBondInference.HydrogonBonds;
import PDBParser.Atom;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.ArrayList;

/**
 * Created by kevin_000 on 27.01.2016.
 */
public class MoleculeAssembler {

    //Materials for different coloring of bases
    private PhongMaterial uracilMaterial = new PhongMaterial(Color.GREEN);
    private PhongMaterial adenineMaterial = new PhongMaterial(Color.RED);
    private PhongMaterial cytosineMaterial = new PhongMaterial(Color.BLUE);
    private PhongMaterial guanineMaterial = new PhongMaterial(Color.YELLOW);
    //Materials for connections
    private PhongMaterial phosphateConnectionMaterial = new PhongMaterial(Color.LIGHTGOLDENRODYELLOW);
    private PhongMaterial sugarConnectionMaterial = new PhongMaterial(Color.DARKCYAN);

    //The Group in with all the molecules
    private Group structureGroup = new Group();
    public Group getStructureGroup() {        return structureGroup;    }

    //sequence
    private int sequenceLength;
    public int getSequenceLength() {        return sequenceLength;    }
    public void setSequenceLength(int sequenceLength) {        this.sequenceLength = sequenceLength;    }

    //MeshView group for all nucleotides
    MoleculeMesh[] nucleotides;
    public MoleculeMesh[] getNucleotides() {        return nucleotides;    }
    public void setNucleotides(MoleculeMesh[] nucleotides) {        this.nucleotides = nucleotides;    }

    //Array of all atoms
    Atom[] atoms;
    public Atom[] getAtoms() {
        return atoms;
    }
    public void setAtoms(Atom[] atoms) {
        this.atoms = atoms;
    }

    public MoleculeAssembler(Atom[] atoms, int sequenceLength){
        this.atoms = atoms;
        this.sequenceLength = sequenceLength;
        //Center all atom coordinates
        centerAllAtoms(this.atoms);
    }

    /**
     * For every molecule in the PDB file make a shape and add it
     * to the group
     * Returns the group with all molecules in it
     */
    public void assembleMolecules(){
        //Center all atom coordinates
        //centerAllAtoms(this.atoms);

        //SET UP ARRAY - TESTING STAGE
        nucleotides = new MoleculeMesh[this.sequenceLength];


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
        HydrogonBonds hydrogonBonds = new HydrogonBonds(sequenceLength);
        hydrogonBonds.inferHydrogenBonds(this.atoms);
        ArrayList<Cylinder> hbonds = hydrogonBonds.getHbonds();
        for (Cylinder cylinder : hbonds){
            structureGroup.getChildren().add(cylinder);
        }
        for (Sphere sphere : hydrogonBonds.getBondAtoms()){
            structureGroup.getChildren().add(sphere);
        }
        for (Cylinder c : hydrogonBonds.getHbondAtomConnections()){
            structureGroup.getChildren().add(c);
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
        return Line3D.makeLine3D(origin, target, phosphateConnectionMaterial, 0.3);
    }

    /**
     * Create a thinner connection between phosphate and sugars
     * COLOR: BLUE
     * @param origin
     * @param target
     * @return
     */
    public Cylinder createSugarConnection(Point3D origin, Point3D target) {
        return Line3D.makeLine3D(origin, target, sugarConnectionMaterial, 0.05);
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
        //makeMolecules();
    }

    /*
    Color nucleotides by basetype (Purine - Pyrimidine)
     */
    public void colorByBasetype(){
        uracilMaterial.setDiffuseColor(Color.BLUE);
        adenineMaterial.setDiffuseColor(Color.YELLOW);
        cytosineMaterial.setDiffuseColor(Color.BLUE);
        guanineMaterial.setDiffuseColor(Color.YELLOW);
        //makeMolecules();
    }

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

    public PhongMaterial getAdenineMaterial() {
        return adenineMaterial;
    }

    public void setAdenineMaterial(PhongMaterial adenineMaterial) {
        this.adenineMaterial = adenineMaterial;
    }

    public PhongMaterial getCytosineMaterial() {
        return cytosineMaterial;
    }

    public void setCytosineMaterial(PhongMaterial cytosineMaterial) {
        this.cytosineMaterial = cytosineMaterial;
    }

    public PhongMaterial getGuanineMaterial() {
        return guanineMaterial;
    }

    public void setGuanineMaterial(PhongMaterial guanineMaterial) {
        this.guanineMaterial = guanineMaterial;
    }

    public PhongMaterial getUracilMaterial() {
        return uracilMaterial;
    }

    public void setUracilMaterial(PhongMaterial uracilMaterial) {
        this.uracilMaterial = uracilMaterial;
    }
}
