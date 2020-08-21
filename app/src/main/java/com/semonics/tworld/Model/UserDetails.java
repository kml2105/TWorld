package com.semonics.tworld.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserDetails implements Serializable {
    @SerializedName("firstName")
    @Expose
    public String firstName;
    @SerializedName("lastName")
    @Expose
    public String lastName;
    @SerializedName("profilePic")
    @Expose
    public String profilePic;
    @SerializedName("profileType")
    @Expose
    public Integer profileType;
}
