package Presenter;

import Concurrent.HydrogenBondService;
import Concurrent.MoleculeAssemblerService;
import HBondInference.HydrogonBonds;
import Model2D.Nussinov;
import Model3D.MoleculeAssembler;
import Selection.SelectionControl;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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

    private HydrogenBondService hydrogenBondService;
    private Text[] nucleotideLetters;

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
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private TextFlow sequenceTextField;
    @FXML
    private CheckMenuItem animatedSelectionBox;


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

                System.out.println("Processors: " + Runtime.getRuntime().availableProcessors());

                //Set the sequence to the textfield
                this.nucleotideLetters = makeNucleotideTextObjects(presenter.getSequence());
                sequenceTextField.getChildren().clear();
                sequenceTextField.getChildren().addAll(nucleotideLetters);
                selectionControl.setNucleotideLetters(nucleotideLetters);
                //sequenceField.setText(presenter.getSequence());

                //Use differen Threads to calculate hydrogen bonds and set up all structure elements
                hydrogenBondService.reset();
                hydrogonBonds = new HydrogonBonds(presenter.getSequence().length());
                hydrogenBondService.updateService(presenter.getAtoms(), hydrogonBonds);
                hydrogenBondService.start();
                progressIndicator.setVisible(true);
                progressIndicator.progressProperty().bind(hydrogenBondService.progressProperty());
                MoleculeAssemblerService moleculeAssemblerService = new MoleculeAssemblerService(presenter.getAtoms(), presenter.getSequence().length());
                moleculeAssemblerService.start();

                //If molecules are assembled, put all parts together and put them on the scene
                moleculeAssemblerService.setOnSucceeded(event2 -> {
                    presenter.putStructureOnScene(moleculeAssemblerService.getMoleculeAssembler(), hydrogonBonds);
                    presenter.getPresenter3D().centerStructure();

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
                    selectionControl.initSelectionModel(moleculeAssemblerService.getMoleculeAssembler().getNucleotides(), presenter2D.getNodes());
                    showInfo(selectedFile.getName() + " loaded succesfully!");

                    this.fileLoaded = true;
                    progressIndicator.setVisible(false);
                });

            } else {
                showInfo("Selected file is not a .pdb file!");
            }

        }catch (NullPointerException np){
            //console.setText(console.getText() + "\n> " + "No file selected.");
            np.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
            showInfo(e.getCause() + " caught during file loading." + e.getMessage());
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
        //Make Presenter3D instance
        presenter3D = new Presenter3D();
        //presenter3D.setStructurePane(structurePane);
        //Make Presenter instance
        presenter = new Presenter();
        presenter.setSecondaryStructurePane(secondaryStructure);
        presenter.setPresenter2D(presenter2D);
        presenter.setStructurePane(structurePane);
        presenter.setPresenter3D(presenter3D);

        selectionControl = new SelectionControl(sequenceField, secondaryStructure, structurePane, animatedSelectionBox);

        hydrogenBondService = new HydrogenBondService();

    }

    //Updates text in the console
    private void showInfo(String info){
        this.console.setText(console.getText() + "\n> " + info);
        this.console.setScrollTop(Double.MAX_VALUE);
    }

    //Make a text object for every nucleotide for TextFlow usage
    private Text[] makeNucleotideTextObjects(String sequence){
        Text[] texts = new Text[sequence.length()];
        for (int i = 0; i < sequence.length(); i++){
            texts[i] = new Text(String.valueOf(sequence.charAt(i)));
        }
        return texts;
    }



}
