package Selection;

import Model2D.Node;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Created by kevin_000 on 27.01.2016.
 */
public class SelectionControl {

    private ArrayList<Integer> selectedItems;
    private AnchorPane structurePane;
    private Pane secondaryPane;

    public Pane getSecondaryPane() {        return secondaryPane;    }
    public void setSecondaryPane(Pane secondaryPane) {        this.secondaryPane = secondaryPane;    }
    public AnchorPane getStructurePane() {        return structurePane;    }
    public void setStructurePane(AnchorPane structurePane) {        this.structurePane = structurePane;    }

    private static final PhongMaterial selectionMaterial = new PhongMaterial(Color.FIREBRICK);



    //FIRST IMPLEMENTATION OF SELECTION MODEL
    //WILL BE MODIFIED IN TERMS OF BETTER HANDLING


    public static void initSelectionModel3D(MeshView[] nucleotides){
        SelectionModel<MeshView> selectionModel = new SelectionModel<>(nucleotides);
        // setup selection capture in view:
        for (int i = 0; i < nucleotides.length; i++) {
            final int index=i;
            nucleotides[i].setOnMouseClicked((e) -> {
                if(!e.isShiftDown())
                    selectionModel.clearSelection();
                if(selectionModel.isSelected(index)) {
                    selectionModel.clearSelection(index);
                }
                else {
                    selectionModel.select(index);
                    nucleotides[index].setMaterial(selectionMaterial);;
                }
            });
        }
    }


    private void makeFillTransition(Node node){
        FillTransition fillTransition = new FillTransition(Duration.millis(1000),node , Color.CYAN, Color.BROWN);
        fillTransition.setCycleCount(Animation.INDEFINITE);
        fillTransition.setAutoReverse(true);
        fillTransition.play();
    }

    public void initSelectionModel2D(Node[] nodes){
        SelectionModel<Node> selectionModel = new SelectionModel<>(nodes);
        // setup selection capture in view:
        for (int i = 0; i < nodes.length; i++) {
            final int index=i;
            secondaryPane.getChildren().add(nodes[i].getSelectionBox());
            nodes[i].setOnMouseClicked((e) -> {
                if(!e.isShiftDown()) {
                    selectionModel.clearSelection();
                    nodes[index].showSelectionBox(false);
                }
                if(selectionModel.isSelected(index)) {
                    selectionModel.clearSelection(index);
                    nodes[index].showSelectionBox(false);
                }
                else {
                    makeFillTransition(nodes[index]);
                    selectionModel.select(index);
                    nodes[index].showSelectionBox(true);
                }
            });
        }
    }
}
