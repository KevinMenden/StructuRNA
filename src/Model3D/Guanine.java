package Model3D;

import Model3D.Purine;
import PDBParser.Atom;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;

import java.util.ArrayList;

/**
 * Created by kevin_000 on 14.12.2015.
 */
public class Guanine extends Purine {

    //Atom
    private Atom O5prime;
    private Atom C5prime;
    private Atom C4prime;
    private Atom O4prime;
    private Atom C3prime;
    private Atom O3prime;
    private Atom C2prime;
    private Atom O2prime;
    private Atom C1prime;
    private Atom N9;
    private Atom C8;
    private Atom N7;
    private Atom C5;
    private Atom C6;
    private Atom O6;
    private Atom N1;
    private Atom C2;
    private Atom N2;
    private Atom N3;
    private Atom C4;
    private Atom H5prime;
    private Atom H5prime2;
    private Atom H4prime;
    private Atom H3prime;
    private Atom H2prime;
    private Atom HO2prime;
    private Atom H1prime;
    private Atom H8;
    private Atom H1;
    private Atom H21;
    private Atom H22;
    private Atom HO5prime;

    ArrayList<Sphere> atomList = new ArrayList<>();
    ArrayList<Cylinder> atomConnections = new ArrayList<>();


    private PhongMaterial guanineMaterial = new PhongMaterial();
    private Color diffuseColor = Color.YELLOW;
    private Color specularColor = Color.YELLOW;

    public Guanine(){
        super();
        guanineMaterial.setDiffuseColor(diffuseColor);
        guanineMaterial.setSpecularColor(specularColor);
        this.setMaterial(guanineMaterial);
    }

    public Guanine(float[] coords){
        super();
        guanineMaterial.setDiffuseColor(diffuseColor);
        guanineMaterial.setSpecularColor(specularColor);
        this.setMaterial(guanineMaterial);
    }

    /**
     * Set the right atom of this molecule
     * @param atom
     */
    public void setAtom(Atom atom){
        switch (atom.getIdentity()){
            case "O5'": this.O5prime = atom; atomList.add(atom.getAtomSphere()); break;
            case "C5'": this.C5prime = atom; atomList.add(atom.getAtomSphere()); break;
            case "C4'": this.C4prime = atom;  atomList.add(atom.getAtomSphere()); break;
            case "O4'": this.O4prime = atom;  atomList.add(atom.getAtomSphere()); break;
            case "C3'": this.C3prime = atom;  atomList.add(atom.getAtomSphere()); break;
            case "O3'": this.O3prime = atom;  atomList.add(atom.getAtomSphere()); break;
            case "C2'": this.C2prime = atom;  atomList.add(atom.getAtomSphere()); break;
            case "O2'": this.O2prime = atom;  atomList.add(atom.getAtomSphere()); break;
            case "C1'": this.C1prime = atom;  atomList.add(atom.getAtomSphere()); break;
            case "N9": this.N9 = atom;  atomList.add(atom.getAtomSphere()); break;
            case "C8": this.C8 = atom;  atomList.add(atom.getAtomSphere()); break;
            case "N7": this.N7 = atom;  atomList.add(atom.getAtomSphere()); break;
            case "C5": this.C5 = atom;  atomList.add(atom.getAtomSphere()); break;
            case "C6": this.C6 = atom;  atomList.add(atom.getAtomSphere()); break;
            case "O6": this.O6 = atom;  atomList.add(atom.getAtomSphere()); break;
            case "N1": this.N1 = atom;  atomList.add(atom.getAtomSphere()); break;
            case "C2": this.C2 = atom;  atomList.add(atom.getAtomSphere()); break;
            case "N2": this.N2 = atom;  atomList.add(atom.getAtomSphere()); break;
            case "N3": this.N3 = atom;  atomList.add(atom.getAtomSphere()); break;
            case "C4": this.C4 = atom;  atomList.add(atom.getAtomSphere()); break;
            case "H5'": this.H5prime = atom;  atomList.add(atom.getAtomSphere()); break;
            case "H5''": this.H5prime2 = atom;  atomList.add(atom.getAtomSphere()); break;
            case "H4'": this.H4prime = atom;  atomList.add(atom.getAtomSphere()); break;
            case "H3'": this.H3prime = atom;  atomList.add(atom.getAtomSphere()); break;
            case "H2'": this.H2prime = atom;  atomList.add(atom.getAtomSphere()); break;
            case "HO2'": this.HO2prime = atom;  atomList.add(atom.getAtomSphere()); break;
            case "H1'": this.H1prime = atom;  atomList.add(atom.getAtomSphere()); break;
            case "H8": this.H8 = atom;  atomList.add(atom.getAtomSphere()); break;
            case "H1": this.H1 = atom;  atomList.add(atom.getAtomSphere()); break;
            case "H21": this.H21 = atom;  atomList.add(atom.getAtomSphere()); break;
            case "H22": this.H22 = atom;  atomList.add(atom.getAtomSphere()); break;
            case "HO5'": this.HO5prime = atom;  atomList.add(atom.getAtomSphere()); break;
            default: break;
        }
    }

