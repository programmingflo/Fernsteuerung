package programming146.fernsteuerung.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("gui.fxml"));
        loader.setController(new Gui());
        Pane mainPain = loader.load();*/
        Parent root = FXMLLoader.load(getClass().getResource("gui.fxml"));
        primaryStage.setTitle("Fernsteuerung");
        primaryStage.setScene(new Scene(root, 600,400));
        primaryStage.show();
    }

    public static void main(String [] args){launch(args);}
}
