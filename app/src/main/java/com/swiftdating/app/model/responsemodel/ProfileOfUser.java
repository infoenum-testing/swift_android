package com.swiftdating.app.model.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileOfUser {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("distanceFromMatch")
    @Expose
    private Double distanceFromMatch;
    @SerializedName("aboutme")
    @Expose
    private String aboutme;
    @SerializedName("ambitions")
    @Expose
    private String ambitions;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("showmeto")
    @Expose
    private String showmeto;
    @SerializedName("interested")
    @Expose
    private String interested;
    @SerializedName("lookingFor")
    @Expose
    private String lookingFor;
    @SerializedName("pets")
    @Expose
    private String pets;
    @SerializedName("ethnicity")
    @Expose
    private String ethnicity;
    @SerializedName("Kids")
    @Expose
    private String kids;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("ZodiacSign")
    @Expose
    private String zodiacSign;
    @SerializedName("education")
    @Expose
    private String education;
    @SerializedName("school")
    @Expose
    private String school;
    @SerializedName("occupation")
    @Expose
    private String occupation;
    @SerializedName("Relegion")
    @Expose
    private String relegion;
    @SerializedName("Political")
    @Expose
    private String political;
    @SerializedName("Drink")
    @Expose
    private String drink;
    @SerializedName("Smoke")
    @Expose
    private String smoke;
    @SerializedName("Exercise")
    @Expose
    private String exercise;
    @SerializedName("City")
    @Expose
    private String city;
    @SerializedName("Country")
    @Expose
    private String Country;
    @SerializedName("State")
    @Expose
    private String State;
    @SerializedName("Question1")
    @Expose
    private String question1;
    @SerializedName("Question2")
    @Expose
    private String question2;
    @SerializedName("Question3")
    @Expose
    private String question3;
    @SerializedName("Answer1")
    @Expose
    private String answer1;
    @SerializedName("Answer2")
    @Expose
    private String answer2;
    @SerializedName("Answer3")
    @Expose
    private String answer3;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("maxAgePrefer")
    @Expose
    private Integer maxAgePrefer;
    @SerializedName("minAgePrefer")
    @Expose
    private Integer minAgePrefer;
    @SerializedName("visible")
    @Expose
    private String visible;
    @SerializedName("expiredMatches")
    @Expose
    private String expiredMatches;
    @SerializedName("chatNotify")
    @Expose
    private String chatNotify;
    @SerializedName("emailNotify")
    @Expose
    private String emailNotify;
    @SerializedName("reactionNotify")
    @Expose
    private String reactionNotify;
    @SerializedName("matchNotify")
    @Expose
    private String matchNotify;
    @SerializedName("completed")
    @Expose
    private Double completed;
    @SerializedName("superLikesCount")
    @Expose
    private Integer superLikesCount;
    @SerializedName("timeToken")
    @Expose
    private Integer timeTokenCount;
    @SerializedName("directMessageCount")
    @Expose
    private Integer directMessageCount;
    @SerializedName("deletedAt")
    @Expose
    private String deletedAt;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("useremail")
    @Expose
    private String useremail;

    @SerializedName("vipToken")
    @Expose
    private int vipToken;

    public String getReactionNotify() {
        return reactionNotify;
    }

    public void setReactionNotify(String reactionNotify) {
        this.reactionNotify = reactionNotify;
    }

    public Integer getDirectMessageCount() {
        return directMessageCount;
    }

    public void setDirectMessageCount(Integer directMessageCount) {
        this.directMessageCount = directMessageCount;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getEmailNotify() {
        return emailNotify;
    }

    public void setEmailNotify(String emailNotify) {
        this.emailNotify = emailNotify;
    }

    public int getVipToken() {
        return vipToken;
    }

    public void setVipToken(int vipToken) {
        this.vipToken = vipToken;
    }

    public ProfileOfUser(String name) {
        this.name = name;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

    public Double getDistanceFromMatch() {
        return distanceFromMatch;
    }

    public void setDistanceFromMatch(Double distanceFromMatch) {
        this.distanceFromMatch = distanceFromMatch;
    }

    public String getAmbitions() {
        return ambitions;
    }

    public void setAmbitions(String ambitions) {
        this.ambitions = ambitions;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getShowmeto() {
        return showmeto;
    }

    public void setShowmeto(String showmeto) {
        this.showmeto = showmeto;
    }

    public String getInterested() {
        return interested;
    }

    public void setInterested(String interested) {
        this.interested = interested;
    }

    public String getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(String lookingFor) {
        this.lookingFor = lookingFor;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getPets() {
        return pets;
    }

    public void setPets(String pets) {
        this.pets = pets;
    }

    public String getKids() {
        return kids;
    }

    public void setKids(String kids) {
        this.kids = kids;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getZodiacSign() {
        return zodiacSign;
    }

    public void setZodiacSign(String zodiacSign) {
        this.zodiacSign = zodiacSign;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getRelegion() {
        return relegion;
    }

    public void setRelegion(String relegion) {
        this.relegion = relegion;
    }

    public String getPolitical() {
        return political;
    }

    public void setPolitical(String political) {
        this.political = political;
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

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public String getAnswer1() {
        return answer1;
    }

    public String getQuestion2() {
        return question2;
    }

    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    public String getAnswer2() {
        return answer2;
    }

    public String getQuestion3() {
        return question3;
    }

    public void setQuestion3(String question3) {
        this.question3 = question3;
    }

    public String getAnswer3() {
        return answer3;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getMaxAgePrefer() {
        return maxAgePrefer;
    }

    public void setMaxAgePrefer(Integer maxAgePrefer) {
        this.maxAgePrefer = maxAgePrefer;
    }

    public Integer getMinAgePrefer() {
        return minAgePrefer;
    }

    public void setMinAgePrefer(Integer minAgePrefer) {
        this.minAgePrefer = minAgePrefer;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getExpiredMatches() {
        return expiredMatches;
    }

    public void setExpiredMatches(String expiredMatches) {
        this.expiredMatches = expiredMatches;
    }

    public String getChatNotify() {
        return chatNotify;
    }

    public void setChatNotify(String chatNotify) {
        this.chatNotify = chatNotify;
    }


    public String getMatchNotify() {
        return matchNotify;
    }

    public void setMatchNotify(String matchNotify) {
        this.matchNotify = matchNotify;
    }

    public Double getCompleted() {
        return completed;
    }

    public void setCompleted(Double completed) {
        this.completed = completed;
    }

    public Integer getSuperLikesCount() {
        return superLikesCount;
    }

    public void setSuperLikesCount(Integer superLikesCount) {
        this.superLikesCount = superLikesCount;
    }

    public Integer getTimeTokenCount() {
        return timeTokenCount;
    }

    public void setTimeTokenCount(Integer timeTokenCount) {
        this.timeTokenCount = timeTokenCount;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "ProfileOfUser{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", distanceFromMatch=" + distanceFromMatch +
                ", aboutme='" + aboutme + '\'' +
                ", ambitions='" + ambitions + '\'' +
                ", dob='" + dob + '\'' +
                ", gender='" + gender + '\'' +
                ", showmeto='" + showmeto + '\'' +
                ", interested='" + interested + '\'' +
                ", lookingFor='" + lookingFor + '\'' +
                ", pets='" + pets + '\'' +
                ", ethnicity='" + ethnicity + '\'' +
                ", kids='" + kids + '\'' +
                ", height='" + height + '\'' +
                ", zodiacSign='" + zodiacSign + '\'' +
                ", education='" + education + '\'' +
                ", school='" + school + '\'' +
                ", occupation='" + occupation + '\'' +
                ", relegion='" + relegion + '\'' +
                ", political='" + political + '\'' +
                ", drink='" + drink + '\'' +
                ", smoke='" + smoke + '\'' +
                ", exercise='" + exercise + '\'' +
                ", city='" + city + '\'' +
                ", Country='" + Country + '\'' +
                ", State='" + State + '\'' +
                ", question1='" + question1 + '\'' +
                ", question2='" + question2 + '\'' +
                ", question3='" + question3 + '\'' +
                ", answer1='" + answer1 + '\'' +
                ", answer2='" + answer2 + '\'' +
                ", answer3='" + answer3 + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", distance=" + distance +
                ", maxAgePrefer=" + maxAgePrefer +
                ", minAgePrefer=" + minAgePrefer +
                ", visible='" + visible + '\'' +
                ", expiredMatches='" + expiredMatches + '\'' +
                ", chatNotify='" + chatNotify + '\'' +
                ", matchNotify='" + matchNotify + '\'' +
                ", completed=" + completed +
                ", superLikesCount=" + superLikesCount +
                ", timeTokenCount=" + timeTokenCount +
                ", deletedAt='" + deletedAt + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", vipToken=" + vipToken +
                '}';
    }
}