package com.penchan.noodoeapi.ui.park

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.penchan.noodoeapi.MainActivity
import com.penchan.noodoeapi.R
import com.penchan.noodoeapi.base.ResponseResult
import com.penchan.noodoeapi.databinding.FragmentParkBinding
import com.penchan.noodoeapi.repository.repo.LoginRepo
import com.penchan.noodoeapi.repository.repo.ParkRepo
import com.penchan.noodoeapi.ui.login.LoginViewModel
import com.penchan.noodoeapi.viewmodel.ViewModelFactory

class ParkFragment : Fragment() {

    private lateinit var mParkModel: ParkViewModel
    private lateinit var mAdapter: ParkAdapter

    private var mRoot: View? = null
    private lateinit var mBinding: FragmentParkBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mRoot == null) {
            mBinding = FragmentParkBinding.inflate(inflater)
            mRoot = mBinding.root

            initRecyclerView()
            init()
        }
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        initActionBar()
        initOptionsMenu()
    }

    private fun initOptionsMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.munu_park, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId) {
                    R.id.action_timezone -> {
                        activity?.findNavController(R.id.nav_host_fragment_content_main)?.navigate(R.id.nav_user)
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun initRecyclerView() {
        // Disable SwipeRefresh
        mBinding.swipeRefresh.isEnabled = false

        val linearLayoutManager = LinearLayoutManager(context)
        mBinding.recyclerView.itemAnimator = DefaultItemAnimator()
        mBinding.recyclerView.layoutManager = linearLayoutManager

        mAdapter = ParkAdapter()
        mBinding.recyclerView.adapter = mAdapter
        mBinding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun initActionBar() {
        (activity as AppCompatActivity)?.supportActionBar?.apply {
            title = getString(R.string.label_park)

            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun init() {
        val repo = ParkRepo()
        val factory = ViewModelFactory(requireActivity().application, repo)
        mParkModel = ViewModelProvider(viewModelStore, factory).get(ParkViewModel::class.java)

        mParkModel.queryList()
        mParkModel.parkDescList.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let { response ->
                when(response) {
                    is ResponseResult.Success -> {
                        hideProgressBar()
                        response.data?.let {
                            mAdapter.updateList(it)
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

    private fun hideProgressBar() {
        mBinding.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        mBinding.progressBar.visibility = View.VISIBLE
    }
}