package com.swift.dating.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChatModel implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

//    @SerializedName("group_id")
//    @Expose
//    private String group_id;

    @SerializedName("from_id")
    @Expose
    private String from_id;

    @SerializedName("to_id")
    @Expose
    private String to_id;

    @SerializedName(value = "msg",alternate = {"message"})
    @Expose
    private String message;

    @SerializedName("profile")
    @Expose
    private String profile;

    @SerializedName(value = "date_created",alternate = {"createdAt","chatTime"})
    @Expose
    private String date_created;


    public String getId() {
        return id;
    }

    public String getFrom_id() {
        return from_id;
    }

    public String getTo_id() {
        return to_id;
    }

    public String getMessage() {
        return message;
    }

    public String getProfile() {
        return profile;
    }



    public String getDate_created() {
        return date_created;
    }


    public ChatModel( String from_id, String to_id, String message,String frompic,String name,int typ,String chatTime) {
        this.from_id = from_id;
        this.to_id = to_id;
        this.message = message;
        this.image = frompic;
        this.name = name;
        this.date_created = chatTime;
    }


    public ChatModel( String from_id, String to_id, String message,String chatTime ) {
        this.from_id = from_id;
        this.to_id = to_id;
        this.message = message;
        this.date_created = chatTime;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /* data related to data */

    @SerializedName("date_id")
    @Expose
    private String date_id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("datetime")
    @Expose
    private String datetime;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("lat")
    @Expose
    private String lat;

    @SerializedName("lng")
    @Expose
    private String lng;

    @SerializedName("datestatus")
    @Expose
    private String datestatus;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("place")
    @Expose
    private String place;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("datemessage")
    @Expose
    private String datemessage;

    private String date_id_old; // only used for fcm push for date modify


    public String getDate_id() {
        return date_id;
    }

    public String getTitle() {
        return title;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getLocation() {
        return location;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getDateStatus() {
        return datestatus;
    }

    public String getDatemessage() {
        return datemessage;
    }

    public void setDatemessage(String datemessage) {
        this.datemessage = datemessage;
    }

    public String getName() {
        return name;
    }

    public String getPlace() {
        return place;
    }

    public String getImage() {
        return image;
    }

    public void setDatestatus(String datestatus) {
        this.datestatus = datestatus;
    }

    public String getDate_id_old() {
        return date_id_old;
    }

    public void setDate_id_old(String date_id_old) {
        this.date_id_old = date_id_old;
    }


    public ChatModel(String fromid, String toid, String date_id, String title, String datetime,
                     String datestatus, String datemessage, String place, String image, String location,
                     String lat, String lng) {
        this.from_id = fromid;
        this.to_id = toid;
        this.date_id = date_id;
        this.title = title;
        this.datetime = datetime;
        this.datestatus = datestatus;
        this.datemessage = datemessage;
        this.location = location;
        this.place = place;
        this.image = image;
        this.lat = lat;
        this.lng = lng;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////

    /* data related to photo request */

    /* data related to data */

    @SerializedName("permission_id")
    @Expose
    private String permission_id;

    @SerializedName("permission_status")
    @Expose
    private String permission_status;

    public String getPermission_id() {
        return permission_id;
    }

    public String getPermission_status() {
        return permission_status;
    }

    public void setPermission_status(String permission_status) {
        this.permission_status = permission_status;
    }

    public void setPermission_id(String permission_id) {
        this.permission_id = permission_id;
    }

    @Override
    public String toString() {
        return "ChatModel{" +
                "id='" + id + '\'' +
                ", from_id='" + from_id + '\'' +
                ", to_id='" + to_id + '\'' +
                ", message='" + message + '\'' +
                ", profile='" + profile + '\'' +
                ", date_created='" + date_created + '\'' +
                ", date_id='" + date_id + '\'' +
                ", title='" + title + '\'' +
                ", datetime='" + datetime + '\'' +
                ", location='" + location + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", datestatus='" + datestatus + '\'' +
                ", name='" + name + '\'' +
                ", place='" + place + '\'' +
                ", image='" + image + '\'' +
                ", datemessage='" + datemessage + '\'' +
                ", date_id_old='" + date_id_old + '\'' +
                ", permission_id='" + permission_id + '\'' +
                ", permission_status='" + permission_status + '\'' +
                '}';
    }
}
