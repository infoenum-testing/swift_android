package com.swift.dating.model.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MatchListResponseModel implements Serializable {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("myquestions")
    @Expose
    private Myquestions myquestions;
    @SerializedName("matches")
    @Expose
    private List<Match> matches = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("error")
    @Expose
    private Error error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Myquestions getMyquestions() {
        return myquestions;
    }

    public void setMyquestions(Myquestions myquestions) {
        this.myquestions = myquestions;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setError(Error error) {
        this.error = error;
    }


    public Error getError() {
        return error;
    }

    public class Error implements Serializable {
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        String code;

    }

    public class Match implements Serializable {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("roleId")
        @Expose
        private Integer roleId;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("deletedAt")
        @Expose
        private String deletedAt;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("AnswerForUser.id")
        @Expose
        private Integer answerForUserId;
        @SerializedName("AnswerForUser.userId")
        @Expose
        private Integer answerForUserUserId;
        @SerializedName("AnswerForUser.matchId")
        @Expose
        private Integer answerForUserMatchId;
        @SerializedName("AnswerForUser.Answer1")
        @Expose
        private String answerForUserAnswer1;
        @SerializedName("AnswerForUser.Answer2")
        @Expose
        private String answerForUserAnswer2;
        @SerializedName("AnswerForUser.Answer3")
        @Expose
        private String answerForUserAnswer3;
        @SerializedName("AnswerForUser.Question1")
        @Expose
        private String answerForUserQuestion1;
        @SerializedName("AnswerForUser.Question2")
        @Expose
        private String answerForUserQuestion2;
        @SerializedName("AnswerForUser.Question3")
        @Expose
        private String answerForUserQuestion3;
        @SerializedName("AnswerForUser.ContactNumber")
        @Expose
        private String answerForUserContactNumber;
        @SerializedName("AnswerForUser.deletedAt")
        @Expose
        private String answerForUserDeletedAt;
        @SerializedName("AnswerForUser.createdAt")
        @Expose
        private String answerForUserCreatedAt;
        @SerializedName("AnswerForUser.updatedAt")
        @Expose
        private String answerForUserUpdatedAt;
        @SerializedName("AnswerByUser.id")
        @Expose
        private Integer answerByUserId;
        @SerializedName("AnswerByUser.userId")
        @Expose
        private Integer answerByUserUserId;
        @SerializedName("AnswerByUser.matchId")
        @Expose
        private Integer answerByUserMatchId;
        @SerializedName("AnswerByUser.Answer1")
        @Expose
        private String answerByUserAnswer1;
        @SerializedName("AnswerByUser.Answer2")
        @Expose
        private String answerByUserAnswer2;
        @SerializedName("AnswerByUser.Answer3")
        @Expose
        private String answerByUserAnswer3;
        @SerializedName("AnswerByUser.Question1")
        @Expose
        private String answerByUserQuestion1;
        @SerializedName("AnswerByUser.Question2")
        @Expose
        private String answerByUserQuestion2;
        @SerializedName("AnswerByUser.Question3")
        @Expose
        private String answerByUserQuestion3;
        @SerializedName("AnswerByUser.ContactNumber")
        @Expose
        private String answerByUserContactNumber;
        @SerializedName("AnswerByUser.deletedAt")
        @Expose
        private String answerByUserDeletedAt;
        @SerializedName("AnswerByUser.createdAt")
        @Expose
        private String answerByUserCreatedAt;
        @SerializedName("AnswerByUser.updatedAt")
        @Expose
        private String answerByUserUpdatedAt;
        @SerializedName("profileOfUser.id")
        @Expose
        private Integer profileOfUserId;
        @SerializedName("profileOfUser.name")
        @Expose
        private String profileOfUserName;
        @SerializedName("ImageForUser.id")
        @Expose
        private Integer imageForUserId;
        @SerializedName("ImageForUser.userId")
        @Expose
        private Integer imageForUserUserId;
        @SerializedName("ImageForUser.orderId")
        @Expose
        private Integer imageForUserOrderId;
        @SerializedName("ImageForUser.imageUrl")
        @Expose
        private String imageForUserImageUrl;
        @SerializedName("ImageForUser.deletedAt")
        @Expose
        private String imageForUserDeletedAt;
        @SerializedName("ImageForUser.createdAt")
        @Expose
        private String imageForUserCreatedAt;
        @SerializedName("ImageForUser.updatedAt")
        @Expose
        private String imageForUserUpdatedAt;
        @SerializedName("MatchForUser.id")
        @Expose
        private Integer matchForUserId;
        @SerializedName("MatchForUser.CalltimerExpiry")
        @Expose
        private String matchForUserCalltimerExpiry;
        @SerializedName("MatchForUser.answersGiven")
        @Expose
        private String matchForUserAnswersGiven;
        @SerializedName("MatchForUser.createdAt")
        @Expose
        private String matchForUserCreatedAt;
        @SerializedName("MatchForUser.updatedAt")
        @Expose
        private String matchForUserUpdatedAt;
        @SerializedName("MatchForUser.ServerTime")
        @Expose
        private String matchForUserServerTime;
        @SerializedName("MatchForUser.timetokenAppliedOn")
        @Expose
        private String timetokenAppliedOn;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Integer getRoleId() {
            return roleId;
        }

        public void setRoleId(Integer roleId) {
            this.roleId = roleId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public Integer getAnswerForUserId() {
            return answerForUserId;
        }

        public void setAnswerForUserId(Integer answerForUserId) {
            this.answerForUserId = answerForUserId;
        }

        public Integer getAnswerForUserUserId() {
            return answerForUserUserId;
        }

        public void setAnswerForUserUserId(Integer answerForUserUserId) {
            this.answerForUserUserId = answerForUserUserId;
        }

        public Integer getAnswerForUserMatchId() {
            return answerForUserMatchId;
        }

        public void setAnswerForUserMatchId(Integer answerForUserMatchId) {
            this.answerForUserMatchId = answerForUserMatchId;
        }

        public String getAnswerForUserAnswer1() {
            return answerForUserAnswer1;
        }

        public void setAnswerForUserAnswer1(String answerForUserAnswer1) {
            this.answerForUserAnswer1 = answerForUserAnswer1;
        }

        public String getAnswerForUserAnswer2() {
            return answerForUserAnswer2;
        }

        public void setAnswerForUserAnswer2(String answerForUserAnswer2) {
            this.answerForUserAnswer2 = answerForUserAnswer2;
        }

        public String getAnswerForUserAnswer3() {
            return answerForUserAnswer3;
        }

        public void setAnswerForUserAnswer3(String answerForUserAnswer3) {
            this.answerForUserAnswer3 = answerForUserAnswer3;
        }

        public String getAnswerForUserQuestion1() {
            return answerForUserQuestion1;
        }

        public void setAnswerForUserQuestion1(String answerForUserQuestion1) {
            this.answerForUserQuestion1 = answerForUserQuestion1;
        }

        public String getAnswerForUserQuestion2() {
            return answerForUserQuestion2;
        }

        public void setAnswerForUserQuestion2(String answerForUserQuestion2) {
            this.answerForUserQuestion2 = answerForUserQuestion2;
        }

        public String getAnswerForUserQuestion3() {
            return answerForUserQuestion3;
        }

        public void setAnswerForUserQuestion3(String answerForUserQuestion3) {
            this.answerForUserQuestion3 = answerForUserQuestion3;
        }

        public String getAnswerForUserContactNumber() {
            return answerForUserContactNumber;
        }

        public void setAnswerForUserContactNumber(String answerForUserContactNumber) {
            this.answerForUserContactNumber = answerForUserContactNumber;
        }

        public String getAnswerForUserDeletedAt() {
            return answerForUserDeletedAt;
        }

        public void setAnswerForUserDeletedAt(String answerForUserDeletedAt) {
            this.answerForUserDeletedAt = answerForUserDeletedAt;
        }

        public String getAnswerForUserCreatedAt() {
            return answerForUserCreatedAt;
        }

        public void setAnswerForUserCreatedAt(String answerForUserCreatedAt) {
            this.answerForUserCreatedAt = answerForUserCreatedAt;
        }

        public String getAnswerForUserUpdatedAt() {
            return answerForUserUpdatedAt;
        }

        public void setAnswerForUserUpdatedAt(String answerForUserUpdatedAt) {
            this.answerForUserUpdatedAt = answerForUserUpdatedAt;
        }

        public Integer getAnswerByUserId() {
            return answerByUserId;
        }

        public void setAnswerByUserId(Integer answerByUserId) {
            this.answerByUserId = answerByUserId;
        }

        public Integer getAnswerByUserUserId() {
            return answerByUserUserId;
        }

        public void setAnswerByUserUserId(Integer answerByUserUserId) {
            this.answerByUserUserId = answerByUserUserId;
        }

        public Integer getAnswerByUserMatchId() {
            return answerByUserMatchId;
        }

        public void setAnswerByUserMatchId(Integer answerByUserMatchId) {
            this.answerByUserMatchId = answerByUserMatchId;
        }

        public String getAnswerByUserAnswer1() {
            return answerByUserAnswer1;
        }

        public void setAnswerByUserAnswer1(String answerByUserAnswer1) {
            this.answerByUserAnswer1 = answerByUserAnswer1;
        }

        public String getAnswerByUserAnswer2() {
            return answerByUserAnswer2;
        }

        public void setAnswerByUserAnswer2(String answerByUserAnswer2) {
            this.answerByUserAnswer2 = answerByUserAnswer2;
        }

        public String getAnswerByUserAnswer3() {
            return answerByUserAnswer3;
        }

        public void setAnswerByUserAnswer3(String answerByUserAnswer3) {
            this.answerByUserAnswer3 = answerByUserAnswer3;
        }

        public String getAnswerByUserQuestion1() {
            return answerByUserQuestion1;
        }

        public void setAnswerByUserQuestion1(String answerByUserQuestion1) {
            this.answerByUserQuestion1 = answerByUserQuestion1;
        }

        public String getAnswerByUserQuestion2() {
            return answerByUserQuestion2;
        }

        public void setAnswerByUserQuestion2(String answerByUserQuestion2) {
            this.answerByUserQuestion2 = answerByUserQuestion2;
        }

        public String getAnswerByUserQuestion3() {
            return answerByUserQuestion3;
        }

        public void setAnswerByUserQuestion3(String answerByUserQuestion3) {
            this.answerByUserQuestion3 = answerByUserQuestion3;
        }

        public String getAnswerByUserContactNumber() {
            return answerByUserContactNumber;
        }

        public void setAnswerByUserContactNumber(String answerByUserContactNumber) {
            this.answerByUserContactNumber = answerByUserContactNumber;
        }

        public String getAnswerByUserDeletedAt() {
            return answerByUserDeletedAt;
        }

        public void setAnswerByUserDeletedAt(String answerByUserDeletedAt) {
            this.answerByUserDeletedAt = answerByUserDeletedAt;
        }

        public String getAnswerByUserCreatedAt() {
            return answerByUserCreatedAt;
        }

        public void setAnswerByUserCreatedAt(String answerByUserCreatedAt) {
            this.answerByUserCreatedAt = answerByUserCreatedAt;
        }

        public String getAnswerByUserUpdatedAt() {
            return answerByUserUpdatedAt;
        }

        public void setAnswerByUserUpdatedAt(String answerByUserUpdatedAt) {
            this.answerByUserUpdatedAt = answerByUserUpdatedAt;
        }

        public Integer getProfileOfUserId() {
            return profileOfUserId;
        }

        public void setProfileOfUserId(Integer profileOfUserId) {
            this.profileOfUserId = profileOfUserId;
        }

        public String getProfileOfUserName() {
            return profileOfUserName;
        }

        public void setProfileOfUserName(String profileOfUserName) {
            this.profileOfUserName = profileOfUserName;
        }

        public Integer getImageForUserId() {
            return imageForUserId;
        }

        public void setImageForUserId(Integer imageForUserId) {
            this.imageForUserId = imageForUserId;
        }

        public Integer getImageForUserUserId() {
            return imageForUserUserId;
        }

        public void setImageForUserUserId(Integer imageForUserUserId) {
            this.imageForUserUserId = imageForUserUserId;
        }

        public Integer getImageForUserOrderId() {
            return imageForUserOrderId;
        }

        public void setImageForUserOrderId(Integer imageForUserOrderId) {
            this.imageForUserOrderId = imageForUserOrderId;
        }

        public String getImageForUserImageUrl() {
            return imageForUserImageUrl;
        }

        public void setImageForUserImageUrl(String imageForUserImageUrl) {
            this.imageForUserImageUrl = imageForUserImageUrl;
        }

        public String getImageForUserDeletedAt() {
            return imageForUserDeletedAt;
        }

        public void setImageForUserDeletedAt(String imageForUserDeletedAt) {
            this.imageForUserDeletedAt = imageForUserDeletedAt;
        }

        public String getImageForUserCreatedAt() {
            return imageForUserCreatedAt;
        }

        public void setImageForUserCreatedAt(String imageForUserCreatedAt) {
            this.imageForUserCreatedAt = imageForUserCreatedAt;
        }

        public String getImageForUserUpdatedAt() {
            return imageForUserUpdatedAt;
        }

        public void setImageForUserUpdatedAt(String imageForUserUpdatedAt) {
            this.imageForUserUpdatedAt = imageForUserUpdatedAt;
        }

        public Integer getMatchForUserId() {
            return matchForUserId;
        }

        public void setMatchForUserId(Integer matchForUserId) {
            this.matchForUserId = matchForUserId;
        }

        public String getMatchForUserCalltimerExpiry() {
            return matchForUserCalltimerExpiry;
        }

        public void setMatchForUserCalltimerExpiry(String matchForUserCalltimerExpiry) {
            this.matchForUserCalltimerExpiry = matchForUserCalltimerExpiry;
        }

        public String getMatchForUserAnswersGiven() {
            return matchForUserAnswersGiven;
        }

        public void setMatchForUserAnswersGiven(String matchForUserAnswersGiven) {
            this.matchForUserAnswersGiven = matchForUserAnswersGiven;
        }

        public String getMatchForUserCreatedAt() {
            return matchForUserCreatedAt;
        }

        public void setMatchForUserCreatedAt(String matchForUserCreatedAt) {
            this.matchForUserCreatedAt = matchForUserCreatedAt;
        }

        public String getMatchForUserUpdatedAt() {
            return matchForUserUpdatedAt;
        }

        public void setMatchForUserUpdatedAt(String matchForUserUpdatedAt) {
            this.matchForUserUpdatedAt = matchForUserUpdatedAt;
        }

        public String getMatchForUserServerTime() {
            return matchForUserServerTime;
        }

        public void setMatchForUserServerTime(String matchForUserServerTime) {
            this.matchForUserServerTime = matchForUserServerTime;
        }


        public String getTimetokenAppliedOn() {
            return timetokenAppliedOn;
        }
    }

    public class Myquestions implements Serializable {

        @SerializedName("Question1")
        @Expose
        private String question1;
        @SerializedName("Question2")
        @Expose
        private String question2;
        @SerializedName("Question3")
        @Expose
        private String question3;

        public String getQuestion1() {
            return question1;
        }

        public void setQuestion1(String question1) {
            this.question1 = question1;
        }

        public String getQuestion2() {
            return question2;
        }

        public void setQuestion2(String question2) {
            this.question2 = question2;
        }

        public String getQuestion3() {
            return question3;
        }

        public void setQuestion3(String question3) {
            this.question3 = question3;
        }

    }
}