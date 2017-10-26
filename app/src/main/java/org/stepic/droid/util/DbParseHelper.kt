package org.stepic.droid.util

object DbParseHelper {

    private const val DEFAULT_SEPARATOR = "__,__"

    @JvmStatic
    @JvmOverloads
    fun parseStringToLongArray(str: String?, separator: String = DEFAULT_SEPARATOR): LongArray? {
        if (str == null) return null

        val strArray = str.split(separator)
        val result = LongArray(strArray.size)
        strArray.forEachIndexed { i, value -> result[i] = value.trim().toLong() }
        return result
    }

    @JvmStatic
    @JvmOverloads
    fun parseLongArrayToString(longArray: LongArray?, separator: String = DEFAULT_SEPARATOR): String? {
        if (longArray == null || longArray.isEmpty()) return null

        return longArray.joinToString(separator)
    }

    @JvmStatic
    @JvmOverloads
    fun parseStringArrayToString(array: Array<String>?, separator: String = DEFAULT_SEPARATOR): String? {
        if (array == null || array.isEmpty()) return null

        return array.joinToString(separator)
    }

    @JvmStatic
    @JvmOverloads
    fun parseStringToStringArray(str: String?, separator: String = DEFAULT_SEPARATOR): Array<String?>? {
        if (str == null) return null
        val strArray = str.split(separator)
        val result = arrayOfNulls<String>(strArray.size)
        strArray.forEachIndexed { i, el ->
            val trimmed = el.trim()
            result[i] = trimmed
        }
        return result
    }

}