    public void makeAtomConnections(){
        atomConnections.add(connectAtoms(H21.getPoint(), N2.getPoint()));
        atomConnections.add(connectAtoms(H22.getPoint(), N2.getPoint()));
        atomConnections.add(connectAtoms(C2.getPoint(), N2.getPoint()));
        atomConnections.add(connectAtoms(C2.getPoint(), N3.getPoint()));
        atomConnections.add(connectAtoms(C2.getPoint(), N1.getPoint()));
        atomConnections.add(connectAtoms(N3.getPoint(), C4.getPoint()));
        atomConnections.add(connectAtoms(C4.getPoint(), N9.getPoint()));
        atomConnections.add(connectAtoms(C4.getPoint(), C5.getPoint()));
        atomConnections.add(connectAtoms(N9.getPoint(), C8.getPoint()));
        atomConnections.add(connectAtoms(C8.getPoint(), H8.getPoint()));
        atomConnections.add(connectAtoms(C8.getPoint(), N7.getPoint()));
        atomConnections.add(connectAtoms(N7.getPoint(), C5.getPoint()));
        atomConnections.add(connectAtoms(C5.getPoint(), C6.getPoint()));
        atomConnections.add(connectAtoms(C6.getPoint(), O6.getPoint()));
        atomConnections.add(connectAtoms(C6.getPoint(), N1.getPoint()));
        atomConnections.add(connectAtoms(N1.getPoint(), H1.getPoint()));
    }

    private Cylinder connectAtoms(Point3D origin, Point3D target){
        return Line3D.makeLine3D(origin, target, new PhongMaterial(Color.BLACK), 0.1);
    }




