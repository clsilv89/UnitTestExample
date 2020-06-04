package com.example.unittest

import android.content.SharedPreferences
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.util.*


@RunWith(MockitoJUnitRunner::class)
class SharedPreferencesHelperTest {
    private val TESTE_NAME = "Test Name"
    private val TESTE_EMAIL = "test@email.com"
    private val TEST_DOB = Calendar.getInstance()

    private var sharedPreferencesEntry: SharedPreferencesEntry? = null
    private var mockSharedPreferencesHelper: SharedPreferencesHelper? = null
    private var failedSharedPreferencesHelper: SharedPreferencesHelper? = null

    init {
        TEST_DOB.set(1989, 4, 11)
    }

    @Mock
    var mockSharedPreferences: SharedPreferences? = null

    @Mock
    var failSharedPreferences: SharedPreferences? = null

    @Mock
    var mockEditor: SharedPreferences.Editor? = null

    @Mock
    var failEditor: SharedPreferences.Editor? = null

    @Before
    fun initMock() {
        sharedPreferencesEntry = SharedPreferencesEntry(TESTE_NAME, TEST_DOB, TESTE_EMAIL)

        mockSharedPreferencesHelper = createMockSharedPreference()

        failedSharedPreferencesHelper = createFailedSharedPreferences()
    }

    @Test
    fun saveAndReadInfos() {
        val success =
            sharedPreferencesEntry?.let { mockSharedPreferencesHelper?.savePersonalInfo(it) }
        assertThat("Verificando se SharedPreferencesEntry retorna true", success, `is`(true))

        val savedPreferencesEntry= mockSharedPreferencesHelper?.getPersonalInfo()

        assertThat(
            "Verificando se as informação foram salvas e lidas corretamente",
            sharedPreferencesEntry?.getName(),
            `is`(equalTo(savedPreferencesEntry?.getName()))
        )
        assertThat(
            "Verificando se as informação foram salvas e lidas corretamente",
            sharedPreferencesEntry?.getEmail(),
            `is`(equalTo(savedPreferencesEntry?.getEmail()))
        )
        assertThat(
            "Verificando se as informação foram salvas e lidas corretamente",
            sharedPreferencesEntry?.getDateOfBirth(),
            `is`(equalTo(savedPreferencesEntry?.getDateOfBirth())
            )
        )
    }

    @Test
    fun saveInfoReturnFalse() {
        val success =
            sharedPreferencesEntry?.let { failedSharedPreferencesHelper?.savePersonalInfo(it) }
        assertThat(
            "Verifica se salvar para um sharedpreferences falho retorna falso",
            success,
            `is`(false)
        )
    }

    private fun createMockSharedPreference(): SharedPreferencesHelper? {
        val sharedPreferencesHelper = mockSharedPreferences?.let { SharedPreferencesHelper(it) }

        `when`<Any>(
            mockSharedPreferences?.getString(
                eq(sharedPreferencesHelper?.KEY_NAME),
                anyString()
            )
        )
            .thenReturn(sharedPreferencesEntry?.getName())
        `when`<Any>(
            mockSharedPreferences?.getString(
                eq(sharedPreferencesHelper?.KEY_EMAIL),
                anyString()
            )
        )
            .thenReturn(sharedPreferencesEntry?.getEmail())
        `when`<Any>(
            mockSharedPreferences?.getLong(
                eq(sharedPreferencesHelper?.KEY_DOB),
                anyLong()
            )
        )
            .thenReturn(sharedPreferencesEntry?.getDateOfBirth()?.timeInMillis)

        `when`<Any>(mockEditor?.commit()).thenReturn(true)
        `when`<Any>(mockSharedPreferences?.edit()).thenReturn(mockEditor)

        return mockSharedPreferences?.let { SharedPreferencesHelper(it) }
    }

    private fun createFailedSharedPreferences(): SharedPreferencesHelper? {
        `when`<Any>(failEditor?.commit()).thenReturn(false)
        `when`<Any>(failSharedPreferences?.edit()).thenReturn(failEditor)

        return failSharedPreferences?.let { SharedPreferencesHelper(it) }
    }

}
