package programming146.fernsteuerung.java;

import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.awt.event.KeyEvent.*;

import static java.awt.event.KeyEvent.VK_T;
import static java.awt.event.KeyEvent.VK_WINDOWS;


public class CommandExecution {
    public CommandExecution(JSONObject rawCommand) throws AWTException {
    }

    public String executeCommand(JSONObject rawCommand) throws JSONException, AWTException {
        Robot robot = new Robot();
        String device = rawCommand.getString("device");
        if(device.equals("keyboard")){
            robot.keyPress(VK_WINDOWS);
        }

        return null;
    }

}
