package PDBParser;

import javafx.geometry.Point3D;

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
