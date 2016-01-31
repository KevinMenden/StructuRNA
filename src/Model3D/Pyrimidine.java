package Model3D;

import PDBParser.Atom;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

/**
 * Created by kevin_000 on 14.12.2015.
 */
public class Pyrimidine {

    //Information about the nucleotide
    private String nucleotideInfo = null;

    private MoleculeMesh meshView = new MoleculeMesh();

    private float[] coordinates = new float[18];

    private float[] texCoords = new float[] {
            0.0f, 1.0f,
            0.5f, 0.5f,
            0.5f, 1.0f
    };

    private PhongMaterial material = new PhongMaterial();
    //Make faces
    private int[] faces = new int[] {
            5, 0, 3, 1, 4, 2,
            5, 0, 4, 2, 3, 1,
            5, 0, 0, 1, 3, 2,
            5, 0, 3, 2, 0, 1,
            0, 0, 2, 1, 3, 2,
            0, 0, 3, 2, 2, 1,
            0, 0, 1, 1, 2, 2,
            0, 0, 2, 2, 1, 1
    };

    //Empty constructor
    public Pyrimidine(){};
    //Construct with coordinates
    public Pyrimidine(float[] coordinates) {
        this.coordinates = coordinates;
        makeMesh();
    }

    /**
     * Make a TriangleMesh and add it to the meshView
     */
    public void makeMesh(){
        //Set up TriangleMesh
        TriangleMesh triangleMesh = new TriangleMesh();
        triangleMesh.getTexCoords().addAll(texCoords);
        triangleMesh.getPoints().addAll(coordinates);
        triangleMesh.getFaces().addAll(faces);

        this.meshView.setMesh(triangleMesh);
        this.meshView.setOriginalMaterial(material);
    }

    /**
     * Identify atom and fill coordinates accordingly
     * @param atom
     */
    public void fillCoordinates(Atom atom){
        String name = atom.getIdentity();

        switch (name){
            case "N1":
                this.coordinates[0] = atom.getCoordinates()[0];
                this.coordinates[1] = atom.getCoordinates()[1];
                this.coordinates[2] = atom.getCoordinates()[2];
                break;
            case "C2":
                this.coordinates[3] = atom.getCoordinates()[0];
                this.coordinates[4] = atom.getCoordinates()[1];
                this.coordinates[5] = atom.getCoordinates()[2];
                break;
            case "N3":
                this.coordinates[6] = atom.getCoordinates()[0];
                this.coordinates[7] = atom.getCoordinates()[1];
                this.coordinates[8] = atom.getCoordinates()[2];
                break;
            case "C4":
                this.coordinates[9] = atom.getCoordinates()[0];
                this.coordinates[10] = atom.getCoordinates()[1];
                this.coordinates[11] = atom.getCoordinates()[2];
                break;
            case "C5":
                this.coordinates[12] = atom.getCoordinates()[0];
                this.coordinates[13] = atom.getCoordinates()[1];
                this.coordinates[14] = atom.getCoordinates()[2];
                break;
            case "C6":
                this.coordinates[15] = atom.getCoordinates()[0];
                this.coordinates[16] = atom.getCoordinates()[1];
                this.coordinates[17] = atom.getCoordinates()[2];
                break;
            default:
                //System.out.println("Could not identify Atom. > Pyrimidine: fillCoordinates()");
                break;

        }
    }

    //Install Tooltip for this molecule
    public void makeTooltip(String tip) {
        Tooltip tooltip = new Tooltip(tip);
        tooltip.install(this.meshView, tooltip);
    }


    /**
     * GETTER & SETTER
     */

    public String getNucleotideInfo() {
        return nucleotideInfo;
    }

    public void setNucleotideInfo(String nucleotideInfo) {
        this.nucleotideInfo = nucleotideInfo;
    }

    public float[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(float[] coordinates) {
        this.coordinates = coordinates;
    }

    public int[] getFaces() {
        return faces;
    }

    public void setFaces(int[] faces) {
        this.faces = faces;
    }

    public PhongMaterial getMaterial() {
        return material;
    }

    public void setMaterial(PhongMaterial material) {
        this.material = material;
    }

    public float[] getTexCoords() {
        return texCoords;
    }

    public void setTexCoords(float[] texCoords) {
        this.texCoords = texCoords;
    }

    public MoleculeMesh getMeshView() {
        return meshView;
    }
}
