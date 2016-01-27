package Model3D;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * Created by kevin_000 on 27.01.2016.
 */
public class MousHandler3D {


    //Mouse positions when mouse is pressed
    private static double mousePosX, mousePosY;


    public static void addMouseHandler(AnchorPane structurePane, Group structureGroup, Rotate cameraRotateX, Rotate cameraRotateY, Translate cameraTranslate) {

    //Save position if mouse is pressed
    structurePane.setOnMousePressed(event-> {
        mousePosX = event.getSceneX();
        mousePosY = event.getSceneY();
    }

    );
        /*
        Move camera if mouse is dragged (zooming or rotating)
         */
    structurePane.setOnMouseDragged(event->

    {
        double dY = event.getSceneY() - mousePosY;
        double dX = event.getSceneX() - mousePosX;
        //Zoom if shift is down
        if (event.isShiftDown()) {
            cameraTranslate.setZ(cameraTranslate.getZ() - dY);
            //Move if alt is down
        } else if (event.isAltDown()) {
            structureGroup.setTranslateX(structureGroup.getTranslateX() + 0.1 * dX);
            structureGroup.setTranslateY(structureGroup.getTranslateY() + 0.1 * dY);
        }
        //Else rotate the camera around the object
        else {
            cameraRotateY.setAngle(cameraRotateY.getAngle() - dX);
            cameraRotateX.setAngle(cameraRotateX.getAngle() + dY);
        }
        mousePosY = event.getSceneY();
        mousePosX = event.getSceneX();
    }

    );
}
}
