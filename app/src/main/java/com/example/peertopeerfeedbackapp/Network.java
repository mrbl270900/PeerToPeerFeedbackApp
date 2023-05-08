package com.example.peertopeerfeedbackapp;

import java.util.List;

public class Network {
    private List<Post> postList;

    private List<String> peerList;

    private String networkCode;

    public Network(String inputNetworkCode){
        networkCode = inputNetworkCode;
    }

    public void addPost(Post inputPost){
        postList.add(inputPost);
    }

    public void addPeer(String inputIp){peerList.add(inputIp);}
}
