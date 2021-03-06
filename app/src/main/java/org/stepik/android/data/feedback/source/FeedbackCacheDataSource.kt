package org.stepik.android.data.feedback.source

import io.reactivex.Single
import java.io.File

interface FeedbackCacheDataSource {
    fun createSystemInfoData(fileName: String, fileContents: String): Single<File>
}