package Model3D;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.util.Duration;

/**
 * Kevin Menden
 *
 * Extension of the MeshView class for Molecules
 * Contains additional features needed for handling the molecules
 */
public class MoleculeMesh extends MeshView {

    Timeline timeline;

    //Original material of the Molecule
    private PhongMaterial originalMaterial;
    private PhongMaterial startMaterial = new PhongMaterial(Color.CYAN);

    //Set the original material of the molecule (if changed through transitions)
    public void setOriginalMaterial(PhongMaterial originalMaterial){
        this.originalMaterial = originalMaterial;
        this.setMaterial(originalMaterial);

        //Set up timeline
        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500),
                new KeyValue(this.scaleXProperty(), 1.5),
                new KeyValue(this.scaleYProperty(), 1.5)));
    }

    //Set the material back to the original material
    public void setMaterialToOriginal(){
        this.setMaterial(this.originalMaterial);
    }

    public void switchOn(){
        this.setMaterial(startMaterial);
        this.timeline.play();

    }
    public void switchOff(){
        this.setMaterial(originalMaterial);
        this.timeline.stop();
        this.setScaleY(1);
        this.setScaleX(1);
    }

}
