package Selection;

import Model2D.Node;
import Model3D.MoleculeMesh;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.event.Event;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
    //Index list of selected items
    private ArrayList<Integer> selectedItems;

    //Objects that are observed
    MoleculeMesh[] nucleotides;
    Node[] nodes;
    private Pane structurePane;
    private Pane secondaryPane;
    private TextField sequenceField;

    //Position of anchor and caret in the textfield
    private int caretPosition = 0;
    private int anchorPosition = 0;


    private static final PhongMaterial selectionMaterial = new PhongMaterial(Color.FIREBRICK);

    //Make instance of object with panes and textfield
    //init bindings for textfield
    public SelectionControl(TextField textField, Pane pane, Pane anchorPane){
        this.sequenceField = textField;
        this.secondaryPane = pane;
        this.structurePane = anchorPane;

        /*
        this.sequenceField.selectedTextProperty().addListener(observable -> {
            this.anchorPosition = this.sequenceField.getAnchor();
            this.caretPosition = this.sequenceField.getCaretPosition();
        });
        */


    }

    /**
     * Initialize the selection of Nucleotides and Nodes
     * @param nucleotides
     * @param nodes
     */
    public void initSelectionModel(MoleculeMesh[] nucleotides, Node[] nodes){
        SelectionModel<MoleculeMesh> model1 = new SelectionModel<>(nucleotides);
        SelectionModel<Node> model2 = new SelectionModel<>(nodes);
        this.nucleotides = nucleotides;
        this.nodes = nodes;

        for (int i = 0; i < nucleotides.length; i++){
            final int index = i;
            nucleotides[i].setOnMouseClicked(event -> {
                handleClickedEvent(event, model1, model2, index);
            });
            nodes[i].setOnMouseClicked(event -> {
                handleClickedEvent(event, model1, model2, index);
            });
        }
    }

    /*
    Things get selected when they are clicked
     */
    private void handleClickedEvent(MouseEvent e, SelectionModel model1, SelectionModel model2, int index){
        /*if(!e.isShiftDown()){
            model1.clearSelection();
            model2.clearSelection();
            nucleotides[index].switchOff();
            nodes[index].switchOff();
            //this.sequenceField.deselect();
        }*/
        if (model1.isSelected(index) || model2.isSelected(index)){
            model1.clearSelection();
            model2.clearSelection();
            nucleotides[index].switchOff();
            nodes[index].switchOff();
            this.sequenceField.deselect();
        }
        else {
            model1.select(index);
            model2.select(index);
            nucleotides[index].switchOn();
            nodes[index].switchOn();
            this.anchorPosition = index;
            this.caretPosition = index+1;
            this.sequenceField.selectRange(anchorPosition, caretPosition);
        }
    }


    /*
    GETTER AND SETTER
     */


    public Pane getSecondaryPane() {        return secondaryPane;    }
    public void setSecondaryPane(Pane secondaryPane) {        this.secondaryPane = secondaryPane;    }
    public Pane getStructurePane() {        return structurePane;    }
    public void setStructurePane(Pane structurePane) {        this.structurePane = structurePane;    }

    public TextField getSequenceField() {
        return sequenceField;
    }

    public void setSequenceField(TextField sequenceField) {
        this.sequenceField = sequenceField;
    }

    public static PhongMaterial getSelectionMaterial() {
        return selectionMaterial;
    }

    public ArrayList<Integer> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(ArrayList<Integer> selectedItems) {
        this.selectedItems = selectedItems;
    }
}
