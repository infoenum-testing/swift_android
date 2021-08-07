package com.swiftdating.app.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NotificationModel implements Serializable {
    @SerializedName("image")
    @Expose
    private ImageModel image;
    @SerializedName("match")
    @Expose
    private Match match;
    @SerializedName("action")
    @Expose
    private Integer action;
    @SerializedName("message")
    @Expose
    private String message;


    @SerializedName("ProfilePic")
    @Expose
    private String profilePic;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName(value = "userId", alternate = "fromid")
    @Expose
    private Integer userId;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("isDestroyed")
    @Expose
    private boolean isDestroyed;
    @SerializedName("isDenied")
    @Expose
    private boolean isDenied;
    @SerializedName("isDeactivated")
    @Expose
    private boolean isDeactivated;
    @SerializedName("approval")
    @Expose
    private boolean approval;
    @SerializedName("pushmessage")
    @Expose
    private boolean pushmessage;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public ImageModel getImage() {
        return image;
    }

    public void setImage(ImageModel image) {
        this.image = image;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public boolean getIsDestroyed() {
        return isDestroyed;
    }

    public boolean getisDeactivated() {
        return isDeactivated;
    }

    public boolean getisDenied() {
        return isDenied;
    }

    public boolean getApproval() {
        return approval;
    }

    public boolean getPushmessage() {
        return pushmessage;
    }

    public class Match implements Serializable {

        @SerializedName("education")
        @Expose
        private String education;
        @SerializedName("occupation")
        @Expose
        private String occupation;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("ethnicity")
        @Expose
        private String ethnicity;
        @SerializedName("distance")
        @Expose
        private Integer distance;
        @SerializedName("matchUpdates")
        @Expose
        private String matchUpdates;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("showmeto")
        @Expose
        private Object showmeto;
        @SerializedName("Drink")
        @Expose
        private String drink;
        @SerializedName("Smoke")
        @Expose
        private String smoke;
        @SerializedName("Relegion")
        @Expose
        private String relegion;
        @SerializedName("matchNotify")
        @Expose
        private String matchNotify;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("Exercise")
        @Expose
        private String exercise;
        @SerializedName("school")
        @Expose
        private String school;
        @SerializedName("callReminder")
        @Expose
        private String callReminder;
        @SerializedName("Question3")
        @Expose
        private String question3;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("ZodiacSign")
        @Expose
        private String zodiacSign;
        @SerializedName("Question2")
        @Expose
        private String question2;
        @SerializedName("Question1")
        @Expose
        private String question1;
        @SerializedName("height")
        @Expose
        private String height;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("visible")
        @Expose
        private String visible;
        @SerializedName("City")
        @Expose
        private String city;
        @SerializedName("maxAgePrefer")
        @Expose
        private Integer maxAgePrefer;
        @SerializedName("completed")
        @Expose
        private Integer completed;
        @SerializedName("superLikesCount")
        @Expose
        private Integer superLikesCount;
        @SerializedName("userId")
        @Expose
        private Integer userId;
        @SerializedName("Kids")
        @Expose
        private String kids;
        @SerializedName("aboutme")
        @Expose
        private String aboutme;
        @SerializedName("deletedAt")
        @Expose
        private String deletedAt;
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("minAgePrefer")
        @Expose
        private Integer minAgePrefer;
        @SerializedName("interested")
        @Expose
        private String interested;
        @SerializedName("Political")
        @Expose
        private String political;
        @SerializedName("expiredMatches")
        @Expose
        private String expiredMatches;


        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        public String getOccupation() {
            return occupation;
        }

        public void setOccupation(String occupation) {
            this.occupation = occupation;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getEthnicity() {
            return ethnicity;
        }

        public void setEthnicity(String ethnicity) {
            this.ethnicity = ethnicity;
        }

        public Integer getDistance() {
            return distance;
        }

        public void setDistance(Integer distance) {
            this.distance = distance;
        }

        public String getMatchUpdates() {
            return matchUpdates;
        }

        public void setMatchUpdates(String matchUpdates) {
            this.matchUpdates = matchUpdates;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public Object getShowmeto() {
            return showmeto;
        }

        public void setShowmeto(Object showmeto) {
            this.showmeto = showmeto;
        }

        public String getDrink() {
            return drink;
        }

        public void setDrink(String drink) {
            this.drink = drink;
        }

        public String getSmoke() {
            return smoke;
        }

        public void setSmoke(String smoke) {
            this.smoke = smoke;
        }

        public String getRelegion() {
            return relegion;
        }

        public void setRelegion(String relegion) {
            this.relegion = relegion;
        }

        public String getMatchNotify() {
            return matchNotify;
        }

        public void setMatchNotify(String matchNotify) {
            this.matchNotify = matchNotify;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getExercise() {
            return exercise;
        }

        public void setExercise(String exercise) {
            this.exercise = exercise;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public String getCallReminder() {
            return callReminder;
        }

        public void setCallReminder(String callReminder) {
            this.callReminder = callReminder;
        }

        public String getQuestion3() {
            return question3;
        }

        public void setQuestion3(String question3) {
            this.question3 = question3;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getZodiacSign() {
            return zodiacSign;
        }

        public void setZodiacSign(String zodiacSign) {
            this.zodiacSign = zodiacSign;
        }

        public String getQuestion2() {
            return question2;
        }

        public void setQuestion2(String question2) {
            this.question2 = question2;
        }

        public String getQuestion1() {
            return question1;
        }

        public void setQuestion1(String question1) {
            this.question1 = question1;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getVisible() {
            return visible;
        }

        public void setVisible(String visible) {
            this.visible = visible;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public Integer getMaxAgePrefer() {
            return maxAgePrefer;
        }

        public void setMaxAgePrefer(Integer maxAgePrefer) {
            this.maxAgePrefer = maxAgePrefer;
        }

        public Integer getCompleted() {
            return completed;
        }

        public void setCompleted(Integer completed) {
            this.completed = completed;
        }

        public Integer getSuperLikesCount() {
            return superLikesCount;
        }

        public void setSuperLikesCount(Integer superLikesCount) {
            this.superLikesCount = superLikesCount;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getKids() {
            return kids;
        }

        public void setKids(String kids) {
            this.kids = kids;
        }

        public String getAboutme() {
            return aboutme;
        }

        public void setAboutme(String aboutme) {
            this.aboutme = aboutme;
        }

        public String getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(String deletedAt) {
            this.deletedAt = deletedAt;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getMinAgePrefer() {
            return minAgePrefer;
        }

        public void setMinAgePrefer(Integer minAgePrefer) {
            this.minAgePrefer = minAgePrefer;
        }

        public String getInterested() {
            return interested;
        }

        public void setInterested(String interested) {
            this.interested = interested;
        }

        public String getPolitical() {
            return political;
        }

        public void setPolitical(String political) {
            this.political = political;
        }

        public String getExpiredMatches() {
            return expiredMatches;
        }

        public void setExpiredMatches(String expiredMatches) {
            this.expiredMatches = expiredMatches;
        }
    }


}