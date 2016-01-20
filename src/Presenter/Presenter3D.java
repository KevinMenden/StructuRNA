package Presenter;

import PDBParser.PDBFile;
import View.View;
import javafx.scene.Group;

/**
 * Created by kevin_000 on 19.01.2016.
 * Class contains all methods for the 3D Visualization of the molecule
 */
public class Presenter3D {

    private View view;

    private Group structureGroup;

    public Presenter3D(View view, Group structureGroup){
        this.view = view;
        this.structureGroup = structureGroup;
    }

    /**
     * Add all bindings and actionEvents to the GUI
     */
    private void addActions(){

        /*
        Save current position if mouse is pressed
         */
        view.subScene.setOnMousePressed(event -> {
            view.mousePosX = event.getSceneX();
            view.mousePosY = event.getSceneY();
        });

        //Rotate the structureGroup when mouse is dragged
        view.subScene.setOnMouseDragged(event -> {
            double dY = event.getSceneY() - view.mousePosY;
            double dX = event.getSceneX() - view.mousePosX;
            //Zoom if shift is down
            if (event.isShiftDown()){
                view.cameraTranslate.setZ(view.cameraTranslate.getZ() + dY);
            }
            else {
                //Else rotate object
                view.cameraRotateY.setAngle(view.cameraRotateY.getAngle() + dX);
                view.cameraRotateX.setAngle(view.cameraRotateX.getAngle() - dY);
            }
        });

        //BINDINGS
        view.separator.layoutXProperty().addListener((observable, oldValue, newValue) -> {
            view.subScene.setLayoutX(Double.parseDouble(newValue.toString()));
        });


    }
}
