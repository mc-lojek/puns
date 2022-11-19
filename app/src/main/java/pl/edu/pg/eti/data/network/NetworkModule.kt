package pl.edu.pg.eti.data.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.edu.pg.eti.data.util.trustAllSslCertificates
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    fun provideApiRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.apply { level = HttpLoggingInterceptor.Level.BODY }
        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .trustAllSslCertificates()
            .build()

        return Retrofit.Builder()
            .baseUrl("http://51.83.130.165:8080")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}