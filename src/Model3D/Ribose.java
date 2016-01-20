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
public class Ribose {


    private MeshView meshView = new MeshView();

    private float[] coordinates = new float[15];

    private float[] texCoords = new float[] {
            0.0f, 1.0f,
            0.5f, 0.5f,
            0.5f, 1.0f
    };

    private PhongMaterial material = new PhongMaterial();

    //Make faces
    private int[] faces = new int[] {
            3, 0, 2, 1, 4, 2,
            3, 0, 4, 2, 2, 1,
            2, 0, 1, 1, 4, 2,
            2, 0, 4, 2, 1, 1,
            1, 0, 0, 1, 4, 2,
            1, 0, 4, 2, 0, 1
    };

    //Make smoothing groups
    private final int SMOOTHING_GROUP = 5;
    private int[] smoothing = new int[faces.length];

    //Empty constructor
    public Ribose(){
        material.setDiffuseColor(Color.BROWN);
        material.setSpecularColor(Color.ROSYBROWN);
    };

    //Construct with coordinates
    public Ribose(float[] coordinates) {
        this.coordinates = coordinates;
        material.setDiffuseColor(Color.BROWN);
        material.setSpecularColor(Color.ROSYBROWN);
        this.meshView.setMaterial(material);
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
        //fillSmoothingGroup();
        //triangleMesh.getFaceSmoothingGroups().addAll(smoothing);
        //System.out.println(triangleMesh.getFaceSmoothingGroups().size());
        //System.out.println(triangleMesh.getFaceSmoothingGroups());

        this.meshView.setMesh(triangleMesh);
        this.meshView.setMaterial(material);
    }

    /**
     * Identify atom and fill coordinates accordingly
     * @param atom
     */
    public void fillCoordinates(Atom atom){
        String name = atom.getIdentity();

        switch (name) {
            case "C1'":
                this.coordinates[0] = atom.getCoordinates()[0];
                this.coordinates[1] = atom.getCoordinates()[1];
                this.coordinates[2] = atom.getCoordinates()[2];
                break;
            case "C2'":
                this.coordinates[3] = atom.getCoordinates()[0];
                this.coordinates[4] = atom.getCoordinates()[1];
                this.coordinates[5] = atom.getCoordinates()[2];
                break;
            case "C3'":
                this.coordinates[6] = atom.getCoordinates()[0];
                this.coordinates[7] = atom.getCoordinates()[1];
                this.coordinates[8] = atom.getCoordinates()[2];
                break;
            case "C4'":
                this.coordinates[9] = atom.getCoordinates()[0];
                this.coordinates[10] = atom.getCoordinates()[1];
                this.coordinates[11] = atom.getCoordinates()[2];
                break;
            case "O4'":
                this.coordinates[12] = atom.getCoordinates()[0];
                this.coordinates[13] = atom.getCoordinates()[1];
                this.coordinates[14] = atom.getCoordinates()[2];
                break;
            default:
                //System.out.println("Could not identify atom. >Ribose: fillCoordinates()");
                break;
        }

    }

    //Make smoothing group array
    private void fillSmoothingGroup(){
        for (int i = 0; i < smoothing.length; i++){
            smoothing[i] = SMOOTHING_GROUP;
        }
    }

    //Install Tooltip for this Ribose molecule
    public void makeTooltip(String tip) {
        Tooltip tooltip = new Tooltip(tip);
        tooltip.install(this.meshView, tooltip);
    }

    /**
     * GETTER & SETTER
     */
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
