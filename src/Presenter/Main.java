package Presenter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.stage.Stage;


/**
 * Created by kevin_000 on 18.01.2016.
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("NewView.fxml"));
        primaryStage.setTitle("StructuRNA");
        primaryStage.setScene(new Scene(root, 1000, 700,true, SceneAntialiasing.BALANCED));
        primaryStage.show();
    }



    public static void main(String[] args) { launch(args);}
}
