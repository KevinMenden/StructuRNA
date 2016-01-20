package Presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

public class Controller {

    @FXML
    private VBox root;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu editMenu;

    @FXML
    private MenuItem colorNucleotide;

    @FXML
    private TextArea console;

    @FXML
    private MenuItem colorBasetype;

    @FXML
    private Pane structurePane;

    @FXML
    private Menu helpMenu;

    @FXML
    private MenuItem aboutButton;

    @FXML
    private TextField sequenceField;

    @FXML
    private Menu fileMenu;

    @FXML
    private Pane secondaryStructure;


}
