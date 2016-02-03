package Presenter;

import HBondInference.HydrogonBonds;
import Model2D.Nussinov;
import Selection.SelectionControl;
import javafx.beans.binding.DoubleBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;

public class Controller {

    /*
    The Presenter instances that hold all methods and models
     */

    private Presenter presenter;// = new Presenter();
    private Presenter2D presenter2D;
    private Presenter3D presenter3D;// = new Presenter3D();
    private HydrogonBonds hydrogonBonds;
    private SelectionControl selectionControl;
    private boolean fileLoaded = false;

    @FXML
    private MenuItem colorNucleotide;
    @FXML
    private TextArea console;
    @FXML
    private MenuItem colorBasetype;
    @FXML
    private Menu backgroundColor;
    @FXML
    private MenuItem backgroundWhite;
    @FXML
    private MenuItem backgroundBlack;
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
    @FXML
    private MenuItem menuItemHbonds;
    @FXML
    private MenuItem menuClearSelection;

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
                showInfo("Loading file " + selectedFile.getName());

                //Set the sequence to the textfield
                sequenceField.setText(presenter.getSequence());

                //hydrogen bond inference, use centered atoms
                hydrogonBonds = new HydrogonBonds(presenter.getSequence().length());
                hydrogonBonds.inferHydrogenBonds(presenter.getAtoms());

                //SelectionControl instance, handles selection events
                selectionControl = new SelectionControl(sequenceField, secondaryStructure, structurePane);

                //make 3D structure
                presenter.getPresenter3D().setUpMoleculeAssembler(hydrogonBonds.getHbonds(), hydrogonBonds.getBondAtoms(), hydrogonBonds.getHbondAtomConnections());
                presenter.getPresenter3D().centerStructure();
                presenter.setUp3DStructure();

                //Draw the secondary structure
                //Check computed dot bracket - if invalid, use nussinov
                if (hydrogonBonds.isValidDotBracketNotation(hydrogonBonds.getDotBracket())) {
                    presenter.setUp2DStructure(hydrogonBonds.getDotBracket());
                    showInfo("Computed secondary structure: " + hydrogonBonds.getDotBracket());
                } else {
                    Nussinov nussinov = new Nussinov(presenter.getSequence());
                    nussinov.apply();
                    presenter.setUp2DStructure(nussinov.getBracketNotation());
                    showInfo("Could not infer hydrogen bonds. Nussinov algorithm used instead: " + nussinov.getBracketNotation());
                }

                //With all objects produced, initialize SelectionModels
                selectionControl.initSelectionModel(presenter3D.getNucleotides(), presenter2D.getNodes());
                showInfo(selectedFile.getName() + " loaded succesfully!");
                this.fileLoaded = true;
            } else {
                showInfo("Selected file is not a .pdb file!");
            }

        }catch (NullPointerException np){
            //console.setText(console.getText() + "\n> " + "No file selected.");
            np.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
            showInfo(e.getCause() + " caught during file loading.");
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
        if (fileLoaded) {
            presenter3D.colorByNucleotide();
            structurePane.getChildren().clear();
            structurePane.getChildren().addAll(presenter3D.subScene);
        }
        else showInfo("No file loaded");
    }

    //Color by basetype
    @FXML
    void colorByBasetype(ActionEvent event) {
        if (fileLoaded) {
            presenter3D.colorByBasetype();
            structurePane.getChildren().clear();
            structurePane.getChildren().addAll(presenter3D.subScene);
        }
        else showInfo("No file loaded");
    }

    //Delete the structure
    @FXML
    void clear(ActionEvent event) {
        if (fileLoaded) {
            structurePane.getChildren().clear();
            secondaryStructure.getChildren().clear();
            console.setText(console.getText() + "\n> Structure deleted.");
        }
    }

    //Show help for program usage
    @FXML
    void showHelp(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About StructuRNA");
        alert.setHeaderText("How to use StructuRNA");
        alert.setContentText(
                "StructuRNA can show RNA molecules that are in the PDB file format. " +
                        "If the structure includes H-molecules, StructuRNA will try to infer hydrogen bonds. " +
                        "Otherwise, the Nussinov algorithm will be used to compute a secondary structure " +
                        "approximation.\n\n" +
                        "You can zoom into the molecule by holding Shift and dragging the mouse up and down. " +
                        "The molecule can be rotated be holding the mouse and dragging it over the screen. " +
                        "You can also drag the molecule over the screen when Alt is pressed.\n\n" +
                        "Specific nucleotides can be selected by clicking on them. The selection is then visible " +
                        "in both the 3D and 2D structure.\n\n" +
                        "Use the options in the Edit and View menus to accustomize the structure visualization.\n\n" +
                        "Have fun!\n" +
                        "Kevin Menden"
        );
        alert.show();
    }

    //Color the background white
    @FXML
    void setWhiteBackground(ActionEvent event) {
        presenter3D.subScene.setFill(Color.WHITE);
    }
    //Color the background black
    @FXML
    void setBlackBackground(ActionEvent event) {
        presenter3D.subScene.setFill(Color.BLACK);
    }

    /*
    Select all nucleotides that build hydrogen bonds
     */
    @FXML
    void selectHbonds(ActionEvent event) {
        if (fileLoaded)
        selectionControl.selectHbondsNucleotides(hydrogonBonds.getHBondIndices());
        else showInfo("No file loaded");
    }
    //Clear all selections
    @FXML
    void clearSelection(ActionEvent event) {
        if (fileLoaded)
        selectionControl.clearSelection();
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
        //presenter3D.setStructurePane(structurePane);
        //Make Presenter instance
        presenter = new Presenter();
        presenter.setSecondaryStructurePane(secondaryStructure);
        presenter.setPresenter2D(presenter2D);
        presenter.setStructurePane(structurePane);
        presenter.setPresenter3D(presenter3D);

    }

    //Updates text in the console
    private void showInfo(String info){
        this.console.setText(console.getText() + "\n> " + info);
        this.console.setScrollTop(Double.MAX_VALUE);
    }



}
