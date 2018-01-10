package programming146.fernsteuerung.java;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class SettingsDialog implements Initializable{
    @FXML public TextField settingsUsernameTextField;
    @FXML public PasswordField settingsPasswordField;
    @FXML public PasswordField settingsSubmitPasswordField;
    @FXML public Button settingsSaveButton;
    @FXML public AnchorPane settings;
    @FXML public Button settingsCloseButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Properties properties = new Properties();
        String username;

        try {
            File propertiesFile = new File("");
            FileInputStream fileInputStream = new FileInputStream(propertiesFile);
            properties.load(fileInputStream);
            fileInputStream.close();
        }catch (IOException e){
            System.out.print(e.getMessage());
        }

        if(!properties.isEmpty()) {
            username = properties.getProperty("username");
            settingsUsernameTextField.setText(username);
        }/*else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            //alert.setHeaderText("Look, an Error Dialog");
            alert.setContentText("Error on loading properties!");

            alert.showAndWait();
        }*/
    }

    @FXML
    public void pressSaveButton() {
        String username = settingsUsernameTextField.getText();
        String password = settingsPasswordField.getText();
        String passwordSubmit = settingsSubmitPasswordField.getText();
        Properties properties = new Properties();

        try {
            File propertiesFile = new File("config.properties");
            FileInputStream fileInputStream = new FileInputStream(propertiesFile);
            properties.load(fileInputStream);
            fileInputStream.close();
        }catch (IOException e){
            System.out.print(e.getMessage());
        }
        if(password.equals(passwordSubmit)) {
            properties.setProperty("username", username);
            properties.setProperty("password", password);
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            //alert.setHeaderText("Look, an Error Dialog");
            alert.setContentText("Passwords are not equal to each other!");
            alert.showAndWait();
        }
        try {
            properties.store(new FileOutputStream("config.properties"),null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) settings.getScene().getWindow(); //get a handle to the stage
        stage.close();
    }

    public void pressCloseButton() {
        Stage stage = (Stage) settings.getScene().getWindow(); //get a handle to the stage
        stage.close();
    }
}
