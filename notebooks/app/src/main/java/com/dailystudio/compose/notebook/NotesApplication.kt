package com.dailystudio.compose.notebook

import com.dailystudio.devbricksx.app.DevBricksApplication
import com.dailystudio.devbricksx.development.Logger
import com.facebook.stetho.Stetho

class NotesApplication : DevBricksApplication() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.USE_STETHO) {
            Stetho.initializeWithDefaults(this)
        }

        Logger.info("application is running in %s mode.",
            if (BuildConfig.DEBUG) "DEBUG" else "RELEASE")
    }

    override fun isDebugBuild(): Boolean {
        return BuildConfig.DEBUG
    }

}
