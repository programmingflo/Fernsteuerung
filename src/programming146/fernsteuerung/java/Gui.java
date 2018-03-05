package programming146.fernsteuerung.java;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class Gui {
    @FXML public MenuItem closeMenuItem;
    @FXML public AnchorPane root;
    @FXML public MenuItem connectionButton;
    @FXML public MenuItem showSettingsButton;
    @FXML public TextArea textAreaOutput;
    @FXML public MenuItem testButton;
    @FXML public MenuItem changeSettingsButton;
    @FXML public MenuItem hideMenuItem;
    private ExecutionLoop executionLoop = new ExecutionLoop();

    /**
     * closes window and ends program
     */
    @FXML
    public void pressClose() {
        Stage stage = (Stage) root.getScene().getWindow(); //get a handle to the stage
        stage.close();
        System.exit(0);
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
            request = "register";
        }else {
            request = "test";
        }

        try {
            ServerConnection serverConnection = new ServerConnection(request, "", "");
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
        ServerConnection serverConnection = new ServerConnection("getCommand", "", "");
        JSONObject testCommand = serverConnection.call();

        CommandExecution commandExecution = new CommandExecution();
        String[] result = commandExecution.executeCommand(testCommand);
        String output = result[0];
        if(result.length>1){
            output += ": " + result[2];
            ServerConnection serverConnection1 = new ServerConnection("saveResult",result[1],result[0]);
            serverConnection1.call();
        }
        textAreaOutput.setText(textAreaOutput.getText()+"\n"+output);
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
    @FXML
    public void pressHide(){
        Stage stage = (Stage) root.getScene().getWindow(); //get a handle to the stage
        addAppToTray();
        stage.hide();
        System.out.print("works hidden");
        String result = executionLoop.executeLoop();
        System.out.print(result);
    }
    // a timer allowing the tray icon to provide a periodic notification event.
    private Timer notificationTimer = new Timer();

    /**
     * Sets up a system tray icon for the application.
     * @author Sergey Gornostaev @ https://stackoverflow.com/questions/40571199/creating-tray-icon-using-javafx
     */
    private void addAppToTray() {
        try {
            // ensure awt toolkit is initialized.
            java.awt.Toolkit.getDefaultToolkit();

            // app requires system tray support, just exit if there is no support.
            if (!java.awt.SystemTray.isSupported()) {
                System.out.println("No system tray support, application exiting.");
                Platform.exit();
            }

            // set up a system tray icon.
            java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
            File icon = new File("Fernsteuerung.png");
            java.awt.Image image = ImageIO.read(icon);
            java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image);

            // if the user double-clicks on the tray icon, show the main app stage.
            trayIcon.addActionListener(event -> Platform.runLater(this::showStage));

            // if the user selects the default menu item (which includes the app name),
            // show the main app stage.
            java.awt.MenuItem openItem = new java.awt.MenuItem("Fernsteuerung");
            openItem.addActionListener(event -> Platform.runLater(this::showStage));

            // the convention for tray icons seems to be to set the default icon for opening
            // the application stage in a bold font.
            java.awt.Font defaultFont = java.awt.Font.decode(null);
            java.awt.Font boldFont = defaultFont.deriveFont(java.awt.Font.BOLD);
            openItem.setFont(boldFont);

            // to really exit the application, the user must go to the system tray icon
            // and select the exit option, this will shutdown JavaFX and remove the
            // tray icon (removing the tray icon will also shut down AWT).
            java.awt.MenuItem exitItem = new java.awt.MenuItem("Exit");
            exitItem.addActionListener(event -> {
                notificationTimer.cancel();
                Platform.exit();
                tray.remove(trayIcon);
            });

            // setup the popup menu for the application.
            final java.awt.PopupMenu popup = new java.awt.PopupMenu();
            popup.add(openItem);
            popup.addSeparator();
            popup.add(exitItem);
            trayIcon.setPopupMenu(popup);

            // create a timer which periodically displays a notification message.
            notificationTimer.schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            javax.swing.SwingUtilities.invokeLater(() ->
                                    trayIcon.displayMessage(
                                            "Fernsteuerung",
                                            "Background modus aktiviert",
                                            java.awt.TrayIcon.MessageType.INFO
                                    )
                            );
                        }
                    },
                    5_000
            );

            // add the application tray icon to the system tray.
            tray.add(trayIcon);
        } catch (java.awt.AWTException | IOException e) {
            System.out.println("Unable to init system tray");
            e.printStackTrace();
        }
    }
    private void showStage() {
        executionLoop.loopTimer.cancel();
        Stage stage = (Stage) root.getScene().getWindow(); //get a handle to the stage
        stage.show();
        /*if (stage != null) {
            stage.show();
            stage.toFront();
        }*/
    }
}
