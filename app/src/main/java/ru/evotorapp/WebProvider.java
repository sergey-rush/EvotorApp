package ru.evotorapp;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by sergey-rush on 28.12.2017.
 */

public class WebProvider extends WebAccess{

    int responseCode;

    public WebProvider(){}

    @Override
    public int getRegister(String imei) {
        HttpURLConnection connection = null;

        try {
            UrlObject urlObject = getUrl(UrlType.Register);
            URL url = new URL(String.format("%s?appId=%s", urlObject.Url, imei));
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod(urlObject.HttpMethod.toString());
            connection.setDoOutput(true);
            responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String output = deserializeToString(connection);
                Device = parseToDevice(output);
            } else {
                return responseCode;
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
        return responseCode;
    }

    public int postOrder(String postData) {
        HttpURLConnection connection = null;

        try {
            UrlObject urlObject = getUrl(UrlType.Order);
            URL url = new URL(urlObject.Url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod(urlObject.HttpMethod.toString());
            connection.setDoInput(true);
            connection.setDoOutput(true);

            serialisePost(connection, postData);
            responseCode = connection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String output = deserializeToString(connection);
                Order = parseToOrder(output);

            } else {
                return responseCode;
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
        return responseCode;
    }

    public int postCheckout(String postData) {
        HttpURLConnection connection = null;

        try {
            UrlObject urlObject = getUrl(UrlType.Checkout);
            URL url = new URL(urlObject.Url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod(urlObject.HttpMethod.toString());
            connection.setDoInput(true);
            connection.setDoOutput(true);

            serialisePost(connection, postData);
            responseCode = connection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String output = deserializeToString(connection);
                Application = parseToApplication(output);

            } else {
                return responseCode;
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
        return responseCode;
    }

    public int deleteDismiss(String postData) {
        HttpURLConnection connection = null;

        try {
            UrlObject urlObject = getUrl(UrlType.Dismiss);
            URL url = new URL(urlObject.Url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod(urlObject.HttpMethod.toString());
            connection.setDoInput(true);
            connection.setDoOutput(true);

            serialisePost(connection, postData);
            responseCode = connection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                //String output = deserializeToString(connection);
                //Order = parseToOrder(output);

            } else {
                return responseCode;
            }

        } catch (MalformedURLException mex) {
            mex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return responseCode;
    }

}
