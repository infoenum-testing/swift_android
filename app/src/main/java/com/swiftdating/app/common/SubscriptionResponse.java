package com.swiftdating.app.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SubscriptionResponse implements Serializable {

    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("startTimeMillis")
    @Expose
    private String startTimeMillis;
    @SerializedName("expiryTimeMillis")
    @Expose
    private String expiryTimeMillis;
    @SerializedName("autoResumeTimeMillis")
    @Expose
    private String autoResumeTimeMillis;
    @SerializedName("autoRenewing")
    @Expose
    private boolean autoRenewing;
    @SerializedName("priceCurrencyCode")
    @Expose
    private String priceCurrencyCode;
    @SerializedName("priceAmountMicros")
    @Expose
    private String priceAmountMicros;
    @SerializedName("countryCode")
    @Expose
    private String countryCode;
    @SerializedName("developerPayload")
    @Expose
    private String developerPayload;
    @SerializedName("paymentState")
    @Expose
    private int paymentState;
    @SerializedName("cancelReason")
    @Expose
    private int cancelReason;
    @SerializedName("userCancellationTimeMillis")
    @Expose
    private String userCancellationTimeMillis;
    @SerializedName("orderId")
    @Expose
    private String orderId;
    @SerializedName("linkedPurchaseToken")
    @Expose
    private String linkedPurchaseToken;
    @SerializedName("purchaseType")
    @Expose
    private int purchaseType;
    @SerializedName("profileName")
    @Expose
    private String profileName;
    @SerializedName("emailAddress")
    @Expose
    private String emailAddress;
    @SerializedName("givenName")
    @Expose
    private String givenName;
    @SerializedName("familyName")
    @Expose
    private String familyName;
    @SerializedName("profileId")
    @Expose
    private String profileId;
    @SerializedName("acknowledgementState")
    @Expose
    private int acknowledgementState;
    @SerializedName("externalAccountId")
    @Expose
    private String externalAccountId;
    @SerializedName("promotionType")
    @Expose
    private int promotionType;
    @SerializedName("promotionCode")
    @Expose
    private String promotionCode;
    @SerializedName("obfuscatedExternalAccountId")
    @Expose
    private String obfuscatedExternalAccountId;
    @SerializedName("obfuscatedExternalProfileId")
    @Expose
    private String obfuscatedExternalProfileId;
    private final static long serialVersionUID = -5387400595300544609L;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getStartTimeMillis() {
        return startTimeMillis;
    }

    public void setStartTimeMillis(String startTimeMillis) {
        this.startTimeMillis = startTimeMillis;
    }

    public String getExpiryTimeMillis() {
        return expiryTimeMillis;
    }

    public void setExpiryTimeMillis(String expiryTimeMillis) {
        this.expiryTimeMillis = expiryTimeMillis;
    }

    public String getAutoResumeTimeMillis() {
        return autoResumeTimeMillis;
    }

    public void setAutoResumeTimeMillis(String autoResumeTimeMillis) {
        this.autoResumeTimeMillis = autoResumeTimeMillis;
    }

    public boolean isAutoRenewing() {
        return autoRenewing;
    }

    public void setAutoRenewing(boolean autoRenewing) {
        this.autoRenewing = autoRenewing;
    }

    public String getPriceCurrencyCode() {
        return priceCurrencyCode;
    }

    public void setPriceCurrencyCode(String priceCurrencyCode) {
        this.priceCurrencyCode = priceCurrencyCode;
    }

    public String getPriceAmountMicros() {
        return priceAmountMicros;
    }

    public void setPriceAmountMicros(String priceAmountMicros) {
        this.priceAmountMicros = priceAmountMicros;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getDeveloperPayload() {
        return developerPayload;
    }

    public void setDeveloperPayload(String developerPayload) {
        this.developerPayload = developerPayload;
    }

    public int getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(int paymentState) {
        this.paymentState = paymentState;
    }

    public int getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(int cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getUserCancellationTimeMillis() {
        return userCancellationTimeMillis;
    }

    public void setUserCancellationTimeMillis(String userCancellationTimeMillis) {
        this.userCancellationTimeMillis = userCancellationTimeMillis;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getLinkedPurchaseToken() {
        return linkedPurchaseToken;
    }

    public void setLinkedPurchaseToken(String linkedPurchaseToken) {
        this.linkedPurchaseToken = linkedPurchaseToken;
    }

    public int getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(int purchaseType) {
        this.purchaseType = purchaseType;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public int getAcknowledgementState() {
        return acknowledgementState;
    }

    public void setAcknowledgementState(int acknowledgementState) {
        this.acknowledgementState = acknowledgementState;
    }

    public String getExternalAccountId() {
        return externalAccountId;
    }

    public void setExternalAccountId(String externalAccountId) {
        this.externalAccountId = externalAccountId;
    }

    public int getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(int promotionType) {
        this.promotionType = promotionType;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public String getObfuscatedExternalAccountId() {
        return obfuscatedExternalAccountId;
    }

    public void setObfuscatedExternalAccountId(String obfuscatedExternalAccountId) {
        this.obfuscatedExternalAccountId = obfuscatedExternalAccountId;
    }

    public String getObfuscatedExternalProfileId() {
        return obfuscatedExternalProfileId;
    }

    public void setObfuscatedExternalProfileId(String obfuscatedExternalProfileId) {
        this.obfuscatedExternalProfileId = obfuscatedExternalProfileId;
    }

    @Override
    public String toString() {
        return "SubscriptionResponse{" +
                "kind='" + kind + '\'' +
                ", startTimeMillis='" + startTimeMillis + '\'' +
                ", expiryTimeMillis='" + expiryTimeMillis + '\'' +
                ", autoResumeTimeMillis='" + autoResumeTimeMillis + '\'' +
                ", autoRenewing=" + autoRenewing +
                ", priceCurrencyCode='" + priceCurrencyCode + '\'' +
                ", priceAmountMicros='" + priceAmountMicros + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", developerPayload='" + developerPayload + '\'' +
                ", paymentState=" + paymentState +
                ", cancelReason=" + cancelReason +
                ", userCancellationTimeMillis='" + userCancellationTimeMillis + '\'' +
                ", orderId='" + orderId + '\'' +
                ", linkedPurchaseToken='" + linkedPurchaseToken + '\'' +
                ", purchaseType=" + purchaseType +
                ", profileName='" + profileName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", givenName='" + givenName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", profileId='" + profileId + '\'' +
                ", acknowledgementState=" + acknowledgementState +
                ", externalAccountId='" + externalAccountId + '\'' +
                ", promotionType=" + promotionType +
                ", promotionCode='" + promotionCode + '\'' +
                ", obfuscatedExternalAccountId='" + obfuscatedExternalAccountId + '\'' +
                ", obfuscatedExternalProfileId='" + obfuscatedExternalProfileId + '\'' +
                '}';
    }
}