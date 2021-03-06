package com.makatizen.makahanap.di.modules;


import com.makatizen.makahanap.data.remote.ApiConstants;
import com.makatizen.makahanap.data.remote.ApiHelper;
import com.makatizen.makahanap.data.remote.ApiInterface;
import com.makatizen.makahanap.data.remote.AppApiHelper;
import com.makatizen.makahanap.data.remote.MakatizenApiInterface;
import com.makatizen.makahanap.data.remote.NexmoApiInterface;
import com.makatizen.makahanap.di.qualifiers.MakatizenApi;
import com.makatizen.makahanap.di.qualifiers.NexmoApi;
import com.makatizen.makahanap.di.scopes.ApplicationScope;

import java.io.IOException;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RetrofitModule {

    @Provides
    @ApplicationScope
    static ApiHelper provideApiHelper(ApiInterface apiInterface, MakatizenApiInterface makatizenApiInterface) {
        return new AppApiHelper(apiInterface, makatizenApiInterface);
    }

    @Provides
    @ApplicationScope
    static ApiInterface provideApiInterface(Retrofit retrofit) {
        return retrofit.create(ApiInterface.class);
    }

    @Provides
    @ApplicationScope
    static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    @Provides
    @ApplicationScope
    static MakatizenApiInterface provideMakatizenApiInterface(@MakatizenApi Retrofit retrofit) {
        return retrofit.create(MakatizenApiInterface.class);
    }

    @Provides
    @ApplicationScope
    static NexmoApiInterface provideNexmoApiInterface(@NexmoApi Retrofit retrofit) {
        return retrofit.create(NexmoApiInterface.class);
    }

    @Provides
    @ApplicationScope
    @MakatizenApi
    static OkHttpClient provideMakatizenOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(final Chain chain) throws IOException {
                Request request = chain.request();

                Builder builder = request.newBuilder()
                        .addHeader("X-API-KEY", ApiConstants.MAKATIZEN_API_KEY)
                        .addHeader("Content-Type", ApiConstants.CONTENT_TYPE)
                        .addHeader("Authorization", ApiConstants.AUTHORIZATION);
                return chain.proceed(builder.build());
            }
        }).addNetworkInterceptor(httpLoggingInterceptor);

        return builder.build();
    }

    @Provides
    @ApplicationScope
    @NexmoApi
    static OkHttpClient provideNexmoOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(final Chain chain) throws IOException {
                Request request = chain.request();

                Builder builder = request.newBuilder()
                        .addHeader("x-rapidapi-host", "nexmo-nexmo-sms-verify-v1.p.rapidapi.com")
                        .addHeader("x-rapidapi-key", "3a734ac8bfmshe5ece28f4cf1d77p118dd0jsn2b53b5176dc8")
                        .addHeader("content-type", "application/x-www-form-urlencoded");
                return chain.proceed(builder.build());
            }
        }).addNetworkInterceptor(httpLoggingInterceptor);

        return builder.build();
    }

    @Provides
    @ApplicationScope
    @NexmoApi
    static Retrofit provideNexmoRetrofit(@NexmoApi OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl("https://nexmo-nexmo-sms-verify-v1.p.rapidapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @ApplicationScope
    @MakatizenApi
    static Retrofit provideMakatizenRetrofit(@MakatizenApi OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(ApiConstants.MAKATIZEN_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @ApplicationScope
    static OkHttpClient provideOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(final Chain chain) throws IOException {
                Request request = chain.request();

                Builder builder = request.newBuilder()
                        .addHeader("X-API-KEY", ApiConstants.API_KEY)
                        .addHeader("Content-Type", ApiConstants.CONTENT_TYPE)
                        .addHeader("Authorization", ApiConstants.AUTHORIZATION);
                return chain.proceed(builder.build());
            }
        }).addInterceptor(httpLoggingInterceptor);

        return builder.build();
    }

    @Provides
    @ApplicationScope
    static Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
