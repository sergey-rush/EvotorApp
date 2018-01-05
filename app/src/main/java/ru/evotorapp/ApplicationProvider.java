package ru.evotorapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by sergey-rush on 21.11.2017.
 */

public class ApplicationProvider {

    public int responseCode = 0;

    public Application lookUp(String name) {
        HttpURLConnection connection = null;
Application item = null;
        try {
            String urlLink = String.format("https://localhost/api/evotor/lookup?name=%s", name);
            URL url = new URL(urlLink);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");

            responseCode = connection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String output = deserializeToString(connection);
                item = parseToApplication(output);
            }

        } catch (MalformedURLException mex) {
            mex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return item;
    }

    private Application parseToApplication(String input) throws JSONException, ParseException {
        Application item = new Application();
        JSONObject resultData = new JSONObject(input);
        item.Name = resultData.getString("Name");
        item.Info = resultData.getString("Info");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        item.Created = format.parse(resultData.getString("Created"));
        return item;
    }

    private String deserializeToString(HttpURLConnection connection) throws IOException {
        InputStream stream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        reader.close();

        String output = buffer.toString();
        return output;
    }
}

