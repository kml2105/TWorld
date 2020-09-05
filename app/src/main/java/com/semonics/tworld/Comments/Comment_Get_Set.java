package com.semonics.tworld.Comments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.semonics.tworld.Model.UserDetails;

import java.io.Serializable;

/**
 * Created by Komal on 3/5/2019.
 */

public class Comment_Get_Set  implements Serializable  {

    @SerializedName("_id")
    @Expose
    public String id;
    @SerializedName("comment")
    @Expose
    public String comment;
    @SerializedName("documentAuthor")
    @Expose
    public String documentAuthor;
    @SerializedName("likedBy")
    @Expose
    public Object likedBy;
    @SerializedName("likeCount")
    @Expose
    public Integer likeCount;
    @SerializedName("userDetails")
    @Expose
    public UserDetails userDetails;
    @SerializedName("createdBy")
    @Expose
    public String createdBy;
    @SerializedName("createdDate")
    @Expose
    public Integer createdDate;
    @SerializedName("modifiedBy")
    @Expose
    public String modifiedBy;
    @SerializedName("modifiedDate")
    @Expose
    public Integer modifiedDate;

}
