package com.penchan.noodoeapi.retrofit

import androidx.viewbinding.BuildConfig
import com.penchan.noodoeapi.repository.api.LoginService
import com.penchan.noodoeapi.repository.api.ParkService
import com.penchan.noodoeapi.repository.api.UserService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {

    companion object {
        private const val TIME_OUT = 60L
        const val NOODOE_URL = "https://noodoe-app-development.web.app/"
        const val TAIPEI_URL = "https://tcgbusfs.blob.core.windows.net/"

        private val noodoeClient: Retrofit by lazy { initClient(NOODOE_URL) }

        private val taipeiClient: Retrofit by lazy { initClient(TAIPEI_URL) }

        fun initClient(baseUri: String): Retrofit {

            return Retrofit.Builder()
                .baseUrl(baseUri)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        private fun getOkHttpClient() : OkHttpClient {

            val logging = HttpLoggingInterceptor()
            logging.level = Level.BODY

            val builder: OkHttpClient.Builder = OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(logging)

            return builder.build()
        }

        val loginService: LoginService by lazy {
            noodoeClient.create(LoginService::class.java)
        }

        val userService: UserService by lazy {
            noodoeClient.create(UserService::class.java)
        }

        val parkService: ParkService by lazy {
            taipeiClient.create(ParkService::class.java)
        }

    }
}