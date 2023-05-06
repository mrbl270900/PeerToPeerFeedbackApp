package com.example.peertopeerfeedbackapp;

import java.util.List;

public class Post {

    private String owner;

    private int id;

    private String Subject;

    private List<Comment> commentsList;


    public Post(){

    }

    public void addComment(){

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
