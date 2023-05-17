package com.example.peertopeerfeedbackapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class HandleApi {

    public static Request readHttpRequest(String input) throws RuntimeException{
        try {
            JSONObject json = new JSONObject(input);
            Request request = new Request(json.getString("method"), json.getString("body"));
            return request;

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

    /*public static List<Post> readPostList(Response input){
        try {
            List<Post> postList;
            String[] bodyList;

            bodyList = input.body.split("posts");

            for (int i = 0; i < bodyList.length; i++) {
                postList.add();
            }

            return postList;

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }*/

}
