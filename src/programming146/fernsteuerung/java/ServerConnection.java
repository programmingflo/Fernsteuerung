package programming146.fernsteuerung.java;

import javafx.concurrent.Task;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author Tim Grohmann
 */

public class ServerConnection extends Task {
    private final String request;

    /**
     * connection to server
     * @param request request type
     * @return JSONObject server response data
     */
    ServerConnection(String request) {
        this.request = request;
    }

    @Override
    protected Object call() {
        try{
            return new JSONObject(downloadFromUrl(request));
        }catch (JSONException|IOException e){
            System.out.print(e.getMessage());
            return null;
        }
    }

    private String downloadFromUrl(String node) throws IOException{
        String urlString = "https://146programming.de/fernsteuerung/"+node;
        InputStream is = null;
        ArrayList<ArrayList<String>> request = new ArrayList<>();
        Properties properties = new Properties();

        try {
            File propertiesFile = new File("");
            FileInputStream fileInputStream = new FileInputStream(propertiesFile);
            properties.load(fileInputStream);
            fileInputStream.close();
        }catch (IOException e){
            System.out.print(e.getMessage());
        }

        if(!properties.isEmpty()) {
            ArrayList<String> account = new ArrayList<>();
            account.add("account");
            account.add(properties.getProperty("username"));

            ArrayList<String> password = new ArrayList<>();
            password.add("password");
            password.add(properties.getProperty("password"));

            request.add(account);
            request.add(password);

            StringBuilder requestString = new StringBuilder();
            Integer response = 1000;

            for(ArrayList<String> object: request){
                if(object.size() == 2){
                    requestString.append(URLEncoder.encode(object.get(0), "utf-8")).append("=").append(URLEncoder.encode(object.get(1), "utf-8")).append("&");
                }
            }
            if(requestString.charAt(requestString.length()-1)=='&' && requestString.length() > 0){
                requestString = new StringBuilder(requestString.substring(0, requestString.length() - 1));
            }

            byte[]  postData        = requestString.toString().getBytes(Charset.forName("utf-8"));
            //Log.v("146s","postData: "+postData);
            int     postDataLength  = postData.length;
            //Log.v("146s","postDataLength: "+postDataLength);
            //String c = "";
            try {
                URL url = new URL(urlString);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds*/);
                conn.setConnectTimeout(15000 /*milliseconds*/);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setFixedLengthStreamingMode(postDataLength);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8")
                );
                writer.write(requestString.toString());
                writer.flush();
                writer.close();
                os.close();
                //Starts the query
                conn.connect();
                response = conn.getResponseCode();
                //Log.d("146s","response: "+response);
                is = conn.getInputStream();

                //Convert the InputStream into a String
                String contentAsString = convertStreamToString(is);
                //Log.d("146s","content: "+contentAsString);
                System.out.print(contentAsString);
                //c=contentAsString;
                return contentAsString;
            } catch (IOException e) {
                System.out.print(e.getMessage());
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return "{\"device\":\"error\",\"command\":\"ERROR: response "+response+"\"}";
        }else{
            return "{\"device\":\"error\",\"command\":\"ERROR: could not load properties\"}";
        }
    }
    private static String convertStreamToString(InputStream is){
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
