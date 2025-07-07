package org.schabi.newpipe.settings.importing

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.schabi.newpipe.streams.io.StoredFileHelper
import org.schabi.newpipe.util.ZipHelper
import java.io.File

object ImportCategoryHelper {
    @JvmStatic
    fun getCounts(context: Context, file: StoredFileHelper): Map<ImportCategory, Int> {
        val counts = mutableMapOf<ImportCategory, Int>()
        val tempDb = File.createTempFile("import", ".db", context.cacheDir)
        try {
            if (ZipHelper.extractFileFromZip(file, tempDb.path, "newpipe.db")) {
                val db = SQLiteDatabase.openDatabase(tempDb.path, null, SQLiteDatabase.OPEN_READONLY)
                counts[ImportCategory.SUBSCRIPTIONS] = queryCount(db, "subscriptions")
                counts[ImportCategory.WATCH_HISTORY] = queryCount(db, "stream_history")
                counts[ImportCategory.SEARCH_HISTORY] = queryCount(db, "search_history")
                counts[ImportCategory.PLAYLISTS] = queryCount(db, "playlists")
                db.close()
            }

            val tempSettings = File.createTempFile("import", ".settings", context.cacheDir)
            if (ZipHelper.extractFileFromZip(file, tempSettings.path, "newpipe.settings")) {
                counts[ImportCategory.SETTINGS] = 1
            }
            tempSettings.delete()
        } catch (e: Exception) {
            // ignore and return empty counts
        } finally {
            tempDb.delete()
        }
        return counts
    }

    @JvmStatic
    private fun queryCount(db: SQLiteDatabase, table: String): Int {
        return db.rawQuery("SELECT COUNT(*) FROM $table", null).use { c ->
            if (c.moveToFirst()) c.getInt(0) else 0
        }
    }
}
