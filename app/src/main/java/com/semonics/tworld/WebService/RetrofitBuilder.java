package com.semonics.tworld.WebService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.semonics.tworld.WebService.WSParams.BASE_URL;


public class RetrofitBuilder {
    public RetrofitBuilder() {         /*No need to define here anything*/ }

    /**
     * Method to get webservice      *      * @return the instance of RetrofitInterface
     */
    public static APIInterface getWebService() {
        return getBuilder().build().create(APIInterface.class);
    }


    /**
     * Method to get retofit builder object.      *      * @return object of the RetrofitBuilder
     */
    public static Retrofit.Builder getBuilder() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(90, TimeUnit.SECONDS);
        httpClient.connectTimeout(90, TimeUnit.SECONDS);

        httpClient.writeTimeout(90, TimeUnit.SECONDS);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Authorization", "Bearer "+ TWorld.getInstance().getSession().getString(SessionManager.PREF_TOKEN))
                        .header("Content-Type", "application/json")
                        .header("Accept","application/json")

                        .method(original.method(), original.body()).build();
                return chain.proceed(request);
            }
        });
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(BASE_URL).client(httpClient.build());
        builder.addConverterFactory(GsonConverterFactory.create());

        return builder;
    }
}

