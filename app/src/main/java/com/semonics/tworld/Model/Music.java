package com.semonics.tworld.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Music implements Serializable {
    @SerializedName("_id")
    @Expose
    public String id;
    @SerializedName("docType")
    @Expose
    public String docType;
    @SerializedName("musicName")
    @Expose
    public String musicName;
    @SerializedName("musicSize")
    @Expose
    public Integer musicSize;
    @SerializedName("path")
    @Expose
    public String path;
    @SerializedName("thumb")
    @Expose
    public String thumb;
}

