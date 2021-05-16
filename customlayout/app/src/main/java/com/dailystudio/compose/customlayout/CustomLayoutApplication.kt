package com.dailystudio.compose.customlayout

import com.dailystudio.devbricksx.app.DevBricksApplication

class CustomLayoutApplication: DevBricksApplication() {

    override fun isDebugBuild(): Boolean {
        return BuildConfig.DEBUG
    }
}