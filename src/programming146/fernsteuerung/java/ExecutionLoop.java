package programming146.fernsteuerung.java;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

class ExecutionLoop{
    ExecutionLoop(){}

    Timer loopTimer;

    String executeLoop(){
        StringBuilder output = new StringBuilder();
        loopTimer = new Timer();
        loopTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                ServerConnection serverConnection = new ServerConnection("getCommand");
                JSONObject testCommand = serverConnection.call();
                CommandExecution commandExecution = new CommandExecution();
                output.append(commandExecution.executeCommand(testCommand));
            }
        },5000,5000);
        return output.toString();
    }
}
