package com.example.peertopeerfeedbackapp;

public class Request {
    public String method;
    public String body;

    public Request(String inputMethod, String inputBody){
        method = inputMethod;
        body = inputBody;
    }
    public String toString(){
        return( "Method: " + this.method + "\n" +
                "Body: {\n" +
                    this.body + "\n" +
                "  }");
    }
}
