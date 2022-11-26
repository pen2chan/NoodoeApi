package com.penchan.noodoeapi.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.penchan.noodoeapi.MainActivity
import com.penchan.noodoeapi.base.ResponseResult
import com.penchan.noodoeapi.databinding.ActivityLoginBinding
import com.penchan.noodoeapi.repository.repo.LoginRepo
import com.penchan.noodoeapi.util.PreferenceHelper
import com.penchan.noodoeapi.util.PreferenceHelper.mail
import com.penchan.noodoeapi.util.PreferenceHelper.timeZone
import com.penchan.noodoeapi.util.PreferenceHelper.userName
import com.penchan.noodoeapi.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    lateinit var mLoginModel: LoginViewModel
    lateinit var mBinging: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinging = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinging.root)

        init()
        initLayout()
    }

    private fun init() {
        val repo = LoginRepo()
        val factory = ViewModelFactory(application, repo)
        mLoginModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)
    }

    private fun initLayout() {
        val prefs = PreferenceHelper.getPreference(this@LoginActivity, PreferenceHelper.PREF_NAME)
        val mail = prefs.mail
        if (mail != null) {
            mBinging.editUserName.setText(mail)
        }
    }

    fun onLoginClick(view: View) {
        var username = mBinging.editUserName.text.toString()
        var password = mBinging.editPassword.text.toString()

        if (username.isNotEmpty() && password.isNotEmpty()) {
            val params = HashMap<String, String>()
            params.put("username", username)
            params.put("password", password)

            mLoginModel.loginUser(params)
            mLoginModel.loginInfo.observe(this, Observer { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when(response) {
                        is ResponseResult.Success -> {
                            hideProgressBar()
                            response.data?.let {
                                val prefs = PreferenceHelper.getPreference(this@LoginActivity,
                                    PreferenceHelper.PREF_NAME)
                                prefs.edit {
                                    this.putString(PreferenceHelper.USER_MAIL, mBinging.editUserName.text.toString())
                                    this.putString(PreferenceHelper.USER_NAME, it.name)
                                    this.putString(PreferenceHelper.TIME_ZONE, it.timezone)
                                    this.putString(PreferenceHelper.OBJECT_ID, it.objectId)
                                    this.putString(PreferenceHelper.TOKEN, it.sessionToken)
                                    apply()
                                }
                                Intent(this@LoginActivity, MainActivity::class.java).also {
                                    startActivity(it)
                                    finish()
                                }
                            }
                        }
                        is ResponseResult.Error -> {
                            hideProgressBar()
                            response.message?.let { message ->
                                val snackbar = Snackbar.make(mBinging.root, message, Snackbar.LENGTH_LONG)
                                snackbar.show()
                            }
                        }
                        is ResponseResult.Loading -> {
                            showProgressBar()
                        }
                    }
                }
            })
        }
    }

    private fun hideProgressBar() {
        mBinging.progress.visibility = View.GONE
    }

    private fun showProgressBar() {
        mBinging.progress.visibility = View.VISIBLE
    }
}