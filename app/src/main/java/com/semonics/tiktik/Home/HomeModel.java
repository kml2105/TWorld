package com.semonics.tiktik.Home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.semonics.tiktik.Model.Music;
import com.semonics.tiktik.Model.UserDetails;

import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.io.Serializable;
import java.util.List;

/**
 * Created by AQEEL on 2/18/2019.
 */

public class HomeModel implements Serializable {
    @SerializedName("_id")
    @Expose
    public String id;
    @SerializedName("docName")
    @Expose
    public String docName;
    @SerializedName("docsize")
    @Expose
    public Integer docsize;
    @SerializedName("docType")
    @Expose
    public String docType;
    @SerializedName("docStatus")
    @Expose
    public Integer docStatus;
    @SerializedName("docTypeExt")
    @Expose
    public String docTypeExt;
    @SerializedName("location")
    @Expose
    public String location;
    @SerializedName("hashTag")
    @Expose
    public List<String> hashTag = null;
    @SerializedName("caption")
    @Expose
    public String caption;
    @SerializedName("tagPeople")
    @Expose
    public List<Object> tagPeople = null;
    @SerializedName("createdDate")
    @Expose
    public Integer createdDate;
    @SerializedName("createdBy")
    @Expose
    public String createdBy;
    @SerializedName("likedBy")
    @Expose
    public Object likedBy;
    @SerializedName("comment")
    @Expose
    public List<Comment> comment = null;
    @SerializedName("likeCount")
    @Expose
    public Integer likeCount;
    @SerializedName("likeCount")
    @Expose
    public String thumb;
    @SerializedName("thumb")
    @Expose
    public Integer commentCount;
    @SerializedName("viewerCount")
    @Expose
    public Integer viewerCount;
    @SerializedName("like")
    @Expose
    public Integer like;
    @SerializedName("userDetails")
    @Expose
    public UserDetails userDetails;
    @SerializedName("music")
    @Expose
    public Music music;
}
