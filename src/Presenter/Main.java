package Presenter;

import View.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

/**
 * Created by kevin_000 on 18.01.2016.
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("view.NewView.fxml"));

        Scene scene = new Scene(root, 600, 600, true);
        primaryStage.setTitle("StructuRNA");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
