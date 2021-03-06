package com.dailystudio.compose.gallery.db

import android.os.Build
import androidx.paging.PagingSource
import androidx.room.Query
import com.dailystudio.devbricksx.annotations.*
import com.dailystudio.devbricksx.database.DateConverter
import com.dailystudio.devbricksx.development.Logger
import com.dailystudio.compose.gallery.api.data.Links
import com.dailystudio.compose.gallery.api.data.Photo
import java.lang.IllegalArgumentException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


@ViewModel
@RoomCompanion(
    primaryKeys = ["id"],
    extension = PhotoItemDaoExtension::class,
    converters = [DateConverter::class],
    database = "unsplash"
)
class PhotoItem(
    @JvmField val id: String,
    @JvmField var cachedIndex: String,
    @JvmField val created: Date,
    @JvmField val lastModified: Date,
    @JvmField val author: String,
    @JvmField val description: String?,
    @JvmField val thumbnailUrl: String,
    @JvmField val downloadUrl: String
) {
    companion object {
        fun fromUnsplashPhoto(photo: Photo): PhotoItem {
            val iso8601: DateFormat =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX",
                        Locale.getDefault())
                } else {
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ",
                        Locale.getDefault())
                }

            val created = try {
                iso8601.parse(photo.created_at)
            } catch (e: IllegalArgumentException) {
                Logger.error("failed to parse date from [${photo.created_at}: $e")

                Date(System.currentTimeMillis())
            }

            val lastModified = try {
                iso8601.parse(photo.updated_at)
            } catch (e: IllegalArgumentException) {
                Logger.error("failed to parse date from [${photo.updated_at}: $e")

                Date(System.currentTimeMillis())
            }

            return PhotoItem(photo.id,
                "0.0",
                created,
                lastModified,
                photo.user.name,
                photo.description,
                photo.urls.regular,
                photo.urls.full)
        }

    }
}

@DaoExtension(entity = PhotoItem::class)
interface PhotoItemDaoExtension {

    @Query("SELECT * FROM photoitem ORDER BY cached_index ASC")
    fun listPhotos(): PagingSource<Int, PhotoItem>

    @Query("DELETE FROM photoitem")
    fun deletePhotos()

}

@RoomCompanion(
    primaryKeys = [ "keyword" ],
    extension = UnsplashPageLinksDaoExtension::class,
    database = "unsplash"
)
data class UnsplashPageLinks(
    @JvmField val keyword: String,
    @JvmField val first: String? = null,
    @JvmField val prev: String? = null,
    @JvmField val next: String? = null,
    @JvmField val last: String? = null,
) {
    companion object {

        fun fromUnsplashLinks(links: Links?,
                              keyword: String
        ): UnsplashPageLinks {
            if (links == null) {
                return UnsplashPageLinks(keyword)
            }

            return UnsplashPageLinks(
                keyword,
                links.first,
                links.prev,
                links.next,
                links.last)
        }

    }
}


@DaoExtension(entity = UnsplashPageLinks::class)
interface UnsplashPageLinksDaoExtension {

    @Query("SELECT * FROM unsplashpagelinks WHERE keyword = :keyword")
    fun linksForKeyword(keyword: String): UnsplashPageLinks

    @Query("DELETE FROM unsplashpagelinks WHERE keyword = :keyword")
    fun deleteLinksForKeyword(keyword: String)

}
