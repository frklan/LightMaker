package com.yellowfortyfour.spigot.lightmaker;

import java.lang.StringBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public final class RestApi 
{
  private static String apiUrl = "http://localhost:5000/api/v1";

  public static void setBaseApi(String url)
  {
    apiUrl = url;
  }
  public static void BulbOn(String bulbId)
  {
      String api = String.format("%s/bulb/%s/on", apiUrl, bulbId);
      sendCommand(api);
  }

  public static void BulbOff(String bulbId)
  {
      String api = String.format("%s/bulb/%s/off", apiUrl, bulbId);
      sendCommand(api); 
  }

  private static String sendCommand(String api)
  {
    StringBuilder response = new StringBuilder();
    try {  
      URL url = new URL(api);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("PUT");
      conn.setRequestProperty("Accept", "application/json");

      if (conn.getResponseCode() != 200) {
        throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
      }

      BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
      String line = "";
      while ((line = br.readLine()) != null) {
        response.append(line);
      }

      conn.disconnect();
      
      } catch(Exception e) {
        //e.printStackTrace();
        return e.getMessage();
      }
      return response.toString();
    }
}
