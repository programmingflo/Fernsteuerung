package programming146.fernsteuerung.java;

import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;

import static java.awt.event.KeyEvent.*;
import static java.lang.Character.isUpperCase;


public class CommandExecution {
    CommandExecution() {
    }

    public String executeCommand(JSONObject rawCommand){ //Aufruf in Gui-Klasse in der pressTest()-Funktion
        try {
            Robot robot = new Robot();
            robot.delay(5000);
            String device = rawCommand.getString("device");
            StringBuilder output = new StringBuilder();
            switch (device) {
                case "keyboard":
                    String command = rawCommand.getString("command");
                    System.out.print(command+"\n");
                    String[] commandParts = command.split("/");

                    for (String commandPart : commandParts) {
                        switch (commandPart) {
                            case "WINDOWS":
                                robot.keyPress(VK_WINDOWS);
                                output.append("WINDOWS/");
                                break;
                            case "WINDOWS-RELEASE":
                                robot.keyRelease(VK_WINDOWS);
                                output.append("WINDOWS-RELEASE/");
                                break;
                            case "ENTER":
                                robot.keyPress(VK_ENTER);
                                output.append("ENTER/");
                                break;
                            default:
                                for (Integer i = 0; i <= commandPart.length()-1; i++) {
                                    if (isUpperCase(commandPart.charAt(i))) {
                                        robot.keyPress(VK_SHIFT);
                                        output.append("SHIFT/");
                                    }
                                    robot.keyPress(commandPart.toUpperCase().charAt(i));
                                    output.append(commandPart.charAt(i)).append("/");
                                    if (isUpperCase(commandPart.charAt(i))) {
                                        robot.keyRelease(VK_SHIFT);
                                        output.append("SHIFT-RELEASE/");
                                    }
                                }
                        }
                    }
                    return "SUCCESS: " + output;
                case "error":
                    return "ERROR: " + rawCommand.getString("command");
                default:
                    return "ERROR: unknown device";
            }
        }catch(AWTException|JSONException e){
            return e.getMessage();
        }
    }

}
