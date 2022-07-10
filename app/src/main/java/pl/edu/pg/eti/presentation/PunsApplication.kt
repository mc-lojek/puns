package pl.edu.pg.eti.presentation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import pl.edu.pg.eti.domain.util.GLOBAL_TAG
import timber.log.Timber

@HiltAndroidApp
class PunsApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        plantTimberWithTag()
        // dzieki temu mozna fajnie sobie logi rzucac
        // dajesz po prostu:
        Timber.d("foobar")
    }

    private fun plantTimberWithTag() {
        Timber.plant(object : Timber.DebugTree() {
            override fun log(
                priority: Int, tag: String?, message: String, t: Throwable?
            ) {
                super.log(priority, "${GLOBAL_TAG}_$tag", message, t)
            }
        })
    }

}