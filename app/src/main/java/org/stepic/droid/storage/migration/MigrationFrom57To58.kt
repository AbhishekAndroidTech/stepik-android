package org.stepic.droid.storage.migration

import android.database.sqlite.SQLiteDatabase
import org.stepic.droid.storage.structure.DbStructureCourse

object MigrationFrom57To58 : Migration {
    override fun migrate(db: SQLiteDatabase) {
        db.execSQL("ALTER TABLE ${DbStructureCourse.TABLE_NAME} ADD COLUMN ${DbStructureCourse.Columns.OPTIONS} TEXT")
    }
}