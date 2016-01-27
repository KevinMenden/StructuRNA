package HBondInference;

import PDBParser.Atom;
import javafx.geometry.Point3D;

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

    //Base type and residueNumber with their getters and setter
    String baseType;
    int resdieNumber;
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
            default:break;
        }
    }

    /**
     * For two HBondBuilder objects, check if they have a HBond connection
     * @param builderA
     * @param builderB
     * @return
     */
    public static boolean isHbond(HBondBuilder builderA, HBondBuilder builderB){
        boolean bool = false;

        //Check if possible Watson Crick base pair
        //If yes, check if H bond
        if (builderA.getBaseType().equals("A") && builderB.getBaseType().equals("U")){
            bool = isAdenineUracilBond(builderA, builderB);
        }
        else if (builderA.getBaseType().equals("U") && builderB.getBaseType().equals("A")){
            bool = isAdenineUracilBond(builderB, builderA);
        }
        else if (builderA.getBaseType().equals("C") && builderB.getBaseType().equals("G")){
            bool = isCytosineGuanineBond(builderA, builderB);
        }
        else if (builderA.getBaseType().equals("G") && builderB.getBaseType().equals("C")){
            bool = isCytosineGuanineBond(builderB, builderA);
        }




        return bool;
    }
    /*
    Check for possible A-U H bond (no angles checked, only distances!)
     */
    private static boolean isAdenineUracilBond(HBondBuilder adenine, HBondBuilder uracil){
        boolean bool = false;
        double distance1 = adenine.getN1().distance(uracil.getH3());
        double distance2 = adenine.getH62().distance(uracil.getO4());
        //double angle1 = adenine.getH62().angle(adenine.getN6(), uracil.getO4());
        //double angle2 = uracil.getH3().angle(uracil.getN3(), adenine.getN1());
        double angle = uracil.getH3().angle(uracil.getN3(), adenine.getN1());

        //Check if distances fit
        if (distance1<= HbondConstants.MAXIMAL_DISTANCE){
            if (distance2 <= HbondConstants.MAXIMAL_DISTANCE){
                if (angle >= HbondConstants.MINIMAL_ANGLE) {
                    bool = true;
                    System.out.println("Hbond between " + adenine.getResdieNumber() + " and " + uracil.getResdieNumber() + "\t Angle1: " + angle);
                }
            }
        }

        return bool;
    }

    /*
    Check for possible C-G H bond
     */
    private static boolean isCytosineGuanineBond (HBondBuilder cytosine, HBondBuilder guanine){
        boolean bool = false;
        double distance1 = guanine.getO6().distance(cytosine.getH41());
        double distance2 = guanine.getH1().distance(cytosine.getN3());
        double distance3 = guanine.getH21().distance(cytosine.getO2());
        double angle = guanine.getH1().angle(guanine.getN1(), cytosine.getN3());

        //Check all requirements for H bonds
        if (distance1<= HbondConstants.MAXIMAL_DISTANCE) {
            if (distance2 <= HbondConstants.MAXIMAL_DISTANCE){
                if (distance3 <= HbondConstants.MAXIMAL_DISTANCE) {
                    if (angle >= HbondConstants.MINIMAL_ANGLE) {
                        bool = true;
                        System.out.println("Hbond between " + cytosine.getResdieNumber() + " and " + guanine.getResdieNumber() + " Angle: \t" + angle);
                    }
                }
            }
        }

        return bool;
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
}
