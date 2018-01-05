package ru.evotorapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by sergey-rush on 17.11.2017.
 */

public class OrderProvider {

    public int responseCode = 0;

    public String postOrder(String postData) {

        String applicationGuid = null;

        HttpURLConnection connection = null;

        try {
            String urlLink = "https://localhost/api/merch/order";
            URL url = new URL(urlLink);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            serialisePost(connection, postData);
            responseCode = connection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String output = deserializeToString(connection);
                applicationGuid = parseToApplicationGuid(output);
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
        return applicationGuid;
    }

    private String parseToApplicationGuid(String input) throws JSONException, ParseException {

        JSONObject resultData = new JSONObject(input);
        String applicationGuid = resultData.getString("application_id");
        String result = resultData.getString("result");
        return applicationGuid;
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

    private void serialisePost(HttpURLConnection connection, String postData) throws IOException {
        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(postData);
        writer.flush();
        writer.close();
        os.close();
    }
}

