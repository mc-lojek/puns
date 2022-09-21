package pl.edu.pg.eti.domain

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.edu.pg.eti.data.network.ApiService
import pl.edu.pg.eti.data.repository.GameRepositoryImpl
import pl.edu.pg.eti.domain.manager.SessionManager
import pl.edu.pg.eti.domain.repository.GameRepository

@InstallIn(SingletonComponent::class)
@Module
class DomainModule {

    @Provides
    fun provideSessionManager(): SessionManager {
        val sm = SessionManager(
            "51.83.130.165",
            "admin",
            5672,
            "lamper123",
            "/puns"
        )
        return sm
    }

    @Provides
    fun provideGameRepository(service: ApiService): GameRepository {
        return GameRepositoryImpl(service)
    }


}