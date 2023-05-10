package com.example.peertopeerfeedbackapp;

import org.json.JSONException;
import org.json.JSONObject;

public class HandleApi {

    public static Request readHttpRequest(String input) throws RuntimeException{
        try {
            JSONObject json = new JSONObject(input);
            Request request = new Request();
            request.method = json.getString("method");
            request.path = json.getString("path");
            request.body = json.getString("body");
            return request;

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static String createHttpRequest(String method, String path, String body) throws RuntimeException{
        try {
            JSONObject json = new JSONObject();
            Request request = new Request();
            request.method = method;
            request.path = path;
            request.body = body;
            json.put("header", "HTTP/1.1");
            json.put("path", request.path);
            json.put("method", request.method);
            json.put("body", request.body);

            return json.toString();

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    public static String createHttpResponse(String body, String status) throws RuntimeException{
        Response response = new Response();
        response.status = status;
        response.body = body;

        try {
            JSONObject json = new JSONObject();
            json.put("header", "HTTP/1.1");
            json.put("status", response.status);
            json.put("body", response.body);
            return json.toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static Response readHttpResponse(String input) throws RuntimeException{
        try {
            JSONObject json = new JSONObject(input);
            Response response = new Response();
            response.status = json.getString("status");
            response.body = json.getString("body");
            return response;

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}
