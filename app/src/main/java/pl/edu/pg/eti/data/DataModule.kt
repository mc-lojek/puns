package pl.edu.pg.eti.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import pl.edu.pg.eti.data.network.ApiService
import pl.edu.pg.eti.domain.util.trustAllSslCertificates
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {

}