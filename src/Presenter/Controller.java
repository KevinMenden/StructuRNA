package Presenter;

import Concurrent.HydrogenBondService;
import Concurrent.MoleculeAssemblerService;
import HBondInference.HydrogonBonds;
import Model2D.Nussinov;
import Model3D.MoleculeAssembler;
import Model3D.VDWSurfaceAssembler;
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
import java.io.SyncFailedException;

/*
This class controls all the GUI elements of the FXML view
It uses Presenter, Presenter2D and Presenter3D to control the structures
SelectionControl is used for monitoring selection of structure elements

The computational intensive tasks of hydrogen bond inference and
structure composition are delegated to different threads, to keep
the GUI responsive (and theoreticall improve performance, however, the threads
are not always delegated to different cores)
 */
public class Controller {

    /*
    The Presenter instances that hold all methods and models
     */

    private Presenter presenter;
    private Presenter2D presenter2D;
    private Presenter3D presenter3D;
    private HydrogonBonds hydrogonBonds;
    private SelectionControl selectionControl;
    private boolean fileLoaded = false;

    private HydrogenBondService hydrogenBondService;
    private MoleculeAssemblerService moleculeAssemblerService;
    private MoleculeAssembler moleculeAssembler;
    private Text[] nucleotideLetters;
    private VDWSurfaceAssembler vdwSurfaceAssembler;

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
    @FXML
    private Slider slider2DScale;
    @FXML
    private MenuItem menuItemSize2D;
    @FXML
    private Menu sizeMenu;
    @FXML
    private MenuItem surfaceMenu;
    @FXML
    private MenuItem hideSurfaceMenu;


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
                showInfo("Loading file " + selectedFile.getName() + " ...");

                //Set the sequence to the textfield
                this.nucleotideLetters = makeNucleotideTextObjects(presenter.getSequence());
                sequenceTextField.getChildren().clear();
                sequenceTextField.getChildren().addAll(nucleotideLetters);
                selectionControl.setNucleotideLetters(nucleotideLetters);

                //Use differen Threads to calculate hydrogen bonds and set up all structure elements
                hydrogenBondService.reset();
                hydrogonBonds = new HydrogonBonds(presenter.getSequence().length());
                hydrogenBondService.updateService(presenter.getAtoms(), hydrogonBonds);
                hydrogenBondService.start();
                progressIndicator.setVisible(true);
                progressIndicator.progressProperty().bind(hydrogenBondService.progressProperty());
                moleculeAssemblerService = new MoleculeAssemblerService(presenter.getAtoms(), presenter.getSequence().length());
                moleculeAssemblerService.start();

