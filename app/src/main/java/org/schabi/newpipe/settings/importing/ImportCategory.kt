package org.schabi.newpipe.settings.importing

import androidx.annotation.StringRes
import org.schabi.newpipe.R

enum class ImportCategory(@StringRes val titleRes: Int) {
    SUBSCRIPTIONS(R.string.import_category_subscriptions),
    WATCH_HISTORY(R.string.import_category_watch_history),
    PLAYLISTS(R.string.import_category_playlists),
    SEARCH_HISTORY(R.string.import_category_search_history),
    SETTINGS(R.string.import_category_settings)
}
