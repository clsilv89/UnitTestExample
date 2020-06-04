package com.example.unittest

import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test

class EmailValidatorTest {
    private val emailValidator = EmailValidator()
    @Test
    fun emailValidatorCorrectEmail() {
        assertTrue(emailValidator.isValidEmail("name@email.com"))
    }

    @Test
    fun emailValidatorCorrectSubDomain() {
        assertTrue(emailValidator.isValidEmail("name@email.co.uk"))
    }

    @Test
    fun emailValidatodWrongSufix() {
        assertFalse(emailValidator.isValidEmail("name@email"))
    }

    @Test
    fun emailValidatorExtraDots() {
        assertFalse(emailValidator.isValidEmail("name@email..com"))
    }

    @Test
    fun emailValidatorInvalidOrNoPrefix() {
        assertFalse(emailValidator.isValidEmail("@email.com"))
    }

    @Test
    fun emailvalidatorEmptyField(){
        assertFalse(emailValidator.isValidEmail(""))
    }

    @Test
    fun emailValidatorNull() {
        assertFalse(emailValidator.isValidEmail(null))
    }
}