package org.stepic.droid.features.deadlines.storage.structure

import android.database.sqlite.SQLiteDatabase

object DbStructureDeadlinesBanner {
    const val DEADLINES_BANNER = "deadlines_banner"

    object Columns {
        const val COURSE_ID = "course_id"
    }

    fun createTable(db: SQLiteDatabase) {
        val sql = """
            CREATE TABLE IF NOT EXISTS $DEADLINES_BANNER (
                ${Columns.COURSE_ID} LONG PRIMARY KEY
            )""".trimIndent()
        db.execSQL(sql)
    }
}