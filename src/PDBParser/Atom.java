package PDBParser;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

/**
 * Created by kevin_000 on 14.12.2015.
 */
public class Atom {

    //Number
    private int number = 0;
    //Residue number
    private int residueNumber = 0;
    //Coordinates
    private float[] coordinates = {0f, 0f, 0f,};
    //Molecule of the atom
    private String base = "";
    //Element
    private String element = "";
    //Identity of atom
    private String identity ="";
    //Point3D of atom
    private Point3D point;

    public Atom(){
    }

    //Create Point3D from coordinates
    public void make3DPoint(){
        this.point = new Point3D(this.coordinates[0], this.coordinates[1], this.coordinates[2]);
    }


    public String getElement() {
        return element;
    }

    /*
    GETTER & SETTER
     */

    public int getResidueNumber() {
        return residueNumber;
    }

    public void setResidueNumber(int residueNumber) {
        this.residueNumber = residueNumber;
    }

    public Point3D getPoint() {
        return point;
    }

    public void setPoint(Point3D point) {
        this.point = point;
        //Update the coordinates array
        this.coordinates[0] = Float.parseFloat(Double.toString(point.getX()));
        this.coordinates[1] = Float.parseFloat(Double.toString(point.getY()));
        this.coordinates[2] = Float.parseFloat(Double.toString(point.getZ()));
    }

    /**
     * Return a sphere according to atom type
     * @return Sphere
     */
    public Sphere getAtomSphere(){
        Sphere atomSphere = new Sphere(0.12);
        atomSphere.setTranslateX(this.coordinates[0]);
        atomSphere.setTranslateY(this.coordinates[1]);
        atomSphere.setTranslateZ(this.coordinates[2]);
        PhongMaterial atomMaterial = new PhongMaterial();
        atomSphere.setMaterial(atomMaterial);
        switch (this.element){
            case "H":
            atomMaterial.setDiffuseColor(Color.WHITESMOKE); break;
            case "O":
                atomMaterial.setDiffuseColor(Color.RED); break;
            case "N":
                atomMaterial.setDiffuseColor(Color.BLUE); break;
            case "P":
                atomMaterial.setDiffuseColor(Color.BLACK); break;
            case "C":
                atomMaterial.setDiffuseColor(Color.GRAY); break;
            default:break;
        }

        return atomSphere;
    }

    /*
    Can be used to construct atom represations when given a
    - Point3D
    - element identifier
     */
    public static Sphere makeAtomSphere(String element, Point3D point3D){
        Sphere atomSphere = new Sphere(0.12);
        PhongMaterial atomMaterial = new PhongMaterial(Color.BLACK);
        atomSphere.setMaterial(atomMaterial);
        atomSphere.setTranslateX(point3D.getX());
        atomSphere.setTranslateY(point3D.getY());
        atomSphere.setTranslateZ(point3D.getZ());
        switch (element){
            case "H":
                atomMaterial.setDiffuseColor(Color.WHITESMOKE); break;
            case "O":
                atomMaterial.setDiffuseColor(Color.RED); break;
            case "N":
                atomMaterial.setDiffuseColor(Color.BLUE); break;
            case "P":
                atomMaterial.setDiffuseColor(Color.BLACK); break;
            case "C":
                atomMaterial.setDiffuseColor(Color.GRAY); break;
            default:break;
        }
        return atomSphere;
    }

    /**
     * Check if this atom is part of the displayed structure
     * @return boolean
     */
    public boolean isPartOfStructure(){
        boolean bool = false;
        switch (identity){
            case "N1": bool = true; break;
            case "C2": bool = true; break;
            case "N3": bool = true; break;
            case "C4": bool = true; break;
            case "C5": bool = true; break;
            case "C6": bool = true; break;
            case "N7": bool = true; break;
            case "C8": bool = true; break;
            case "N9": bool = true; break;
            case "C1'": bool = true; break;
            case "C2'": bool = true; break;
            case "C3'": bool = true; break;
            case "C4'": bool = true; break;
            case "O4'": bool = true; break;
        }

        return bool;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public float[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(float[] coordinates) {
        this.coordinates = coordinates;
        make3DPoint();
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

}
