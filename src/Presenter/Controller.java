package Presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;

import java.io.File;

public class Controller {

    /*
    The Presenter instance that holds all models
     */
    private Presenter presenter = new Presenter();

    private Presenter3D presenter3D = new Presenter3D();

    @FXML
    private MenuItem colorNucleotide;

    @FXML
    private TextArea console;

    @FXML
    private MenuItem colorBasetype;

    @FXML
    private StackPane structurePane;

    @FXML
    private Menu helpMenu;

    @FXML
    private MenuItem aboutButton;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu editMenu;

    @FXML
    private MenuItem openFileMenu;

    @FXML
    private VBox root;


    @FXML
    private MenuItem close;

    @FXML
    private TextField sequenceField;

    @FXML
    private Menu fileMenu;

    @FXML
    private Group structureGroup;

    @FXML
    private Pane secondaryStructure;

    /**
     * Open a PDB file, make the 3D structure and the 2D structure
     * @param event
     */
    @FXML
    void openFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());
        if (selectedFile.getAbsolutePath().endsWith(".pdb")) {
            this.presenter.loadFile(selectedFile.getAbsolutePath());
            console.setText(console.getText() + "\n> " + selectedFile.getAbsolutePath() + " loaded succesfully!");
            //Set the Atom[] array in the Presenter3D Class
            presenter3D.setAtoms(presenter.pdbFile.getAtoms());
            presenter3D.makeMolecules();
            structureGroup.getChildren().clear();
            structureGroup.getChildren().addAll(presenter3D.structureGroup);
        } else {
            console.setText(console.getText() +"\n> " + "Selected file is not a .pdb file!");
        }
    }



    @FXML
    void exitProgram(ActionEvent event) {
        System.exit(0);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

        //Add Transforms to camera
        structureGroup.getChildren().clear();

        //Testing Graph
        secondaryStructure.getChildren().add(presenter.graphGroup);
        presenter.buildSecondaryStructureGraph("...((...))......((...))......((...))......((...))...");

        Cylinder cylinder = new Cylinder(10, 200);
        structureGroup.getChildren().addAll(cylinder);

    }


}
