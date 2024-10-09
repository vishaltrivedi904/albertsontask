package com.example.albertsontask.ui.main

import android.accounts.NetworkErrorException
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.albertsontask.R
import com.example.albertsontask.adapters.UserAdapter
import com.example.albertsontask.calbacks.OnItemClickListener
import com.example.albertsontask.common.hideKeyboard
import com.example.albertsontask.common.setLightStatusBar
import com.example.albertsontask.data.api.NetworkResponse
import com.example.albertsontask.data.model.profile.Profile
import com.example.albertsontask.data.model.user.Result
import com.example.albertsontask.data.model.user.UserModel
import com.example.albertsontask.databinding.ActivityMainBinding
import com.example.albertsontask.ui.profile.ProfileActivity
import com.example.albertsontask.utils.NetworkErrorUtil
import com.example.albertsontask.utils.OnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnItemClickListener<Result>,
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityMainBinding
    private val mViewModel: MainViewModel by viewModels()
    private var adapter: UserAdapter? = null
    private var progressDialog: ProgressDialog? = null
    private val singleClickListener = object : OnSingleClickListener() {
        override fun onSingleClick(view: View) {
            if (view.id == binding.searchButton.id) {
                val results = binding.search.text.toString().trim()
                if (TextUtils.isEmpty(results)) {
                    mViewModel.results = 10
                    mViewModel.page = 1
                    adapter?.apply {
                        clear()
                    }
                    mViewModel.type=0
                    getUsers(mViewModel.page, mViewModel.results)
                } else {
                    if (results.toInt() > 5000 || results.toInt() < 10) {
                        Toast.makeText(
                            this@MainActivity,
                            getString(R.string.please_enter_result_request_count_between_10_to_5000),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        mViewModel.page = 1
                        adapter?.apply {
                            clear()
                        }
                        mViewModel.type=0
                        mViewModel.results = results.toInt()
                        getUsers(mViewModel.page, mViewModel.results)
                    }


                }

            }
        }
    }


    private val scrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (mViewModel.isLoading) {
                    if (adapter!!.itemCount > 2) {
                        if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == adapter!!.itemCount - 1) {
                            mViewModel.isLoading = false
                            mViewModel.page += 1
                            mViewModel.type=2
                            getUsers(mViewModel.page, mViewModel.results)
                        }
                    }
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setLightStatusBar(false)
        initListener()
        mViewModel.data.observe(this, UsersObserver())
        mViewModel.type=0
        getUsers(mViewModel.page, mViewModel.results)
    }

    private fun initListener() {
        adapter = UserAdapter(this, mutableListOf(), this)
        binding.content.userListRecycler.adapter = adapter
        binding.content.userListRecycler.setHasFixedSize(true)
        binding.content.userListRecycler.addOnScrollListener(scrollListener)
        binding.content.root.setOnRefreshListener(this)
        binding.searchButton.setOnClickListener(singleClickListener)
    }

    fun getUsers(
        page: Int = 1,
        results: Int = 10,
        inc: String = "name,location,picture,gender,email,phone"
    ) {
        mViewModel.getUsers(page, results, inc)
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }

    override fun onItemClicked(data: Result) {
        val address="${data.location!!.street.number} ${data.location.street.name}, " + "${data.location.city}, ${data.location.state}, " + "${data.location.country}, ${data.location.postcode}".capitalize(Locale.ENGLISH)
        val profile=Profile(data.gender,data.email,data.phone,address,"${data.name?.title} ${data.name?.first} ${data.name?.last}",data.picture!!.large)
        val bundle = Bundle()
        bundle.putParcelable("user", profile)
        startActivity(
            Intent(this@MainActivity, ProfileActivity::class.java).putExtra(
                "BUNDLE",
                bundle
            )
        )
    }

    private fun showNoData(data: MutableList<Result>, message: String) {
        if (data.isEmpty() && mViewModel.page == 1) {
            binding.content.userListRecycler.visibility = View.GONE
            binding.content.noData.visibility = View.VISIBLE
            binding.content.messageTv.text = message
        } else {
            binding.content.userListRecycler.visibility = View.VISIBLE
            binding.content.noData.visibility = View.GONE

        }
    }

    override fun onRefresh() {
        mViewModel.page = 1
        adapter?.apply {
            clear()
        }
        mViewModel.type=1
        getUsers(mViewModel.page, mViewModel.results)
    }

    private fun showLoading() {
        progressDialog = ProgressDialog(this,R.style.AppCompatAlertDialogStyle)
        progressDialog!!.setMessage(getString(R.string.please_wait))
        progressDialog!!.setCancelable(false)
        if (!progressDialog!!.isShowing) {
            progressDialog!!.show()
        }
    }

    override fun onStop() {
        hideLoading()
        super.onStop()
    }

    private fun hideLoading() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
    }

    open inner class UsersObserver :
        Observer<NetworkResponse<UserModel>> {
        override fun onChanged(value: NetworkResponse<UserModel>) {
            when (value) {
                is NetworkResponse.Loading -> {
                    hideKeyboard()
                    when (mViewModel.type) {
                        0 -> {
                            showLoading()
                        }

                        1 -> {
                            binding.content.root.isRefreshing = true
                        }

                        2 -> {
                            binding.content.progressBar.visibility = View.VISIBLE
                        }
                    }
                }

                is NetworkResponse.Success -> {
                    hideLoading()
                    binding.content.root.isRefreshing = false
                    binding.content.progressBar.visibility = View.GONE
                    val apiResponse = value.data!!
                    mViewModel.isLoading = apiResponse.results.isNotEmpty()
                    val recyclerViewState: Parcelable =
                        binding.content.userListRecycler.layoutManager!!.onSaveInstanceState()!!
                    adapter!!.setItems(apiResponse.results)
                    binding.content.userListRecycler.layoutManager!!.onRestoreInstanceState(
                        recyclerViewState
                    );
                    showNoData(apiResponse.results, "")
                }

                is NetworkResponse.Error -> {
                    hideLoading()
                    binding.content.progressBar.visibility = View.GONE
                    binding.content.root.isRefreshing = false
                    showNoData(mutableListOf(), value.message ?: getString(R.string.no_users))
                    Toast.makeText(this@MainActivity,value.message ?: getString(R.string.no_users),Toast.LENGTH_SHORT).show()
                }

                is NetworkResponse.Failure -> {
                    hideLoading()
                    binding.content.progressBar.visibility = View.GONE
                    binding.content.root.isRefreshing = false
                    showNoData(mutableListOf(), NetworkErrorUtil.getExceptionMessage(this@MainActivity,value.throwable!!)?: getString(
                        R.string.something_went_wrong_please_try_again_later
                    ))
                    Toast.makeText(this@MainActivity,NetworkErrorUtil.getExceptionMessage(this@MainActivity,value.throwable!!) ?: getString(R.string.something_went_wrong_please_try_again_later),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            hideKeyboard()
        }
        return super.dispatchTouchEvent(ev)
    }



}