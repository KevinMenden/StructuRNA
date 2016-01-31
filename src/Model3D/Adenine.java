package Model3D;

import PDBParser.Atom;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

/**
 * Created by kevin_000 on 14.12.2015.
 */
public class Adenine extends Purine {


    private Atom O5prime;
    private Atom C5prime;
    private Atom C4prime;
    private Atom C3prime;
    private Atom O3prime;
    private Atom C2prime;
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


    //Material
    private PhongMaterial adenineMaterial = new PhongMaterial();
    private Color diffuseColor = Color.RED;
    private Color specularColor = Color.DARKRED;

    public Adenine(){
        super();
        adenineMaterial.setDiffuseColor(diffuseColor);
        adenineMaterial.setSpecularColor(specularColor);
        this.setMaterial(adenineMaterial);
    }

    public Adenine(float[] coords){
        super();
        adenineMaterial.setDiffuseColor(diffuseColor);
        adenineMaterial.setSpecularColor(specularColor);
        this.setMaterial(adenineMaterial);
    }
}
