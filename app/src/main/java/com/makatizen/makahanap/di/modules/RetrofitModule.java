package com.makatizen.makahanap.di.modules;


import com.makatizen.makahanap.data.remote.ApiConstants;
import com.makatizen.makahanap.data.remote.ApiHelper;
import com.makatizen.makahanap.data.remote.ApiInterface;
import com.makatizen.makahanap.data.remote.AppApiHelper;
import com.makatizen.makahanap.di.qualifiers.MakatizenApi;
import com.makatizen.makahanap.di.scopes.ApplicationScope;
import dagger.Module;
import dagger.Provides;
import java.io.IOException;
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
    ApiHelper provideApiHelper(ApiInterface apiInterface) {
        return new AppApiHelper(apiInterface);
    }

    @Provides
    @ApplicationScope
    ApiInterface provideApiInterface(Retrofit retrofit) {
        return retrofit.create(ApiInterface.class);
    }

    @Provides
    @ApplicationScope
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }


    @Provides
    @ApplicationScope
    @MakatizenApi
    ApiHelper provideMakatizenApiHelper(@MakatizenApi ApiInterface apiInterface) {
        return new AppApiHelper(apiInterface);
    }

    @Provides
    @ApplicationScope
    @MakatizenApi
    OkHttpClient provideMakatizenOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor) {
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
    @MakatizenApi
    Retrofit provideMakatizenRetrofit(@MakatizenApi OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(ApiConstants.MAKATIZEN_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @ApplicationScope
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor) {
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
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @ApplicationScope
    @MakatizenApi
    ApiInterface provideRetrofitApiInterface(@MakatizenApi Retrofit retrofit) {
        return retrofit.create(ApiInterface.class);
    }
}
