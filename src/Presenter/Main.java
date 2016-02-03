package Presenter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 * Created by kevin_000 on 18.01.2016.
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Load application icon
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("img/trna_pdb.gif")));

        //load FXML, init stage
        Parent root = FXMLLoader.load(getClass().getResource("NewView.fxml"));
        primaryStage.setTitle("StructuRNA");
        Scene scene = new Scene(root, 1000, 700, true, SceneAntialiasing.BALANCED);
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    public static void main(String[] args) { launch(args);}
}
