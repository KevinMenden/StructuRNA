package Presenter;

import Model2D.Nussinov;
import Model3D.HydrogonBonds;
import Selection.SelectedText;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;

import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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
    private SplitPane vertSplitPane;

    @FXML
    private MenuItem close;

    @FXML
    private TextField sequenceField;

    @FXML
    private Menu fileMenu;

    @FXML
    private Pane secondaryStructure;

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
                this.presenter2D.setPdbFile(presenter.getPdbFile());
                console.setText(console.getText() + "\n> " + selectedFile.getAbsolutePath() + " loaded succesfully!");

                //Set the Atom[] array in the Presenter3D Class and extract sequence
                presenter3D.setAtoms(presenter.getPdbFile().getAtoms());
                sequenceField.setText(presenter.getPdbFile().sequence);

                //Make the 3D structure
                presenter3D.makeMolecules();
                structurePane.getChildren().clear();
                structurePane.getChildren().addAll(presenter3D.subScene);
                structurePane.setAlignment(presenter3D.subScene, Pos.CENTER);
                //Bind scene to its pane
                bindSceneToPane(presenter3D.subScene, vertSplitPane);

                //Make the secondary structure
                //Infer base pairing from PDB file
                try {
                    HydrogonBonds hydrogonBonds = new HydrogonBonds();
                    hydrogonBonds.setSequence(presenter.getPdbFile().sequence);
                    System.out.println(hydrogonBonds.inferHydrogenBonds(presenter.getPdbFile().getAtoms()));
                    presenter2D.buildSecondaryStructureGraph(hydrogonBonds.inferHydrogenBonds(presenter.getPdbFile().getAtoms()));
                    secondaryStructure.getChildren().clear();
                    secondaryStructure.getChildren().add(presenter2D.getGraphGroup());
                }catch (NullPointerException np){
                    //If Problems with H bond inference occur, use Nussinov instead
                    console.setText(console.getText() + "\n> " + " Could not infer secondary structure from PDB file. Using Nussinov-algorithm instead.");
                    Nussinov nussinov = new Nussinov(presenter.getPdbFile().sequence);
                    nussinov.apply();
                    presenter2D.buildSecondaryStructureGraph(nussinov.getBracketNotation());
                    secondaryStructure.getChildren().clear();
                    secondaryStructure.getChildren().add(presenter2D.getGraphGroup());
                }
            } else {
                console.setText(console.getText() + "\n> " + "Selected file is not a .pdb file!");
            }

        }catch (NullPointerException np){
            console.setText(console.getText() + "\n> " + "No file selected.");
        }
        catch (Exception e){
            e.printStackTrace();
            console.setText(console.getText() + "\n> Exception caught during file loading." );
        }
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

        //Make Presenter instance
        presenter = new Presenter();

        //Make Presenter2D instane
        presenter2D = new Presenter2D();

        //Make Presenter3D instance, set up action events for handling
        //the 3D structure
        presenter3D = new Presenter3D();
        presenter3D.setStructurePane(structurePane);
        presenter3D.setActionEvents();

        //Initialize Text selection class
        SelectedText selectedText = new SelectedText(sequenceField);

    }

    /**
     * Bind the widht and height of subScene to the splitPane
     * using DoubleBindings and Properties
     * @param subScene
     * @param splitPane
     */
    private void bindSceneToPane(SubScene subScene, SplitPane splitPane){
        subScene.widthProperty().bind(new DoubleBinding() {
            @Override
            protected double computeValue() {
                return splitPane.getDividerPositions()[0];
            }
        }.multiply(splitPane.widthProperty()));

        subScene.heightProperty().bind(splitPane.heightProperty());
    }


}
