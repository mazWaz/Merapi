package live.mazwaz.merapi.data

import android.webkit.CookieManager
import com.orhanobut.hawk.Hawk
import live.mazwaz.merapi.BuildConfig
import live.mazwaz.merapi.data.service.AppService
import live.mazwaz.merapi.data.service.AppService2
import live.mazwaz.merapi.utils.Constants
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


fun retrofit(url: String = Constants.BASE_URL): Retrofit {

    val loggingInterceptor: HttpLoggingInterceptor = if (BuildConfig.DEBUG) {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    } else {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
    }

    val httpClient = OkHttpClient.Builder()
        .readTimeout(120, TimeUnit.SECONDS)
        .connectTimeout(120, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .addInterceptor {
            val requestBuilder = it.request().newBuilder()
            requestBuilder.addHeader("Accept", "application/json")
            it.request().newBuilder().get()
            it.proceed(requestBuilder.build())
        }


    return Retrofit.Builder()
        .baseUrl(url)
        .client(httpClient.build())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
}

fun retrofit2(url: String = Constants.BASE_URL): Retrofit {

    val loggingInterceptor: HttpLoggingInterceptor = if (BuildConfig.DEBUG) {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    } else {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
    }

    val httpClient = OkHttpClient.Builder()
        .readTimeout(120, TimeUnit.SECONDS)
        .connectTimeout(120, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .addInterceptor {
            val requestBuilder = it.request().newBuilder()
            requestBuilder.addHeader("Accept", "application/json")
            it.request().newBuilder().get()
            it.proceed(requestBuilder.build())
        }


    return Retrofit.Builder()
        .baseUrl(url)
        .client(httpClient.build())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
}

fun provideApiService(retrofit: Retrofit): AppService =
    retrofit.create(AppService::class.java)

fun provideApiService2(retrofit2: Retrofit): AppService2 =
    retrofit2.create(AppService2::class.java)