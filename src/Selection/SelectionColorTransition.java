package Selection;

import javafx.animation.Transition;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.util.Duration;

/**
 * Created by kevin_000 on 31.01.2016.
 */
public class SelectionColorTransition extends Transition {

    private PhongMaterial phongMaterial;
    private Color start;
    private Color end;

    public SelectionColorTransition(
            PhongMaterial phongMaterial,
            Color start,
            Color end,
            Duration duration
    ){
        this.phongMaterial = phongMaterial;
        this.start = start;
        this.end = end;
        setCycleDuration(duration);
    }


    @Override
    protected void interpolate(double t) {
        double green = start.getGreen() + t * (end.getGreen() - start.getGreen());
        double red = start.getRed() + t * (end.getRed() - start.getRed());
        double blue = start.getBlue() + t * (end.getBlue() - start.getBlue());
        red   = red < 0 ? 0 : (red > 1 ? 1 : red);
        green = green < 0 ? 0 : (green > 1 ? 1 : green);
        blue  = blue < 0 ? 0 : (blue > 1 ? 1 : blue);
        final Color color = Color.color(red, green, blue);
        phongMaterial.setDiffuseColor(color);
    }

}
