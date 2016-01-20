package Presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

public class Controller {

    /*
    The Presenter instance that holds all models
     */
    private Presenter presenter = new Presenter();

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
    private SubScene subScene;

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

    @FXML
    void openFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());
        this.presenter.loadFile(selectedFile.getAbsolutePath());
        console.setText(console.getText()+ "\n> " + selectedFile.getAbsolutePath() + " loaded succesfully!");
    }

    @FXML
    void exitProgram(ActionEvent event) {
        System.exit(0);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        secondaryStructure.getChildren().add(presenter.graphGroup);
        presenter.buildSecondaryStructureGraph("...((...))......((...))......((...))......((...))...");
    }


}
