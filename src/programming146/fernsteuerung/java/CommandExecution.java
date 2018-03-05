package programming146.fernsteuerung.java;

import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;

import static java.awt.event.KeyEvent.*;
import static java.lang.Character.isUpperCase;


public class CommandExecution {
    CommandExecution() {}

    public String[] executeCommand(JSONObject rawCommand){ //Aufruf in Gui-Klasse in der pressTest()-Funktion
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
                            case "ALT":
                                robot.keyPress(VK_ALT);
                                output.append("ALT/");
                                break;
                            case "ALT-RELEASE":
                                robot.keyRelease(VK_ALT);
                                output.append("ALT-RELEASE/");
                                break;
                            case "STRG":
                                robot.keyPress(VK_CONTROL);
                                output.append("ALT/");
                                break;
                            case "STRG-RELEASE":
                                robot.keyRelease(VK_CONTROL);
                                output.append("STRG-RELEASE/");
                                break;
                            case "SLASH":
                                robot.keyPress(VK_SLASH);
                                output.append("SLASH/");
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
                    return new String[]{"SUCCESS", rawCommand.getString("id"), output.toString()};
                case "error":
                    return new String[]{"ERROR"+ rawCommand.getString("command"), rawCommand.getString("id")};
                default:
                    return new String[]{"ERROR unknown device"};
            }
        }catch(AWTException|JSONException e){
            return new String[]{e.getMessage()};
        }
    }

}
