package programming146.fernsteuerung.java;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Gui {
    @FXML public MenuItem closeMenuItem;
    @FXML public AnchorPane root;
    @FXML public MenuItem connectionButton;
    @FXML public MenuItem showSettingsButton;
    @FXML public TextArea textAreaOutput;
    @FXML public MenuItem testButton;
    @FXML public MenuItem changeSettingsButton;

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
            File propertiesFile = new File("config.properties");
            FileInputStream fileInputStream = new FileInputStream(propertiesFile);
            properties.load(fileInputStream);
            fileInputStream.close();
        }catch (IOException e){
            //System.out.print(e.getCause());
            e.printStackTrace();
        }

        if(!properties.isEmpty()) {
            username = properties.getProperty("username");
            password = properties.getProperty("password");
        }
        if(username.isEmpty()&&password.isEmpty()){
            request = "initial";
        }else {
            request = "command";
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

    @FXML
    public void pressTest(){
        JSONObject testCommand = new JSONObject();
        try {
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("cmd.exe /c start pause");
            testCommand.put("device","keyboard");
            //testCommand.put("command","test");
            //testCommand.put("command","WINDOWS/R/WINDOWS-RELEASE/C/M/D/ENTER");
            testCommand.put("command","C/M/D/ENTER");
        } catch (JSONException e) {
            System.out.print(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        CommandExecution commandExecution = new CommandExecution();
        String result = commandExecution.executeCommand(testCommand);
        textAreaOutput.setText(result);
    }

    @FXML
    public void pressChangeSettings(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("settingsDialog.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Einstellungen");
            stage.setScene(new Scene(root, 600,400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
