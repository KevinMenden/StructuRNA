package Model3D;

import Model3D.Pyrimidine;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

/**
 * Created by kevin_000 on 14.12.2015.
 */
public class Cytosine extends Pyrimidine {

    private PhongMaterial cytosineMaterial = new PhongMaterial();
    private Color diffuseColor = Color.TURQUOISE;
    private Color specularColor = Color.BLUE;

    public Cytosine(){
        super();
        cytosineMaterial.setDiffuseColor(diffuseColor);
        cytosineMaterial.setSpecularColor(specularColor);
        this.setMaterial(cytosineMaterial);
    }

    public Cytosine(float[] coords){
        super();
        cytosineMaterial.setDiffuseColor(diffuseColor);
        cytosineMaterial.setSpecularColor(specularColor);
        this.setMaterial(cytosineMaterial);
    }
}