    /*
    GETTER AND SETTER
     */
    public Atom getC1prime() {        return C1prime;    }
    public void setC1prime(Atom c1prime) {        C1prime = c1prime;    }
    public Atom getC2() {        return C2;    }
    public void setC2(Atom c2) {        C2 = c2;    }
    public Atom getC2prime() {        return C2prime;    }
    public void setC2prime(Atom c2prime) {        C2prime = c2prime;    }
    public Atom getC3prime() {        return C3prime;    }
    public void setC3prime(Atom c3prime) {        C3prime = c3prime;    }
    public Atom getC4() {        return C4;    }
    public void setC4(Atom c4) {        C4 = c4;    }
    public Atom getC4prime() {        return C4prime;    }
    public void setC4prime(Atom c4prime) {        C4prime = c4prime;    }
    public Atom getC5() {        return C5;    }
    public void setC5(Atom c5) {        C5 = c5;    }
    public Atom getC5prime() {        return C5prime;    }
    public void setC5prime(Atom c5prime) {        C5prime = c5prime;    }
    public Atom getC6() {        return C6;    }
    public void setC6(Atom c6) {        C6 = c6;    }
    public Atom getC8() {        return C8;    }
    public void setC8(Atom c8) {        C8 = c8;    }
    public Color getDiffuseColor() {        return diffuseColor;    }
    public void setDiffuseColor(Color diffuseColor) {        this.diffuseColor = diffuseColor;
    }
    public PhongMaterial getGuanineMaterial() {
        return guanineMaterial;    }
    public void setGuanineMaterial(PhongMaterial guanineMaterial) {
        this.guanineMaterial = guanineMaterial;    }
    public Atom getH1() {        return H1;    }
    public void setH1(Atom h1) {        H1 = h1;    }
    public Atom getH1prime() {        return H1prime;    }
    public void setH1prime(Atom h1prime) {        H1prime = h1prime;    }
    public Atom getH21() {        return H21;    }
    public void setH21(Atom h21) {        H21 = h21;    }
    public Atom getH22() {        return H22;    }
    public void setH22(Atom h22) {        H22 = h22;    }
    public Atom getH2prime() {        return H2prime;    }
    public void setH2prime(Atom h2prime) {        H2prime = h2prime;    }
    public Atom getH3prime() {        return H3prime;    }
    public void setH3prime(Atom h3prime) {        H3prime = h3prime;    }
    public Atom getH4prime() {        return H4prime;    }
    public void setH4prime(Atom h4prime) {        H4prime = h4prime;    }
    public Atom getH5prime2() {        return H5prime2;    }
    public void setH5prime2(Atom h5prime2) {        H5prime2 = h5prime2;    }
    public Atom getH5prime() {        return H5prime;    }
    public void setH5prime(Atom h5prime) {        H5prime = h5prime;
    }

    public Atom getH8() {
        return H8;
    }

    public void setH8(Atom h8) {
        H8 = h8;
    }

    public Atom getHO2prime() {
        return HO2prime;
    }

    public void setHO2prime(Atom HO2prime) {
        this.HO2prime = HO2prime;
    }

    public Atom getHO5prime() {
        return HO5prime;
    }

    public void setHO5prime(Atom HO5prime) {
        this.HO5prime = HO5prime;
    }

    public Atom getN1() {
        return N1;
    }

    public void setN1(Atom n1) {
        N1 = n1;
    }

    public Atom getN2() {
        return N2;
    }

    public void setN2(Atom n2) {
        N2 = n2;
    }

    public Atom getN3() {
        return N3;
    }

    public void setN3(Atom n3) {
        N3 = n3;
    }

    public Atom getN7() {
        return N7;
    }

    public void setN7(Atom n7) {
        N7 = n7;
    }

    public Atom getN9() {
        return N9;
    }

    public void setN9(Atom n9) {
        N9 = n9;
    }

    public Atom getO3prime() {
        return O3prime;
    }

    public void setO3prime(Atom o3prime) {
        O3prime = o3prime;
    }

    public Atom getO5prime() {
        return O5prime;
    }

    public void setO5prime(Atom o5prime) {
        O5prime = o5prime;
    }

    public Atom getO6() {
        return O6;
    }

    public void setO6(Atom o6) {
        O6 = o6;
    }

    public Color getSpecularColor() {
        return specularColor;
    }

    public void setSpecularColor(Color specularColor) {
        this.specularColor = specularColor;
    }

    public Atom getO4prime() {
        return O4prime;
    }

    public void setO4prime(Atom o4prime) {
        O4prime = o4prime;
    }

    public Atom getO2prime() {
        return O2prime;
    }

    public void setO2prime(Atom o2prime) {
        O2prime = o2prime;
    }

    public ArrayList<Cylinder> getAtomConnections() {
        return atomConnections;
    }

    public ArrayList<Sphere> getAtomList() {
        return atomList;
    }
}
