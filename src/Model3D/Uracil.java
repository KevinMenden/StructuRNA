package Model3D;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

/**
 * Created by kevin_000 on 14.12.2015.
 */
public class Uracil extends Pyrimidine {

    private PhongMaterial uracilMaterial = new PhongMaterial();
    private Color diffuseColor = Color.GREEN;
    private Color specularColor = Color.DARKGREEN;

    public Uracil(){
        super();
        uracilMaterial.setDiffuseColor(diffuseColor);
        uracilMaterial.setSpecularColor(specularColor);
        this.setMaterial(uracilMaterial);
    }

    public Uracil(float[] coords){
        super();
        uracilMaterial.setDiffuseColor(diffuseColor);
        uracilMaterial.setSpecularColor(specularColor);
        this.setMaterial(uracilMaterial);
    }
}
