package com.penchan.noodoeapi.ui.user

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.penchan.noodoeapi.R
import com.penchan.noodoeapi.base.ResponseResult
import com.penchan.noodoeapi.databinding.FragmentUserBinding
import com.penchan.noodoeapi.repository.repo.UserRepo
import com.penchan.noodoeapi.util.PreferenceHelper
import com.penchan.noodoeapi.util.PreferenceHelper.mail
import com.penchan.noodoeapi.util.PreferenceHelper.objectId
import com.penchan.noodoeapi.util.PreferenceHelper.timeZone
import com.penchan.noodoeapi.util.PreferenceHelper.token
import com.penchan.noodoeapi.viewmodel.ViewModelFactory


class UserFragment : Fragment() {

    private lateinit var mUserModel: UserViewModel

    private var mRoot: View? = null
    private lateinit var mBinding: FragmentUserBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mRoot == null) {
            mBinding = FragmentUserBinding.inflate(inflater)
            mRoot = mBinding.root

            initLayout()
            init()
        }
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        initActionBar()
    }

    private fun  initLayout() {
        val prefs = PreferenceHelper.getPreference(requireActivity(), PreferenceHelper.PREF_NAME)
        mBinding.textMail.text = prefs.mail
        val timeZone = prefs.timeZone?.toIntOrNull() ?: 0
        val timeZoneArray = resources.getStringArray(R.array.timezone_menu_array)
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, timeZoneArray)
        mBinding.spinnerTimezone.adapter = adapter
        mBinding.spinnerTimezone.setSelection(timeZone)
        mBinding.spinnerTimezone.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                updateTimeZone(position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    private fun init() {
        val repo = UserRepo()
        val factory = ViewModelFactory(requireActivity().application, repo)
        mUserModel = ViewModelProvider(viewModelStore, factory).get(UserViewModel::class.java)
    }

    private fun initActionBar() {
        (activity as AppCompatActivity)?.supportActionBar?.apply {
            title = getString(R.string.label_user)

            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun updateTimeZone(position: Int) {
        val prefs = PreferenceHelper.getPreference(requireActivity(), PreferenceHelper.PREF_NAME)
        val timeZone = prefs.timeZone?.toIntOrNull() ?: 0
        val objectId = prefs.objectId
        val token = prefs.token

        if (timeZone != position &&
            objectId != null && token != null) {
            val params = HashMap<String, String>()
            params.put("timezone", position.toString())
            mUserModel.putUserData(params, objectId, token)

            mUserModel.actionResult.observe(viewLifecycleOwner, Observer { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when(response) {
                        is ResponseResult.Success -> {
                            hideProgressBar()
                            response.data?.let {
                                prefs.timeZone = position.toString()
                            }
                        }
                        is ResponseResult.Error -> {
                            hideProgressBar()
                            response.message?.let { message ->
                                val snackbar = Snackbar.make(mBinding.root, message, Snackbar.LENGTH_LONG)
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
        mBinding.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        mBinding.progressBar.visibility = View.VISIBLE
    }
}