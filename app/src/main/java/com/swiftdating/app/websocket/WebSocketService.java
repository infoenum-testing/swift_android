package com.swiftdating.app.websocket;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.swiftdating.app.R;
import com.swiftdating.app.ui.chatScreen.ChatWindow;
import com.swiftdating.app.common.CommonUtils;
import com.swiftdating.app.data.network.CallServer;
import com.swiftdating.app.data.preference.SharedPreference;
import com.swiftdating.app.model.ChatModel;
import com.swiftdating.app.model.ImageModel;
import com.swiftdating.app.model.responsemodel.ProfileOfUser;

public class WebSocketService {

    private Context mContext;
    private SharedPreference sharedPref;
    private WebSocketClient mWebSocketClient;
    private Intent notificationIntent;

    private boolean is_opened = false;

    @SuppressLint("StaticFieldLeak")
    private static WebSocketService mInstance = null;

    private SocketStatusCallback sessionStartCallback;

    private WebSocketService() {
    }

    public static WebSocketService getInstance() {
        if (mInstance == null) {
            mInstance = new WebSocketService();
        }
        return mInstance;
    }

    public boolean isIs_opened() {
        return is_opened;
    }


    public interface SocketStatusCallback {
        void onResponse(boolean opened);
    }

    private void on_socket_opened(boolean hasStarted) {
        if (sessionStartCallback == null) {
            return;
        }
        sessionStartCallback.onResponse(hasStarted);
    }

