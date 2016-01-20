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
public class Purine {

    //Information about the nucleotide
    private String nucleotideInfo = null;

    private MeshView meshView = new MeshView();

    private float[] coordinates = new float[27];

    private float[] texCoords = new float[] {
            0.0f, 1.0f,
            0.5f, 0.5f,
            0.5f, 1.0f
    };

    private PhongMaterial material = new PhongMaterial();

    private int[] faces = new int[] {
            7,0,4,1,6,2,
            7,0,6,2,4,1,
            7,0,8,1,4,2,
            7,0,4,2,8,1,
            8,0,3,1,4,2,
            8,0,4,2,3,1,
            4,0,0,1,5,2,
            4,0,5,2,0,1,
            4,0,3,1,0,2,
            4,0,0,2,3,1,
            3,0,2,1,0,2,
            3,0,0,2,2,1,
            2,0,1,1,0,2,
            2,0,0,2,1,1
    };

    //Empty constructor
    public Purine(){};

    //Construct with coordinates
    public Purine(float[] coordinates) {
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
        this.meshView.setMaterial(material);
        Tooltip tooltip = new Tooltip(this.getNucleotideInfo());
        tooltip.install(this.meshView, tooltip);
    }

    /**
     * Identify atom and fill coordinates accordingly
     * @param atom
     */
    public void fillCoordinates(Atom atom){
        String name = atom.getIdentity();
        //If no information about nucleotide saved, add it now
        if (this.nucleotideInfo == null) {
            this.nucleotideInfo = atom.getBase() + atom.getResidueNumber();
        }

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
            case "N7":
                this.coordinates[18] = atom.getCoordinates()[0];
                this.coordinates[19] = atom.getCoordinates()[1];
                this.coordinates[20] = atom.getCoordinates()[2];
                break;
            case "C8":
                this.coordinates[21] = atom.getCoordinates()[0];
                this.coordinates[22] = atom.getCoordinates()[1];
                this.coordinates[23] = atom.getCoordinates()[2];
                break;
            case "N9":
                this.coordinates[24] = atom.getCoordinates()[0];
                this.coordinates[25] = atom.getCoordinates()[1];
                this.coordinates[26] = atom.getCoordinates()[2];
                break;
            default:
                //System.out.println("Could not identify atom. >Purine: fillCoordinates()");
                break;
        }
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

    public MeshView getMeshView() {
        return meshView;
    }
}
