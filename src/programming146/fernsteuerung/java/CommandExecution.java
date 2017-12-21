package programming146.fernsteuerung.java;

import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;

import static java.awt.event.KeyEvent.*;


public class CommandExecution {
    CommandExecution() {
    }

    public String executeCommand(JSONObject rawCommand){ //Aufruf in Gui-Klasse in der pressTest()-Funktion
        try {
            Robot robot = new Robot();
            String device = rawCommand.getString("device");
            String output = "";
            if (device.equals("keyboard")) {
                String command = rawCommand.getString("command");
                String[] commandParts = command.split("/");

                for(String commandPart: commandParts){
                    if(commandPart.length() == 1){
                        robot.keyPress(commandPart.charAt(0));
                        output = output + (commandPart.charAt(0) + "/");
                    }else{
                        switch (commandPart){
                            case "WINDOWS":
                                robot.keyPress(VK_WINDOWS);
                                output = output + "WINDOWS/";
                                break;
                            case "WINDOWS-RELEASE":
                                robot.keyRelease(VK_WINDOWS);
                                output = output + "WINDOWS-RELEASE/";
                                break;
                            case "ENTER":
                                robot.keyPress(VK_ENTER);
                                output = output + "ENTER/";
                                break;
                        }
                    }
                }
            }
            return "success/"+output;
        }catch(AWTException|JSONException e){
            return e.getMessage();
        }
    }

}
