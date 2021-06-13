package com.dailystudio.compose.gallery

import com.dailystudio.devbricksx.app.DevBricksApplication

class GalleryApplication : DevBricksApplication() {

    override fun isDebugBuild(): Boolean {
        return BuildConfig.DEBUG
    }

}
