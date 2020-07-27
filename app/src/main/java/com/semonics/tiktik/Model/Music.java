package com.semonics.tiktik.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Music {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("docType")
    @Expose
    private String docType;
    @SerializedName("musicName")
    @Expose
    private String musicName;
    @SerializedName("musicSize")
    @Expose
    private Integer musicSize;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("thumb")
    @Expose
    private String thumb;
}

