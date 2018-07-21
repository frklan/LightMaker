package com.yellowfortyfour.spigot.lightmaker.api;

import java.lang.StringBuilder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.SSLHandshakeException;
import java.net.ConnectException;

public final class RestApi 
{
  private static String apiUrl = "";
  private static String jwtToken = "";

  public static void setBaseApi(String url)
  {
    apiUrl = url;
  }

  public static void setJwtToken(String token) 
  {
    jwtToken = token;
  }

  public static void BulbOn(String bulbId) throws Exception
  {
      String api = String.format("%s/bulb/%s/on", apiUrl, bulbId);
      sendCommand(api);
  }

  public static void BulbOff(String bulbId) throws Exception
  {
      String api = String.format("%s/bulb/%s/off", apiUrl, bulbId);
      sendCommand(api); 
  }

  public static void BulbToggle(String bulbId) throws Exception
  {
      String api = String.format("%s/bulb/%s/toggle", apiUrl, bulbId);
      sendCommand(api);
  }
  private static void sendCommand(String api) throws Exception
  {
    StringBuilder response = new StringBuilder();
    HttpURLConnection conn = null;

    try {  
      URL url = new URL(api);
          
      // We should be using HttpsURLConnection here, however casting to a HttpURLConnection
      // allows us to supply either a https.. or http.. URL and automagically select
      // SSL or not. 
      // Not sure if this makes it less secure (when we do use SSL).
      conn = (HttpURLConnection) url.openConnection();

      conn.setRequestMethod("PUT");
      conn.addRequestProperty("Accept", "application/json");
      conn.setRequestProperty("token", jwtToken);
      
      int httpCode = conn.getResponseCode();
      if(httpCode == 401) {
        throw new RuntimeException("we are not authorized to use the API endpoint, is the API-token valid?");
      } else if (httpCode != 200) {
        throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
      }

      BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
      String line = "";
      while ((line = br.readLine()) != null) {
        response.append(line);
      }
      
    } catch(SSLHandshakeException e) {
      throw new RuntimeException("Unable to establish a SSL connection, do we have SSL enabled on the server?");
    } catch(ConnectException e) {
      throw new RuntimeException("Unable to connect to " + api + ", is the server running?");
    } catch(Exception e) {
      throw new RuntimeException(e.getMessage());
    } finally {
      conn.disconnect();
    }
      return;
    }
}