package com.swift.dating.model.requestmodel;

public class SuperLikeCountModel {
    private int superlikeTokens;
    private double price;

    public SuperLikeCountModel(int superLikesCount,double price) {
        this.superlikeTokens = superLikesCount;
        this.price = price;

    }
}
