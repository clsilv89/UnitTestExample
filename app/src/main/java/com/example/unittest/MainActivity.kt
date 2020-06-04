package com.example.unittest

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import java.util.regex.Pattern


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    lateinit var sharedPreferencers: SharedPreferencesHelper

    private var datePicker: DatePicker? = null
    private var nameText: EditText? = null
    private var emailText: EditText? = null

    private val emailValidator = EmailValidator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameText = findViewById(R.id.userNameInput)
        emailText = findViewById(R.id.emailInput)
        datePicker = findViewById(R.id.dateOfBirthInput)

        emailText?.addTextChangedListener(emailValidator)
        val sharePreferenceManager = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferencers = SharedPreferencesHelper(sharePreferenceManager)

    }

    private fun populateUi() {
        val sharedPreferencesEntry: SharedPreferencesEntry? = sharedPreferencers.getPersonalInfo()
        val dateOfBirth = sharedPreferencesEntry?.getDateOfBirth()
        nameText?.setText(sharedPreferencesEntry?.getName())
        emailText?.setText(sharedPreferencesEntry?.getEmail())

        dateOfBirth?.let {
            datePicker?.init(it[Calendar.YEAR], it[Calendar.MONTH], it[Calendar.DAY_OF_MONTH], null)
        }
    }

    fun onSaveClick(view: View) {
        if(!emailValidator.IsValid()) {
            emailText?.error = "Email Inválido"
            return
        }

        val name = nameText?.text.toString()
        val email = emailText?.text.toString()
        val dateOfBirth = Calendar.getInstance()
        datePicker?.let {
            datePicker ->
            dateOfBirth.set(datePicker.year, datePicker.month, datePicker.dayOfMonth)
        }
        val sharedPreferencesEntry = SharedPreferencesEntry(name, dateOfBirth, email)
        val isSuccess = sharedPreferencers.savePersonalInfo(sharedPreferencesEntry)

        if(isSuccess) {
            Toast.makeText(this, "informações salvas", Toast.LENGTH_LONG).show()
        }
    }

    fun onRevertClick(view: View) {
        populateUi()
        Toast.makeText(this, "Informações recuperadas", Toast.LENGTH_LONG).show()
    }
}

class EmailValidator : TextWatcher {

    private val EMAIL_PATTERN: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    private var isValid = false

    fun IsValid(): Boolean {
        return isValid
    }

    fun isValidEmail(email: CharSequence?): Boolean {
        return email != null && EMAIL_PATTERN.matcher(email).matches()
    }

    override fun afterTextChanged(s: Editable?) {
        isValid = isValidEmail(s)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        /*Não Usado*/
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        /*Não Usado*/
    }

}

class SharedPreferencesHelper(private var sharedPreferences: SharedPreferences) {

    val KEY_NAME = "key_name"
    val KEY_DOB = "key_dob_millis"
    val KEY_EMAIL = "key_email"

    fun savePersonalInfo(SharedPreferencesEntry: SharedPreferencesEntry): Boolean {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        SharedPreferencesEntry.getDateOfBirth()?.timeInMillis?.let { editor.putLong(KEY_DOB, it) }
        editor.putString(KEY_NAME, SharedPreferencesEntry.getName())
        editor.putString(KEY_EMAIL, SharedPreferencesEntry.getEmail())

        return editor.commit()
    }

    fun getPersonalInfo(): SharedPreferencesEntry? {
        val name: String = sharedPreferences.getString(KEY_NAME, "").toString()
        val email: String = sharedPreferences.getString(KEY_EMAIL, "").toString()
        val doBtoMills: Long =
            sharedPreferences.getLong(KEY_DOB, Calendar.getInstance().timeInMillis)
        val dateOfBirth = Calendar.getInstance()
        dateOfBirth.timeInMillis = doBtoMills

        return SharedPreferencesEntry(name, dateOfBirth, email)
    }
}

class SharedPreferencesEntry(name: String, dateOfBirth: Calendar, email: String) {
    private var name = name
    private var dateOfBirth = dateOfBirth
    private var email = email

    fun getName(): String? {
        return name
    }

    fun getDateOfBirth(): Calendar? {
        return dateOfBirth
    }

    fun getEmail(): String? {
        return email
    }
}