package Model3D;

import Model3D.Purine;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

/**
 * Created by kevin_000 on 14.12.2015.
 */
public class Guanine extends Purine {

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
}
