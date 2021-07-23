package com.swift.dating.model.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import com.swift.dating.model.BaseModel;
import com.swift.dating.model.ImageModel;

public class VerificationResponseModel extends BaseModel {
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("ProfileCompleted")
    @Expose
    private Double ProfileCompleted;
    @SerializedName(value = "imagedata", alternate = "images")
    @Expose
    private ImageData imagedata;
    @SerializedName("insta")
    @Expose
    private ArrayList<InstagramImageModel.Datum> insta;
    @SerializedName("login")
    @Expose
    private Boolean login;
    @SerializedName("newuser")
    @Expose
    private Boolean newuser;
    @SerializedName("NoOfLikes")
    @Expose
    private NoOfLikes noOfLikes;

    public NoOfLikes getNoOfLikes() {
        return noOfLikes;
    }

    public void setNoOfLikes(NoOfLikes noOfLikes) {
        this.noOfLikes = noOfLikes;
    }

    public Boolean getNewuser() {
        return newuser;
    }

    public void setNewuser(Boolean newuser) {
        this.newuser = newuser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<InstagramImageModel.Datum> getInsta() {
        return insta;
    }

    public void setInsta(ArrayList<InstagramImageModel.Datum> insta) {
        this.insta = insta;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getLogin() {
        return login;
    }

    public void setLogin(Boolean login) {
        this.login = login;
    }

    public ImageData getImagedata() {
        return imagedata;
    }

    public void setImagedata(ImageData imagedata) {
        this.imagedata = imagedata;
    }

    public Double getProfileCompleted() {
        return ProfileCompleted;
    }

    public void setProfileCompleted(Double profileCompleted) {
        ProfileCompleted = profileCompleted;
    }

    public class NoOfLikes {
        @SerializedName("success")
        @Expose
        private Boolean success;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("users")
        @Expose
        private int users;
        @SerializedName("usersImage")
        @Expose
        private List<ImageModel> usersImage = null;

        public List<ImageModel> getUsersImage() {
            return usersImage;
        }

        public void setUsersImage(List<ImageModel> usersImage) {
            this.usersImage = usersImage;
        }

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getUsers() {
            return users;
        }

        public void setUsers(int users) {
            this.users = users;
        }

        @Override
        public String toString() {
            return "NoOfLikes{" +
                    "success=" + success +
                    ", message='" + message + '\'' +
                    ", users=" + users +
                    '}';
        }
    }

    public class OtpOfUser {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("userId")
        @Expose
        private Integer userId;
        @SerializedName("otp")
        @Expose
        private Integer otp;
        @SerializedName("counter")
        @Expose
        private Integer counter;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;

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

        public Integer getOtp() {
            return otp;
        }

        public void setOtp(Integer otp) {
            this.otp = otp;
        }

        public Integer getCounter() {
            return counter;
        }

        public void setCounter(Integer counter) {
            this.counter = counter;
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

    }

    public class User {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("roleId")
        @Expose
        private Integer roleId;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("isVerified")
        @Expose
        private String isVerified;
        @SerializedName("isLinkedinUser")
        @Expose
        private String isLinkedinUser;
        @SerializedName("isPremium")
        @Expose
        private String isPremium;
        @SerializedName("isDeluxe")
        @Expose
        private String isDeluxe;
        @SerializedName("isVerifiedDate")
        @Expose
        private String isVerifiedDate;
        @SerializedName("deletedAt")
        @Expose
        private String deletedAt;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("isRejected")
        @Expose
        private String isRejected;
        @SerializedName("otpOfUser")
        @Expose
        private OtpOfUser otpOfUser;
        @SerializedName("profileOfUser")
        @Expose
        private ProfileOfUser profileOfUser;
        @SerializedName("SelfiesForUser")
        @Expose
        private SelfieOfUser SelfiesForUser;

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

        public String getIsDeluxe() {
            return isDeluxe;
        }

        public void setIsDeluxe(String isDeluxe) {
            this.isDeluxe = isDeluxe;
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

        public OtpOfUser getOtpOfUser() {
            return otpOfUser;
        }

        public void setOtpOfUser(OtpOfUser otpOfUser) {
            this.otpOfUser = otpOfUser;
        }

        public ProfileOfUser getProfileOfUser() {
            return profileOfUser;
        }

        public void setProfileOfUser(ProfileOfUser profileOfUser) {
            this.profileOfUser = profileOfUser;
        }

        public SelfieOfUser getSelfiesForUser() {
            return SelfiesForUser;
        }

        public void setSelfiesForUser(SelfieOfUser selfiesForUser) {
            SelfiesForUser = selfiesForUser;
        }

        public String getIsVerified() {
            return isVerified;
        }

        public String getisRejected() {
            return isRejected;
        }

        public String getIsLinkedinUser() {
            return isLinkedinUser;
        }

        public String getIsPremium() {
            return isPremium;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }

    public class SelfieOfUser {
        @SerializedName("selfieUrl")
        String selfieUrl;

        public String getSelfieUrl() {
            return selfieUrl;
        }

        public void setSelfieUrl(String selfieUrl) {
            this.selfieUrl = selfieUrl;
        }
    }


    public class ImageData {
        @SerializedName("success")
        private boolean success;
        @SerializedName("data")
        List<ImageModel> data;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public List<ImageModel> getData() {
            return data;
        }

        public void setData(List<ImageModel> data) {
            this.data = data;
        }


    }

    @Override
    public String toString() {
        return "VerificationResponseModel{" +
                "token='" + token + '\'' +
                ", user=" + user +
                ", ProfileCompleted=" + ProfileCompleted +
                ", imagedata=" + imagedata +
                ", insta=" + insta +
                ", login=" + login +
                ", newuser=" + newuser +
                ", noOfLikes=" + noOfLikes +
                '}';
    }
}