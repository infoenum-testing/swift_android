package com.swift.dating.callbacks;

public interface AuthenticationListener {
    void onTokenReceived(String auth_token);
}