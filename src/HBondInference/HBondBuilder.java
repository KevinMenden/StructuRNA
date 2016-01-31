package HBondInference;

import Model3D.Line3D;
import PDBParser.Atom;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;


import java.util.ArrayList;

/**
 * Created by Kevin Menden on 26.01.2016.
 *
 * Representation of a nucleotide for h bond inference
 * Contains methods to infer H-bonds between two objects of the class
 */
public class HBondBuilder {

    //H bond building atoms
    private Point3D N1;
    private Point3D N2;
    private Point3D N3;
    private Point3D N4;
    private Point3D N6;
    private Point3D O2;
    private Point3D O4;
    private Point3D O6;
    private Point3D H21;
    private Point3D H62;
    private Point3D H4;
    private Point3D H3;
    private Point3D H1;
    private Point3D H41;
    private Point3D C4;
    private Point3D C6;
    private Point3D C2;


    //stings
    private String nitrogen = "N";
    private String oxygen = "O";
    private String hydrogen = "H";

    //ArrayList of hbonds
    private ArrayList<Cylinder> hbonds = new ArrayList<>();

    //ArrayList of bond building atoms
    private ArrayList<Sphere> bondAtoms = new ArrayList<>();

    //ArrayList connecting the bond building atoms to the structure
    private ArrayList<Cylinder> bondAtomConnections = new ArrayList<>();

    //Base type and residueNumber with their getters and setter
    String baseType;
    int resdieNumber;


    Exception NucleotideNotSetExeption = new NullPointerException();

    /**
     * Set the atom
     * @param atom
     */
    public void setAtom(Atom atom){
        String id = atom.getIdentity();
        switch (id){
            case "N1":
                N1 = atom.getPoint(); break;
            case "N2":
                N2 = atom.getPoint(); break;
            case "N3":
                N3 = atom.getPoint(); break;
            case "N4":
                N4 = atom.getPoint(); break;
            case "N6":
                N6 = atom.getPoint(); break;
            case "O2":
                O2 = atom.getPoint(); break;
            case "O4":
                O4 = atom.getPoint(); break;
            case "O6":
                O6 = atom.getPoint(); break;
            case "H21":
                H21 = atom.getPoint(); break;
            case "H62":
                H62 = atom.getPoint(); break;
            case "H4":
                H4 = atom.getPoint(); break;
            case "H3":
                H3 = atom.getPoint(); break;
            case "H1":
                H1 =atom.getPoint(); break;
            case "H41":
                H41 = atom.getPoint(); break;
            case "C4":
                C4 = atom.getPoint(); break;
            case "C6":
                C6 = atom.getPoint(); break;
            case "C2":
                C2 = atom.getPoint(); break;
            default:break;
        }
    }

    /**
     * For two HBondBuilder objects, check if they have a HBond connection
     * @param builderA
     * @param builderB
     * @return
     */
    public  boolean isHbond(HBondBuilder builderA, HBondBuilder builderB){
        boolean bool = false;

        try{

        //Check if possible Watson Crick base pair
        //If yes, check if H bond
        if (builderA.isAdenine() && builderB.isUracil()){
            bool = isAdenineUracilBond(builderA, builderB);
        }
        else if (builderA.isUracil() && builderB.isAdenine()){
            bool = isAdenineUracilBond(builderB, builderA);
        }
        else if (builderA.isCytosine() && builderB.isGuanine()){
            bool = isCytosineGuanineBond(builderA, builderB);
        }
        else if (builderA.isGuanine() && builderB.isCytosine()) {
                bool = isCytosineGuanineBond(builderB, builderA);
            }
        }catch (NullPointerException e){
         System.out.println(builderA.getBaseType());
            //System.out.println(builderB.getBaseType());
            System.out.println(builderB.getResdieNumber());
        }




        return bool;
    }
    /*
    Check for possible A-U H bond (no angles checked, only distances!)
     */
    private  boolean isAdenineUracilBond(HBondBuilder adenine, HBondBuilder uracil){
        boolean bool = false;
        if (adenine.isAdenineFilled() && uracil.isUracilFilled()) {
            double distance1 = adenine.getN1().distance(uracil.getH3());
            double distance2 = adenine.getH62().distance(uracil.getO4());
            double angle = uracil.getH3().angle(uracil.getN3(), adenine.getN1());


            //Check if distances fit
            if (distance1 <= HbondConstants.MAXIMAL_DISTANCE_AU) {
                if (distance2 <= HbondConstants.MAXIMAL_DISTANCE_AU) {
                    if (angle >= HbondConstants.MINIMAL_ANGLE_AU) {
                        bool = true;
                        System.out.println("Hbond between " + adenine.getResdieNumber() + " and " + uracil.getResdieNumber() + "\t Angle1: " + angle);
                        addBondsAndAtomsAU(adenine, uracil);
                    }
                }
            }
        }
        return bool;
    }

