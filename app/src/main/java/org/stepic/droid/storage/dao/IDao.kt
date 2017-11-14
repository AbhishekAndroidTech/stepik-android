package org.stepic.droid.storage.dao

import android.content.ContentValues

interface IDao<T> {
    fun insertOrUpdate(persistentObject: T)

    fun insertOrReplace(persistentObject: T)

    fun isInDb(persistentObject: T): Boolean

    fun isInDb(whereColumn: String, value: String): Boolean

    fun getAll(): List<T?>

//    fun insertOrUpdateAll(listOfPersistentObjects: List<T?>) //todo: Make insert ALL with supporting of inner fragments

    fun getAll(whereColumnName: String, whereValue: String): MutableList<T?>

    fun get(whereColumnName: String, whereValue: String): T?

    fun update(whereColumn: String, whereValue: String, contentValues: ContentValues)

    fun getAllInRange(whereColumn: String, commaSeparatedIds: String): List<T>

    fun remove(whereColumn: String, whereValue: String)

    fun removeAll()
}