    public void connectWebSocket(final Context context, boolean shouldRestart) {
        mContext = context;
        sharedPref = new SharedPreference(mContext);
        if(sharedPref.isloggedIn().equalsIgnoreCase("true")) {


            if (shouldRestart) {
                if (mWebSocketClient != null) {
                    mWebSocketClient.close();
                    mWebSocketClient = null;
                }
                is_opened = false;
            }

            if (mWebSocketClient != null && is_opened) {
                on_socket_opened(true);
                return;
            }

            if (mWebSocketClient != null) {
                mWebSocketClient.close();
                mWebSocketClient = null;
            }
            is_opened = false;

            URI uri;
            try {

                uri = new URI(CallServer.SocketUrl);

                mWebSocketClient = new WebSocketClient(uri) {

                    @Override
                    public void onOpen(ServerHandshake serverHandshake) {
                        Log.e("Web socket", "Opened");
                        // session started
                        is_opened = true;
                        on_socket_opened(true);

                        setWebSocketHitConnect(mContext);
                    }

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onMessage(String s) {

                        try {
                            Log.e("Socket_Message_Received", s);

                            JSONObject response = new JSONObject(s);

                            String command = response.getString("command");

                            if (command.equalsIgnoreCase("message")) {

                            String fromid = response.getJSONObject("data").getString("fromid");
                            String fromname = response.getJSONObject("data").getString("fromname");
                            String frompic = response.getJSONObject("data").getString("frompic");
                            String chattime = response.getJSONObject("data").getString("chattime");
                            String toid = response.getJSONObject("data").getString("to_id");
                            String msg = response.getJSONObject("data").getString("msg");

                                String chatStatus = "";
                                if (toid.equalsIgnoreCase(sharedPref.getUserId())) {
                                    if (sharedPref.isChatOpen()) {
                                        if (sharedPref.getUserId().equalsIgnoreCase(fromid)) {
                                            ////////////////////////////////////////////////
                                        } else {// for single chat
                                            if (sharedPref.getChatUserId().equalsIgnoreCase(fromid)) {
                                                ChatModel chatModel = new ChatModel(fromid, toid, msg,chattime);
                                                mContext.sendBroadcast(new Intent("BGChat")
                                                        .putExtra("chatModel", chatModel)
                                                        .putExtra("action", "0"));
                                            } else {
                                                ChatModel messageDetail = new ChatModel(fromid, fromname, frompic,chattime);
                                                notificationIntent = new Intent(mContext, ChatWindow.class)
                                                        .putExtra("messageDetail", messageDetail);
//
                                            }
                                        }
                                    } else {
                                        if (!fromid.equalsIgnoreCase(sharedPref.getUserId())) {
                                            ChatModel messageDetail = new ChatModel(fromid, fromname, frompic,chattime);
                                            notificationIntent = new Intent(mContext, ChatWindow.class)
                                                    .putExtra("messageDetail", messageDetail);
                                            ChatModel chatModel = new ChatModel(fromid, toid, msg,chattime);
                                            mContext.sendBroadcast(new Intent("BGChat")
                                                    .putExtra("chatModel", chatModel)
                                                    .putExtra("action", "0"));
                                        }
                                    }
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onClose(int i, String s, boolean b) {
                        Log.e("Web socket", "Closed " + s);
                        is_opened = false;
                        on_socket_opened(false);
                        if (sharedPref.getUserId() != null)
                            connectWebSocket(context, true);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("Web socket", "Error " + e.getMessage());
                        is_opened = false;
                        on_socket_opened(false);
                    }
                };

                mWebSocketClient.connect();
                mWebSocketClient.setConnectionLostTimeout(0);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * method to connect user to socket
     * @param context
     */
    private void setWebSocketHitConnect(Context context) {

        try {

            if (!is_opened) {
                showErrorToast(context);
                WebSocketService.getInstance().connectWebSocket(context, true);
            }

            JSONObject params = new JSONObject();
            params.put("command", "connect");

            JSONObject innerParams = new JSONObject();
            innerParams.put("id", sharedPref.getUserId());
            params.put("data", innerParams);

            if (mWebSocketClient != null && mWebSocketClient.isOpen()) {
                mWebSocketClient.send(params.toString());
                Log.e("SOCKET CONNECTED ==", params.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * socket method to send message to any user
     * @param context
     * @param fromid
     * @param toid
     * @param message
     */
    public void sendMessage(Context context, String fromid, String toid, String message,String frompic,String fromname) {

        try {

            if (!is_opened) {
                showErrorToast(context);
                WebSocketService.getInstance().connectWebSocket(context, true);
                return;
            }
            String gmtTime = CommonUtils.getUTCdatetimeAsDate();


            /* send broadcast to yourself */
                ChatModel chatModel = new ChatModel(fromid, toid, message,frompic,fromname,0,gmtTime);
                mContext.sendBroadcast(new Intent("BGChat").putExtra("chatModel", chatModel)
                        .putExtra("action", "0"));

            Gson gson = new Gson();
            String jsonImage = sharedPref.getUserImage();
            Type type = new TypeToken<List<ImageModel>>() {
            }.getType();
            ArrayList<ImageModel> imagelist = gson.fromJson(jsonImage, type);

            JSONObject params = new JSONObject();
            params.put("command", "message");
            ProfileOfUser obj = new Gson().fromJson(sharedPref.getUser(), ProfileOfUser.class);
            JSONObject innerParams = new JSONObject();
            innerParams.put("fromid", fromid);
            innerParams.put("to_id", toid);
            innerParams.put("msg",message);
            innerParams.put("fromname", obj.getName());
            innerParams.put("frompic",CallServer.BaseImage + imagelist.get(0).getImageUrl());
            innerParams.put("chattime",gmtTime);
            params.put("data", innerParams);

            if (mWebSocketClient != null && mWebSocketClient.isOpen()) {
                mWebSocketClient.send(params.toString());
                Log.e("SOCKET SEND MESSAGE == ", params.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void sendClose(){
        try {
            JSONObject params = new JSONObject();
            params.put("command", "close");
            if (mWebSocketClient != null && mWebSocketClient.isOpen()) {
                mWebSocketClient.send(params.toString());
                Log.e("SOCKET SEND MESSAGE == ", params.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * socket method to read message sent by user
     * @param context
     * @param fromid
     * @param toid
     */
    private void readMessage(Context context, String fromid, String toid) {
        try {

            if (!is_opened) {
                showErrorToast(context);
                WebSocketService.getInstance().connectWebSocket(context, true);
                return;
            }

            JSONObject params = new JSONObject();
            params.put("command", "onReadMessage");

            JSONObject innerParams = new JSONObject();
            innerParams.put("fromid", fromid);
            innerParams.put("to_id", toid);
            params.put("data", innerParams);

            if (mWebSocketClient != null && mWebSocketClient.isOpen()) {
                mWebSocketClient.send(params.toString());
                Log.e("SOCKET READ MESSAGE == ", params.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * socket method to enter chat room
     * @param context
     * @param roomid
     * @param userid
     * @param enter
     */
    public void enterChatRoom(Context context, String roomid, String userid, boolean enter) {

        try {

            if (!is_opened) {
                showErrorToast(context);
                WebSocketService.getInstance().connectWebSocket(context, true);
                return;
            }

            JSONObject params = new JSONObject();
            params.put("command", enter ? "onlineGroup" : "offlineGroup");

            JSONObject innerParams = new JSONObject();
            innerParams.put("groupid", roomid);
            innerParams.put("fromid", userid);
            params.put("data", innerParams);

            if (mWebSocketClient != null && mWebSocketClient.isOpen()) {
                mWebSocketClient.send(params.toString());
                Log.e("SOCKET CHAT ROOM == ", params.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * generate local push notification from web socket message received
     * @param message
     * @param fromName
     * @param fromPic
     */
    private void generateNotification(String message, String fromName, String fromPic) {
        Bitmap bitmap;
        if (fromPic != null && !fromPic.isEmpty())
            bitmap = CommonUtils.getBitmapFromUrl(fromPic, 1);
        else
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher_foreground);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(mContext, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        int pushIcon = R.mipmap.ic_launcher_round;
        int notificationId = 1;
        String channelId = mContext.getString(R.string.notification_channel_id);
        String channelName = mContext.getString(R.string.notification_channel_name);;

        NotificationManager mNotificationManager = (NotificationManager) mContext.
                getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(mChannel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, channelId);
        builder.setContentIntent(intent)
                .setContentTitle(fromName)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSmallIcon(pushIcon)
                .setLargeIcon(bitmap)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setOngoing(false);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        if (mNotificationManager != null) {
            mNotificationManager.notify(notificationId, notification);
        }
    }

    /**
     * show toast for socket error
     * @param context
     */
    private void showErrorToast(Context context) {
        Toast.makeText(context, context.getString(R.string.socket_not_connected_text), Toast.LENGTH_LONG).show();
    }
}