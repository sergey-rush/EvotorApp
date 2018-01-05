package ru.evotorapp;

/**
 * Created by sergey-rush on 28.12.2017.
 */

public class UrlObject {

    public HttpMethod HttpMethod;
    public String Url;

    public UrlObject(HttpMethod httpMethod, String url) {
        HttpMethod = httpMethod;
        Url = url;
    }
}