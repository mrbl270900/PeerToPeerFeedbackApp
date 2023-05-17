package com.example.peertopeerfeedbackapp;

import java.util.List;

public class Post {

    private String owner;

    private int id;

    private String subject;

    private List<Comment> commentsList;


    public Post(String inputOwner, String inputSubject, int inputId){
        owner = inputOwner;
        id = inputId;
        subject = inputSubject;
    }

    public void addComment(String inputOwner, String inputContens){
        commentsList.add(new Comment(inputOwner, inputContens, commentsList.toArray().length));
    }


    private class Comment{
        private String owner;
        private String contens;
        private int id;

        public Comment(String inputOwner, String inputContens, int inputId){
            owner = inputOwner;
            contens = inputContens;
            id = inputId;
        }
    }


}
