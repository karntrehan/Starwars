package com.karntrehan.starwars.architecture

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import java.net.UnknownHostException


abstract class BaseFragment : Fragment() {

    abstract val layout: Int

    protected abstract val viewModel: BaseVM

    protected lateinit var parentActivity: AppCompatActivity

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @CallSuper
    override
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        parentActivity = activity as AppCompatActivity
        return inflater.inflate(layout, container, false)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenToLoadingState()
        listenForExceptions()
    }

    private fun listenToLoadingState() {
        viewModel.loading.observe(this, Observer { loading ->
            when (loading) {
                true -> showLoading()
                else -> hideLoading()
            }
        })
    }

    abstract fun hideLoading()

    abstract fun showLoading()

    @CallSuper
    private fun listenForExceptions() {
        viewModel.error.observe(this, Observer { error ->

            if (error == null) return@Observer

            //Make sure error is not propagated to all subsequent fragments
            viewModel.errorHandled()

            Log.e("BaseFragment", error.localizedMessage, error)
            when (error) {
                is UnknownHostException -> showNetworkError()
            }
        })
    }

    protected open fun showSnackBar(@StringRes reason: Int) {
        showSnackBar(getString(reason))
    }

    protected open fun showSnackBar(
            reason: String?,
            length: Int = Snackbar.LENGTH_SHORT
    ) {
        if (reason.isNullOrEmpty()) return

        activity?.window?.decorView?.let {
            Snackbar.make(it.findViewById<View>(android.R.id.content), reason, length).show()
        }
    }

    protected open fun showToast(reason: String?) {
        if (reason == null) return
        Toast.makeText(context, reason, Toast.LENGTH_LONG)
                .show()
    }

    protected open fun showToast(@StringRes reason: Int) {
        showToast(getString(reason))
    }

    //Open to allow extending fragment to override this behaviour
    protected open fun showNetworkError() {
        showToast(com.karntrehan.starwars.R.string.cannot_connect_to_internet)
    }

    //region region:Toolbar
    protected fun setUpToolbar(toolbar: Toolbar, title: String? = null) {
        parentActivity.setSupportActionBar(toolbar)
        parentActivity.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            if (!title.isNullOrBlank()) setTitle(title)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> parentActivity.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
    //endregion

    protected open fun popBack() = parentActivity.supportFragmentManager.popBackStack()

}