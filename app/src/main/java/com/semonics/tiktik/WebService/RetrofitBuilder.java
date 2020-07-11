package com.semonics.tiktik.WebService;

import com.semonics.tiktik.SimpleClasses.SessionManager;
import com.semonics.tiktik.SimpleClasses.TicTic;
import com.semonics.tiktik.SimpleClasses.WSParams;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

import static com.semonics.tiktik.SimpleClasses.Variables.base_url;

public class RetrofitBuilder {
    static String BASE_URL = "http://18.188.113.192:80/";
    public RetrofitBuilder() {         /*No need to define here anything*/ }

    /**
     * Method to get webservice      *      * @return the instance of RetrofitInterface
     */
    public static APIInterface getWebService(String url) {
        BASE_URL = BASE_URL + url;
        TicTic.getInstance().getSession().putString(SessionManager.PREF_BASE_URL, BASE_URL);

        return getBuilder().build().create(APIInterface.class);
    }

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
                        .header("Authorization", "Bearer "+ TicTic.getInstance().getSession().getString(WSParams.WS_KEY_TOKEN))
                        .header("Content-Type", "application/json")
                        .header("Accept","application/json")
                        .method(original.method(), original.body()).build();
                return chain.proceed(request);
            }
        });
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(TicTic.getInstance().getSession().getString(BASE_URL)).client(httpClient.build());

        return builder;
    }
}