                //If molecules are assembled, put all parts together and put them on the scene
                //Note: molecule assembling takes approx. 3 times as long as hydrogen bonds inference (measured for different molecule sizes)
                moleculeAssemblerService.setOnSucceeded(event2 -> {
                    if (hydrogenBondService.getState() == Worker.State.SUCCEEDED) {
                        this.moleculeAssembler = moleculeAssemblerService.getMoleculeAssembler();
                        presenter.putStructureOnScene(this.moleculeAssembler, hydrogonBonds);
                        presenter.getPresenter3D().centerStructure();
                    } else {
                        //For the unlikely case that molecule assembling was faster than hbond-inference, wait some time
                        //Should only be possible for small molecules
                        try {
                            Thread.sleep(300);
                            this.moleculeAssembler = moleculeAssemblerService.getMoleculeAssembler();
                            presenter.putStructureOnScene(this.moleculeAssembler, hydrogonBonds);
                            presenter.getPresenter3D().centerStructure();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

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
                    //Set up slider for 2D scale
                    slider2DScale.setValue(1.0);
                    slider2DScale.valueProperty().addListener((observable, oldValue, newValue) -> {
                        if (slider2DScale.isValueChanging()) {
                            presenter2D.getGraphGroup().setScaleX(slider2DScale.getValue());
                            presenter2D.getGraphGroup().setScaleY(slider2DScale.getValue());
                        }
                    });

                    //After molecule has loaded, make the Van der Waals surface for later usage
                    vdwSurfaceAssembler.addVanDerWaalsSurface(presenter.getAtoms());

                    this.fileLoaded = true;
                    progressIndicator.setVisible(false);
                });

            } else {
                showInfo("Selected file is not a .pdb file!");
            }

        }catch (NullPointerException np){
            showInfo(np.getMessage() +  " thrown while file loading.");
            //np.printStackTrace();
        }
        catch (Exception e){
            //e.printStackTrace();
            showInfo(e.getCause() + " caught during file loading." + e.getMessage());
        }
    }

    //Center the structure
    @FXML
    void center(ActionEvent event) {
        presenter3D.centerStructure();
        vdwSurfaceAssembler.centerStructure();
    }

    //Color each nucleotide
    @FXML
    void colorByNucleotide(ActionEvent event) {
        if (fileLoaded) {
            moleculeAssembler.colorByNucleotide();
        }
        else showInfo("No file loaded");
    }

    //Color by basetype
    @FXML
    void colorByBasetype(ActionEvent event) {
        if (fileLoaded) {
            moleculeAssembler.colorByBasetype();
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
            sequenceTextField.getChildren().clear();
        }
    }

    //Show help for program usage
    @FXML
    void showHelp(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About StructuRNA");
        alert.setHeaderText("How to use StructuRNA");
        alert.setContentText(
                "This Program was written as project in the course Advanced Java for " +
                        "Bioinformatics, taught by Prof Dr. Daniel Huson and Ania Gorska " +
                        "from Tuebingen University. " +
                        "StructuRNA can show RNA molecules that are in the PDB file format. " +
                        "If the structure includes H-molecules, StructuRNA will try to infer hydrogen bonds. " +
                        "Otherwise, the Nussinov algorithm will be used to compute a secondary structure " +
                        "approximation.\n\n" +
                        "You can zoom into the molecule by holding Shift and dragging the mouse up and down. " +
                        "The molecule can be rotated by holding the mouse and dragging it over the screen. " +
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
        if (fileLoaded){
            presenter3D.subScene.setFill(Color.WHITE);
        vdwSurfaceAssembler.getSurfaceSubScene().setFill(Color.WHITE);
        }
    }
    //Color the background black
    @FXML
    void setBlackBackground(ActionEvent event) {
        if (fileLoaded) {
            presenter3D.subScene.setFill(Color.BLACK);
            vdwSurfaceAssembler.getSurfaceSubScene().setFill(Color.BLACK);
        }
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

        vdwSurfaceAssembler = new VDWSurfaceAssembler();
        vdwSurfaceAssembler.setStructurePane(structurePane);

    }

    //Updates text in the console
    private void showInfo(String info){
        this.console.setText(console.getText() + "\n> " + info);
        this.console.setScrollTop(Double.MIN_VALUE);
    }

    //Make a text object for every nucleotide for TextFlow usage
    private Text[] makeNucleotideTextObjects(String sequence){
        Text[] texts = new Text[sequence.length()];
        for (int i = 0; i < sequence.length(); i++){
            texts[i] = new Text(String.valueOf(sequence.charAt(i)));
        }
        return texts;
    }

    //Show the Van der Waals surface
    @FXML
    void makeSurface(ActionEvent event) {
        if (fileLoaded){
            structurePane.getChildren().clear();
            vdwSurfaceAssembler.centerStructure();
            vdwSurfaceAssembler.updateTransitions(presenter3D.cameraRotateX, presenter3D.cameraRotateY, presenter3D.cameraTranslate, presenter3D.structureGroup);
            structurePane.getChildren().add(vdwSurfaceAssembler.getSurfaceSubScene());
            vdwSurfaceAssembler.switchOnMouseHandling();
        }

    }

    //Hide Van der Waals surface, show molecule structure
    @FXML
    void hideSurface(ActionEvent event) {
        if (fileLoaded){
            structurePane.getChildren().clear();
            presenter3D.updateTransitions(vdwSurfaceAssembler.cameraRotateX, vdwSurfaceAssembler.cameraRotateY, vdwSurfaceAssembler.cameraTranslate, vdwSurfaceAssembler.getSurfaceGroup());
            structurePane.getChildren().add(presenter3D.subScene);
            presenter3D.switchOnMouseHandling();
        }
    }



}
