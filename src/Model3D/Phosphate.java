package Model3D;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

/**
 * Created by kevin_000 on 15.12.2015.
 */
public class Phosphate extends Sphere {

    private final double PHOSPHATE_RADIUS = 1;
    private PhongMaterial phosphateMaterial = new PhongMaterial();
    private Color diffuseColor = Color.ROSYBROWN;
    private Color specularColor = Color.LIGHTCYAN;

    public Phosphate(){
        super();
        this.setRadius(PHOSPHATE_RADIUS);
        this.phosphateMaterial.setDiffuseColor(diffuseColor);
        this.phosphateMaterial.setSpecularColor(specularColor);
        this.setMaterial(phosphateMaterial);
    }
}
