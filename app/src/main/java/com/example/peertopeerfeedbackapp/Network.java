package com.example.peertopeerfeedbackapp;

import java.util.List;

public class Network {
    private List<Post> postList;

    private String networkCode;

    private Network(String inputNetworkCode){
        networkCode = inputNetworkCode;
    }

    private void addPost(Post inputPost){
        postList.add(inputPost);
    }
}
