package org.stepik.android.presentation.download

import org.stepic.droid.persistence.model.DownloadItem

interface DownloadView {
    sealed class State {
        object Idle : State()
        object Loading : State()
        object Empty : State()
        data class DownloadedCoursesLoaded(val courses: List<DownloadItem>) : State()
    }

    fun setState(state: State)
    fun setBlockingLoading(isLoading: Boolean)
    fun setStorageInfo(contentSize: Long, avalableSize: Long, totalSize: Long)

    fun showRemoveTaskError()
}