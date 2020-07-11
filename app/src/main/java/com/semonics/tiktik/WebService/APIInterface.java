package com.semonics.tiktik.WebService;


import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Belal on 10/2/2017.
 */

public interface APIInterface {

    @GET("/checkServer")
    Call<ResponseBody> doCheckServer();

    @POST("/{path}")
    Call<ResponseBody> doReqeust(@Path("path") String path, @Body RequestBody requestBody);

    @POST("/{path}")
    Call<ResponseBody> doRequestWithoutBody(@Path("path") String path);

//    @POST("/login")
//    Call<ResponseBody> doLogin(@Body RequestBody requestBody);
//
//    @POST("/changePassword")
//    Call<ResponseBody> doChangePassword(@Body RequestBody requestBody);


}
