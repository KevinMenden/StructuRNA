package Model3D;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

/**
 * Created by kevin_000 on 14.12.2015.
 */
public class Adenine extends Purine {

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
