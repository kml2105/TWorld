package com.semonics.tworld.Search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SearchUserModel implements Serializable {
    @SerializedName("_id")
    @Expose
    public String id;
    @SerializedName("authToken")
    @Expose
    public String authToken;
    @SerializedName("bio")
    @Expose
    public String bio;
    @SerializedName("createdBy")
    @Expose
    public String createdBy;
    @SerializedName("createdDate")
    @Expose
    public Integer createdDate;
    @SerializedName("deviceId")
    @Expose
    public String deviceId;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("emailOtp")
    @Expose
    public Integer emailOtp;
    @SerializedName("emailOtpExpiredTime")
    @Expose
    public Integer emailOtpExpiredTime;
    @SerializedName("emailVerifiedStatus")
    @Expose
    public Integer emailVerifiedStatus;
    @SerializedName("firstName")
    @Expose
    public String firstName;
    @SerializedName("followersCount")
    @Expose
    public Integer followersCount;
    @SerializedName("followingCount")
    @Expose
    public Integer followingCount;
    @SerializedName("lastName")
    @Expose
    public String lastName;
    @SerializedName("mobile")
    @Expose
    public Integer mobile;
    @SerializedName("mobileOtp")
    @Expose
    public Integer mobileOtp;
    @SerializedName("mobileOtpExpiredTime")
    @Expose
    public Integer mobileOtpExpiredTime;
    @SerializedName("mobileVerifiedStatus")
    @Expose
    public Integer mobileVerifiedStatus;
    @SerializedName("modifiedBy")
    @Expose
    public String modifiedBy;
    @SerializedName("modifiedDate")
    @Expose
    public Integer modifiedDate;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("postCount")
    @Expose
    public Integer postCount;
    @SerializedName("profileType")
    @Expose
    public Integer profileType;
    @SerializedName("status")
    @Expose
    public Integer status;
/*    @SerializedName("userProfile")
    @Expose
    public UserProfile userProfile;*/
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("website")
    @Expose
    public String website;
}
