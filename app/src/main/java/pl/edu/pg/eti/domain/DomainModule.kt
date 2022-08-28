package pl.edu.pg.eti.domain

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.edu.pg.eti.data.repository.LoginRepositoryImpl
import pl.edu.pg.eti.data.repository.RegisterRepositoryImpl
import pl.edu.pg.eti.data.service.ApiService
import pl.edu.pg.eti.domain.repository.LoginRepository
import pl.edu.pg.eti.domain.repository.RegisterRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DomainModule {

    @Singleton
    @Provides
    fun provideLoginRepository(apiService: ApiService): LoginRepository {
        return LoginRepositoryImpl(apiService)
    }

    @Singleton
    @Provides
    fun provideRegisterRepository(apiService: ApiService): RegisterRepository {
        return RegisterRepositoryImpl(apiService)
    }

}