package programming146.fernsteuerung.java;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.Properties;

public class Gui {
    @FXML public MenuItem closeMenuItem;
    @FXML public AnchorPane root;
    @FXML public MenuItem connectionButton;
    @FXML public MenuItem showSettingsButton;
    @FXML public TextArea textAreaOutput;

    /**
     * closes window and ends program
     */
    @FXML
    public void pressClose() {
        Stage stage = (Stage) root.getScene().getWindow(); //get a handle to the stage
        stage.close();
    }

    /**
     * checks server registration and connects to server
     */
    @FXML
    public void pressServerConnection() {
        String request;
        Properties properties = new Properties();
        String username = "",password = "";

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
            password = properties.getProperty("password");
        }
        if(username.isEmpty()&&password.isEmpty()){
            request = "initial";
        }else {
            request = "login";
        }

        try {
            ServerConnection serverConnection = new ServerConnection(request);
            serverConnection.call();
        }catch (Exception e){
            System.out.print(e.getMessage());
        }
    }

    @FXML
    public void pressShowSettings(){
        Properties properties = new Properties();
        String username = "",password = "";

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
            password = properties.getProperty("password");
        }

        textAreaOutput.setText("username: "+username+"\npassword: "+password);
    }
}