    /*
    Check for possible C-G H bond
     */
    private  boolean isCytosineGuanineBond (HBondBuilder cytosine, HBondBuilder guanine){
        boolean bool = false;
        if (cytosine.isCytosineFilled() && guanine.isGuanineFilled()) {
            double distance1 = guanine.getO6().distance(cytosine.getH41());
            double distance2 = guanine.getH1().distance(cytosine.getN3());
            double distance3 = guanine.getH21().distance(cytosine.getO2());
            double angle = guanine.getH1().angle(guanine.getN1(), cytosine.getN3());

            //Check all requirements for H bonds
            if (distance1 <= HbondConstants.MAXIMAL_DISTANCE) {
                if (distance2 <= HbondConstants.MAXIMAL_DISTANCE) {
                    if (distance3 <= HbondConstants.MAXIMAL_DISTANCE) {
                        if (angle >= HbondConstants.MINIMAL_ANGLE) {
                            bool = true;
                            System.out.println("Hbond between " + cytosine.getResdieNumber() + " and " + guanine.getResdieNumber() + " Angle: \t" + angle);
                            addBondsAndAtomsGC(guanine, cytosine);
                        }
                    }
                }
            }
        }
        return bool;
    }
    /*
    Add bonds and atoms for GC hydrogen bonds
     */
    private void addBondsAndAtomsGC(HBondBuilder guanine, HBondBuilder cytosine){
        //Make hydrogen bond cylinder
        hbonds.add(makeHydrogenBond(guanine.getO6(), cytosine.getH41()));
        hbonds.add(makeHydrogenBond(guanine.getH1(), cytosine.getN3()));
        hbonds.add(makeHydrogenBond(guanine.getH21(), cytosine.getO2()));
        //Add hydrogen building atoms
        bondAtoms.add(Atom.makeAtomSphere(oxygen, guanine.getO6()));
        bondAtoms.add(Atom.makeAtomSphere(hydrogen, cytosine.getH41()));
        bondAtoms.add(Atom.makeAtomSphere(hydrogen, guanine.getH1()));
        bondAtoms.add(Atom.makeAtomSphere(nitrogen, cytosine.getN3()));
        bondAtoms.add(Atom.makeAtomSphere(hydrogen, guanine.getH21()));
        bondAtoms.add(Atom.makeAtomSphere(oxygen, cytosine.getO2()));
        bondAtoms.add(Atom.makeAtomSphere(nitrogen, guanine.getN2()));
        bondAtoms.add(Atom.makeAtomSphere(nitrogen, cytosine.getN4()));
        //Connect atoms with molecule
        bondAtomConnections.add(makeAtomConnection(cytosine.getC4(), cytosine.getN4()));
        bondAtomConnections.add(makeAtomConnection(cytosine.getN4(), cytosine.getH41()));
        bondAtomConnections.add(makeAtomConnection(guanine.getC6(), guanine.getO6()));
        bondAtomConnections.add(makeAtomConnection(guanine.getN1(), guanine.getH1()));
        bondAtomConnections.add(makeAtomConnection(cytosine.getC2(), cytosine.getO2()));
        bondAtomConnections.add(makeAtomConnection(guanine.getC2(), guanine.getN2()));
        bondAtomConnections.add(makeAtomConnection(guanine.getN2(), guanine.getH21()));
    }

    /*
    Add bonds and atoms for AU hydrogen bonds
     */
    private void addBondsAndAtomsAU(HBondBuilder adenine, HBondBuilder uracil){
        //Make hydrogen bond cylinder
        hbonds.add(makeHydrogenBond(adenine.getN1(), uracil.getH3()));
        hbonds.add(makeHydrogenBond(adenine.getH62(), uracil.getO4()));
        //make hydrogen bond building atoms
        bondAtoms.add(Atom.makeAtomSphere(nitrogen, adenine.getN1()));
        bondAtoms.add(Atom.makeAtomSphere(hydrogen, uracil.getH3()));
        bondAtoms.add(Atom.makeAtomSphere(hydrogen, adenine.getH62()));
        bondAtoms.add(Atom.makeAtomSphere(oxygen, uracil.getO4()));
        bondAtomConnections.add(makeAtomConnection(uracil.getC4(), uracil.getO4()));
        bondAtomConnections.add(makeAtomConnection(uracil.getN3(), uracil.getH3()));
        bondAtomConnections.add(makeAtomConnection(adenine.getN6(), adenine.getH62()));
        bondAtomConnections.add(makeAtomConnection(adenine.getC6(), adenine.getN6()));
    }

