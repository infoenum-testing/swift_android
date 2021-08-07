package com.swiftdating.app.websocket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WebSocketBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(WebSocketBroadcastReceiver.class.getSimpleName(), "Web Socket Closed");

        if (Helper.isAppRunning(context, "app.blackgentry") &&
                !WebSocketService.getInstance().isIs_opened()) {
            WebSocketService.getInstance().connectWebSocket(context, true);
        }
    }
}