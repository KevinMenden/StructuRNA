package HBondInference;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

/**
 * Created by kevin_000 on 27.01.2016.
 */
public class HbondConstants {

    //Maximal distance for hydrogen bonds
    public static final double MAXIMAL_DISTANCE = 3.5;

    //Minimal Angle for N-H-N hydrogen bonds
    public static final double MINIMAL_ANGLE = 140.0;

    public static final double MINIMAL_ANGLE_AU = 140;

    public static final double MAXIMAL_DISTANCE_AU = 3.5;

    //Material for hbonds
    public final static PhongMaterial hBondMaterial = new PhongMaterial(Color.web("#6F1EFA30"));


}
