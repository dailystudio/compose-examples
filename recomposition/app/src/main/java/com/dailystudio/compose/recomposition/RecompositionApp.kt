package com.dailystudio.compose.recomposition

import com.dailystudio.devbricksx.app.DevBricksApplication

class RecompositionApp: DevBricksApplication() {

    override fun isDebugBuild(): Boolean {
        return BuildConfig.DEBUG
    }
}