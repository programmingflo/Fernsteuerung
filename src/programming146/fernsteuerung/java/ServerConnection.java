package programming146.fernsteuerung.java;

import javafx.concurrent.Task;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class ServerConnection extends Task {
    @Override
    protected Object call() throws Exception {
        try{
            return new JSONObject(downloadFromUrl());
        }catch (IOException | JSONException e){
            System.out.print(e.getMessage());
            return null;
        }
    }

    private String downloadFromUrl() throws IOException{
        String urlString = "https://146programming.de/fernsteuerung/login";
        InputStream is = null;
        try{
            URL url = new URL(urlString);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds*/);
            conn.setConnectTimeout(15000 /*milliseconds*/);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            //int response = conn.getResponseCode();
            //Log.d("146s", "response: " + response);
            is = conn.getInputStream();

            //Convert the InputStream into a String
            String contentAsString = convertStreamToString(is);
            System.out.print(contentAsString);
            return contentAsString;
        }catch (IOException e){
            System.out.print(e.getMessage());
        }finally {
            if(is != null){
                try{
                    is.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
    private static String convertStreamToString(InputStream is){
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
