package com.swiftdating.app.callbacks;

public interface AuthenticationListener {
    void onTokenReceived(String auth_token);
}