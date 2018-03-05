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
                ServerConnection serverConnection = new ServerConnection("getCommand", "", "");
                JSONObject testCommand = serverConnection.call();
                CommandExecution commandExecution = new CommandExecution();
                String[]result = commandExecution.executeCommand(testCommand);
                output.append(result[0]);
                if(result.length>1){
                    output.append(result[1]);
                    output.append(result[2]);

                    ServerConnection serverConnection1 = new ServerConnection("saveResult",result[1],result[0]);
                    serverConnection1.call();
                }

            }
        },5000,5000);
        return output.toString();
    }
}
