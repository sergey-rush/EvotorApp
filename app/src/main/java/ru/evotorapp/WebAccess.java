package ru.evotorapp;

import android.content.Context;

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
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sergey-rush on 28.12.2017.
 */

public abstract class WebAccess {

    public AppMode Mode = AppMode.Develop;
    private Map<AppMode, Map<UrlType, UrlObject>> urlMap;

    public Device Device;
    public Order Order;
    public Application Application;

    private static WebAccess instance = null;

    public static WebAccess getInstance() {
        if (instance == null) {
            instance = new WebProvider();
        }
        return instance;
    }

    public WebAccess(){
        initUrls();
    }

    public abstract int getRegister(String imei);
    public abstract int postOrder(String postData);
    public abstract int postCheckout(String postData);
    public abstract int deleteDismiss(String postData);

    protected void serialisePost(HttpURLConnection connection, String postData) throws IOException {
        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(postData);
        writer.flush();
        writer.close();
        os.close();
    }

    protected String deserializeToString(HttpURLConnection connection) throws IOException {
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

    protected Device parseToDevice(String input) throws JSONException, ParseException {

        Device device = new Device();
        JSONObject resultData = new JSONObject(input);
        device.IsActive = resultData.getBoolean("IsActive");
        device.MaxAmount = resultData.getInt("MaxAmount");
        device.MinAmount = resultData.getInt("MinAmount");
        return device;
    }

    protected Order parseToOrder(String input) throws JSONException, ParseException {

        Device device = new Device();
        JSONObject resultData = new JSONObject(input);
        device.IsActive = resultData.getBoolean("IsActive");
        device.MaxAmount = resultData.getInt("MaxAmount");
        device.MinAmount = resultData.getInt("MinAmount");
        return new Order();
    }

    protected Application parseToApplication(String input) throws JSONException, ParseException {

        Device device = new Device();
        JSONObject resultData = new JSONObject(input);
        device.IsActive = resultData.getBoolean("IsActive");
        device.MaxAmount = resultData.getInt("MaxAmount");
        device.MinAmount = resultData.getInt("MinAmount");
        return new Application();
    }

    private void initUrls()
    {
        urlMap = new HashMap<AppMode, Map<UrlType, UrlObject>>();

        Map<UrlType, UrlObject> developMap = new HashMap<UrlType, UrlObject>();
        developMap.put(UrlType.Register, new UrlObject(HttpMethod.GET, "https://localhost/api/Evotor/Register"));
        developMap.put(UrlType.Order, new UrlObject(HttpMethod.POST, "https://localhost/api/Evotor/Order"));
        developMap.put(UrlType.Order, new UrlObject(HttpMethod.POST, "https://localhost/api/Evotor/Checkout"));
        developMap.put(UrlType.Order, new UrlObject(HttpMethod.DELETE, "https://localhost/api/Evotor/Dismiss"));

        urlMap.put(AppMode.Develop, developMap);

        Map<UrlType, UrlObject> testMap = new HashMap<UrlType, UrlObject>();
        testMap.put(UrlType.Register, new UrlObject(HttpMethod.GET, "https://localhost/api/Evotor/Register"));
        developMap.put(UrlType.Order, new UrlObject(HttpMethod.POST, "https://localhost/api/Evotor/Order"));
        developMap.put(UrlType.Order, new UrlObject(HttpMethod.POST, "https://localhost/api/Evotor/Checkout"));
        developMap.put(UrlType.Order, new UrlObject(HttpMethod.DELETE, "https://localhost/api/Evotor/Dismiss"));

        urlMap.put(AppMode.Test, testMap);

        Map<UrlType, UrlObject> prodMap = new HashMap<UrlType, UrlObject>();
        prodMap.put(UrlType.Register, new UrlObject(HttpMethod.GET, "https://localhost/api/Evotor/Register"));
        developMap.put(UrlType.Order, new UrlObject(HttpMethod.POST, "https://localhost/api/Evotor/Order"));
        developMap.put(UrlType.Order, new UrlObject(HttpMethod.POST, "https://localhost/api/Evotor/Checkout"));
        developMap.put(UrlType.Order, new UrlObject(HttpMethod.DELETE, "https://localhost/api/Evotor/Dismiss"));

        urlMap.put(AppMode.Product, prodMap);
    }

    public UrlObject getUrl(UrlType urlType) {
        return urlMap.get(Mode).get(urlType);
    }


}
