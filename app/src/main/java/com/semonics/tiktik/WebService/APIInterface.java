package com.semonics.tiktik.WebService;


import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Belal on 10/2/2017.
 */

public interface APIInterface {
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/{module}")
    Call<ResponseBody> doRequestPost(@Path(value = "module", encoded = true) String module, @Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/{module}")
    Call<ResponseBody> doRequestPostWithHeader(@Header("Authorization") String token, @Path(value = "module", encoded = true) String module, @Body RequestBody requestBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/{module}")
    Call<ResponseBody> doRequestGet(@Header("Authorization") String token, @Path(value = "module", encoded = true) String module);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @DELETE("/{module}")
    Call<ResponseBody> doRequestDelete(@Header("Authorization") String token, @Path(value = "module", encoded = true) String module);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/{module}")
    Call<ResponseBody> doRequestGetWithPagination(
            @Header("Authorization") String token,
            @Path(value = "module", encoded = true) String module,
            @Query("page") int pageStartIndex,
            @Query("limit") int pageEndIndex
    );

    @GET("/{module}")
    Call<ResponseBody> doRequestGetForSearchWithPagination(
            @Header("Authorization") String token,
            @Path(value = "module", encoded = true) String module,
            @Query("keyword") String keyword,
            @Query("page") int pageStartIndex,
            @Query("limit") int pageEndIndex
    );

    @Multipart
    @POST("/{module}")
    Call<ResponseBody> uploadFile(
            @Header("token") String token,
            @Part RequestBody requestBody, @Path("path") String module
            /*@Part MultipartBody.Part file*/);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @Multipart
    @POST("/{module}")
    Call<ResponseBody> postVideoThumbnail(@Header("Authorization") String token, @Path(value = "module", encoded = true) String module, @PartMap() Map<String, RequestBody> partMap
            , @Part MultipartBody.Part video_thumbnail);

//    @POST("/login")
//    Call<ResponseBody> doLogin(@Body RequestBody requestBody);
//
//    @POST("/changePassword")
//    Call<ResponseBody> doChangePassword(@Body RequestBody requestBody);


}
