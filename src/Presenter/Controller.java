package Presenter;

import Selection.SelectionControl;
import javafx.beans.binding.DoubleBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

public class Controller {

    /*
    The Presenter instances that hold all methods and models
     */

    private Presenter presenter;// = new Presenter();
    private Presenter2D presenter2D;
    private Presenter3D presenter3D;// = new Presenter3D();

    @FXML
    private MenuItem colorNucleotide;

    @FXML
    private TextArea console;

    @FXML
    private MenuItem colorBasetype;

    //@FXML
    //private StackPane structurePane;

    @FXML
    private Pane structurePane;

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
    private SplitPane vertSplitPane;

    @FXML
    private MenuItem close;

    @FXML
    private TextField sequenceField;

    @FXML
    private Menu fileMenu;

    @FXML
    private Pane secondaryStructure;

    @FXML
    private MenuItem centerButton;

    /**
     * Open a PDB file, make the 3D structure and the 2D structure
     * @param event
     */
    @FXML
    void openFile(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        try {
            File selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());
            //If file is .pdb file, load it
            if (selectedFile.getAbsolutePath().endsWith(".pdb")) {
                this.presenter.loadFile(selectedFile.getAbsolutePath());
                //Set the sequence to te textfield
                sequenceField.setText(presenter.getPdbFile().getSequence());
                presenter3D.setSequenceLength(presenter.getSequence().length());

                //SelectionControl instance, handles selection events
                SelectionControl selectionControl = new SelectionControl(sequenceField, secondaryStructure, structurePane);
                //Center atoms, make 3D structure
                presenter3D.centerStructure();
                presenter.setUp3DStructure();

                //Make the secondary structure
                presenter.setUp2DStructure();

                //With all objects produced, initialize SelectionModels
                selectionControl.initSelectionModel(presenter3D.getNucleotides(), presenter2D.getNodes());
                console.setText(console.getText() + "\n> " + selectedFile.getAbsolutePath() + " loaded succesfully!");

            } else {
                console.setText(console.getText() + "\n> " + "Selected file is not a .pdb file!");
            }

        }catch (NullPointerException np){
            console.setText(console.getText() + "\n> " + "No file selected.");
            np.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
            console.setText(console.getText() + "\n> Exception caught during file loading." );
        }
    }

    //Center the structure
    @FXML
    void center(ActionEvent event) {
        presenter3D.centerStructure();
    }

    //Color each nucleotide
    @FXML
    void colorByNucleotide(ActionEvent event) {
        presenter3D.colorByNucleotide();
        structurePane.getChildren().clear();
        structurePane.getChildren().addAll(presenter3D.subScene);
    }

    //Color by basetype
    @FXML
    void colorByBasetype(ActionEvent event) {
        presenter3D.colorByBasetype();
        structurePane.getChildren().clear();
        structurePane.getChildren().addAll(presenter3D.subScene);
    }

    //Delete the structure
    @FXML
    void clear(ActionEvent event) {
        structurePane.getChildren().clear();
        secondaryStructure.getChildren().clear();
        console.setText(console.getText() + "\n> Structure deleted." );

    }




    @FXML
    void exitProgram(ActionEvent event) {
        System.exit(0);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        //Make Presenter2D instance
        presenter2D = new Presenter2D();

        //Make Presenter3D instance, set up action events for handling
        //the 3D structure
        presenter3D = new Presenter3D();
        presenter3D.setStructurePane(structurePane);
        //Make Presenter instance
        presenter = new Presenter();
        presenter.setSecondaryStructurePane(secondaryStructure);
        presenter.setPresenter2D(presenter2D);
        presenter.setStructurePane(structurePane);
        presenter.setPresenter3D(presenter3D);

    }



}
