package programming146.fernsteuerung.java;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Gui {
    @FXML public MenuItem closeMenuItem;
    public AnchorPane root;

    @FXML
    public void pressClose() {
        Stage stage = (Stage) root.getScene().getWindow(); //get a handle to the stage
        stage.close();
    }
}
