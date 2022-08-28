package pl.edu.pg.eti.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import pl.edu.pg.eti.data.service.ApiService
import pl.edu.pg.eti.domain.util.trustAllSslCertificates
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Singleton
    @Provides
    fun provideApiRetrofit(): Retrofit {
        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .trustAllSslCertificates()
            .build()

        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")         //adres do zmiany jako stala
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}