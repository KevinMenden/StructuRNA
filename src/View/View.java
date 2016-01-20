package View;

import javafx.geometry.Orientation;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * Created by kevin_000 on 18.01.2016.
 */
public class View extends BorderPane {

    /*
    Create instance of View with a Group object containing the structure elements
     */
    public View(Group structureGroup, Group graphGroup){
        this.structureGroup = structureGroup;
        this.graphGroup = graphGroup;
        layoutForm();
    }

    //The Group with the 3D objects
    public Group structureGroup = new Group();

    //The Group with the 2D objects
    public Group graphGroup = new Group();

    /*
    All menus with their items
     */
    public MenuBar menuBar = new MenuBar();
    public Menu fileMenu = new Menu("File");
    public MenuItem openFile = new MenuItem("Open file");
    public Menu editMenu = new Menu("Edit");
    public Menu viewMenu = new Menu("View");

    public VBox controlsBox = new VBox();

    public TextField sequenceField = new TextField();

    //Separator for 2D-3D separation
    public Separator separator = new Separator();

    //Console for User-Program communication
    public TextArea consoleArea = new TextArea();

    //A pane, SubScene and camera for the 3D structure
    public Pane threeDpane = new Pane();
    public SubScene subScene;
    public PerspectiveCamera camera = new PerspectiveCamera();
    public Rotate cameraRotateX = new Rotate(0, new Point3D(1, 0, 0));
    public Rotate cameraRotateY = new Rotate(0, new Point3D(0, 1, 0));
    public Translate cameraTranslate = new Translate(0, 0, -1000);

    //Variables for saving the current mouse position
    public double mousePosX, mousePosY;

    public void layoutForm() {
        fileMenu.getItems().addAll(openFile);
        menuBar.getMenus().addAll(fileMenu, editMenu, viewMenu);

        //Set up the separator
        separator.setOrientation(Orientation.VERTICAL);
        separator.setLayoutX(300);
        separator.setPrefHeight(600);
        separator.setPrefWidth(10);
        separator.setLayoutY(50);
        this.setCenter(separator);

        consoleArea.setPrefWidth(300);
        consoleArea.setPrefHeight(100);
        consoleArea.setLayoutY(300);
        this.setBottom(consoleArea);

        //Add a SubScene to the threeDpane and set up camera
        camera.setFarClip(10000);
        camera.setNearClip(0.1);
        camera.getTransforms().addAll(cameraRotateX, cameraRotateY, cameraTranslate);
        subScene = new SubScene(structureGroup, 300, 300, true, SceneAntialiasing.BALANCED);
        //subScene.setLayoutX(300);
        subScene.setCamera(camera);


        //DUMMY DUMMY DUMMMY
        Circle circle = new Circle(20);
        graphGroup.getChildren().add(circle);
        sequenceField.setText("ACGACTGACTGACGACGTTAGCTGATCGTTTTAGTGTGCTGTGGTGAGAGGATCGCGCG");
        consoleArea.setMinHeight(100);

        controlsBox.getChildren().addAll(menuBar, sequenceField);
        this.setRight(subScene);
        this.setLeft(graphGroup);
        this.setTop(controlsBox);
    }
}