    //Check if all necessary atoms for cytosine are filled
    private boolean isCytosineFilled(){
        boolean bool = false;
        if (this.H41 != null && this.N3 != null && this.O2 != null) bool = true;
        return bool;
    }
    //Check if guanine atoms are filled
    private boolean isGuanineFilled(){
        boolean bool = false;
        if (this.H21 != null && this.O6 != null && this.H1 != null) bool = true;
        return bool;
    }
    //Check if uracil atoms are filled
    private boolean isUracilFilled(){
        boolean bool = false;
        if (this.H3 != null && this.O4 != null && this.N3 != null) bool = true;
    return bool;
    }
    //Check if uracil atoms are filled
    private boolean isAdenineFilled(){
        boolean bool = false;
        if (this.H62 != null && this.N1 != null) bool = true;
        return bool;
    }

    private boolean isCytosine(){
        boolean bool = false;
        if (this.baseType.equals("C") || this.baseType.equals("CCC") || this.baseType.contains("C")) bool = true;
        return bool;
    }
    private boolean isGuanine(){
        boolean bool = false;
        if (this.baseType.equals("G") || this.baseType.equals("GGG") || this.baseType.contains("G")) bool = true;
        return bool;
    }
    private boolean isAdenine(){
        boolean bool = false;
        if (this.baseType.equals("A") || this.baseType.equals("AAA") || this.baseType.contains("A")) bool = true;
        return bool;
    }
    private boolean isUracil(){
        boolean bool = false;
        if (this.baseType.equals("U") || this.baseType.equals("UUU") || this.baseType.contains("U")) bool = true;
        return bool;
    }

    /**
     * Create hydrogen bonds shape from two points
     */
    private Cylinder makeHydrogenBond(Point3D origin, Point3D target){
        return Line3D.makeLine3D(origin, target, HbondConstants.hBondMaterial, 0.1);
    }
    //Connect two atoms
    private Cylinder makeAtomConnection(Point3D origin, Point3D target){
        return Line3D.makeLine3D(origin, target, new PhongMaterial(Color.DARKCYAN), 0.05);
    }

    /*
    GETTERS AND SETTERS FOR ALL ATOMS
     */
    public Point3D getH1() {
        return H1;
    }

    public void setH1(Point3D h1) {
        H1 = h1;
    }

    public Point3D getO6() {
        return O6;
    }

    public void setO6(Point3D o6) {
        O6 = o6;
    }

    public Point3D getO4() {
        return O4;
    }

    public void setO4(Point3D o4) {
        O4 = o4;
    }

    public Point3D getO2() {
        return O2;
    }

    public void setO2(Point3D o2) {
        O2 = o2;
    }

    public Point3D getN4() {
        return N4;
    }

    public void setN4(Point3D n4) {
        N4 = n4;
    }

    public Point3D getN3() {
        return N3;
    }

    public void setN3(Point3D n3) {
        N3 = n3;
    }

    public Point3D getN2() {
        return N2;
    }

    public void setN2(Point3D n2) {
        N2 = n2;
    }

    public Point3D getN1() {
        return N1;
    }

    public void setN1(Point3D n1) {
        N1 = n1;
    }

    public Point3D getH62() {
        return H62;
    }

    public void setH62(Point3D h62) {
        H62 = h62;
    }

    public Point3D getH4() {
        return H4;
    }

    public void setH4(Point3D h4) {
        H4 = h4;
    }

    public Point3D getH41() {
        return H41;
    }

    public void setH41(Point3D h41) {
        H41 = h41;
    }

    public Point3D getH3() {
        return H3;
    }

    public void setH3(Point3D h3) {
        H3 = h3;
    }

    public Point3D getH21() {
        return H21;
    }

    public void setH21(Point3D h21) {
        H21 = h21;
    }

    public Point3D getN6() {
        return N6;
    }

    public void setN6(Point3D n6) {
        N6 = n6;
    }

    public Point3D getC4() {        return C4;    }

    public void setC4(Point3D c4) {        C4 = c4;    }

    public Point3D getC2() {        return C2;    }

    public void setC2(Point3D c2) {        C2 = c2;    }

    public Point3D getC6() {        return C6;    }

    public void setC6(Point3D c6) {        C6 = c6;    }

    public int getResdieNumber() {
        return resdieNumber;
    }
    public void setResdieNumber(int resdieNumber) {
        this.resdieNumber = resdieNumber;
    }
    public String getBaseType() {
        return baseType;
    }
    public void setBaseType(String baseType) {
        this.baseType = baseType;
    }
    public ArrayList<Sphere> getBondAtoms() {        return bondAtoms;    }
    public ArrayList<Cylinder> getHbonds() {return hbonds;}

    public ArrayList<Cylinder> getBondAtomConnections() {
        return bondAtomConnections;
    }

    public void setBondAtomConnections(ArrayList<Cylinder> bondAtomConnections) {
        this.bondAtomConnections = bondAtomConnections;
    }


}
