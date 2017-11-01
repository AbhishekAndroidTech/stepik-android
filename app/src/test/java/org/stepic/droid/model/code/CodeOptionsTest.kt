package org.stepic.droid.model.code

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.stepic.droid.testUtils.TestingGsonProvider
import org.stepic.droid.testUtils.assertThatObjectParcelable

@RunWith(RobolectricTestRunner::class)
class CodeOptionsTest {

    private val gson = TestingGsonProvider.gson

    @Test
    fun simpleCodeOptionsSuccess() {
        val sample1 = ParcelableStringList().apply {
            addAll(listOf("1", "3"))
        }
        val sample2 = ParcelableStringList().apply {
            addAll(listOf("1", "2", "3", "4"))
        }
        val codeOptions = CodeOptions(
                limits = mapOf("java" to CodeLimit(3, 256)),
                executionMemoryLimit = 120,
                executionTimeLimit = 5,
                codeTemplates = hashMapOf("java" to "public static etc"),
                samples = listOf(sample1, sample2)
        )

        codeOptions.assertThatObjectParcelable<CodeOptions>()
    }


    @Test
    fun emptyListsAndMapsSuccess() {
        val codeOptions = CodeOptions(
                limits = emptyMap<String, CodeLimit>(),
                executionTimeLimit = 0,
                executionMemoryLimit = 0,
                codeTemplates = HashMap<String, String>(),
                samples = emptyList()
        )

        codeOptions.assertThatObjectParcelable<CodeOptions>()
    }

    @Test
    fun emptyCodeOptionsNull() {
        val optionsJson = "{}"
        val options = gson.fromJson(optionsJson, CodeOptions::class.java)

        Assert.assertNull(options)
    }
}
