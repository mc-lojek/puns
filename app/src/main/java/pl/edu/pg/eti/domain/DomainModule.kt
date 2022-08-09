package pl.edu.pg.eti.domain

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.edu.pg.eti.data.repository.LoginRepositoryImpl
import pl.edu.pg.eti.data.repository.RabbitRepositoryImpl
import pl.edu.pg.eti.domain.manager.SessionManager
import pl.edu.pg.eti.domain.repository.LoginRepository
import pl.edu.pg.eti.domain.repository.RabbitRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DomainModule {

    @Singleton
    @Provides
    fun provideLoginRepository(): LoginRepository {
        return LoginRepositoryImpl()
    }

    @Singleton
    @Provides
    fun provideRabbitRepository(): RabbitRepository {
        return RabbitRepositoryImpl()
    }

    @Singleton
    @Provides
    fun provideSessionManager(): SessionManager {
        return SessionManager()
    }

}