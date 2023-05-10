package com.example.peertopeerfeedbackapp;

public class Response {
    public String status;
    public String body;

    public String toString(){
        return( "Status: " + this.status + "\n" +
                "Body : {\n" +
                    this.body + "\n" +
                "  }");
    }
}
