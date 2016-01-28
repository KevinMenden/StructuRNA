package HBondInference;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

/**
 * Created by kevin_000 on 27.01.2016.
 */
public class HbondConstants {

    //Maximal distance for hydrogen bonds
    public static final double MAXIMAL_DISTANCE = 4.0;

    //Minimal Angle for N-H-N hydrogen bonds
    public static final double MINIMAL_ANGLE = 110.0;

    //Material for hbonds
    public final static PhongMaterial hBondMaterial = new PhongMaterial(Color.RED);


}
