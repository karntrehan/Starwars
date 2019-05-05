package com.karntrehan.starwars.architecture

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import java.net.UnknownHostException


abstract class BaseFragment : Fragment() {

    private val vmFactory: ViewModelProvider.Factory by lazy { provideVMFactory() }

    protected val vm: BaseVM by lazy { provideVM() }

    abstract val vmClass: Class<out BaseVM>

    abstract val layout: Int

    private lateinit var parentActivity: AppCompatActivity


    abstract fun provideVMFactory(): ViewModelProvider.Factory

    protected open fun provideVM() = ViewModelProviders.of(this, vmFactory).get(vmClass)

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
        vm.loading.observe(this, Observer { loading ->
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
        vm.error.observe(this, Observer { error ->

            if (error == null) return@Observer

            //Make sure error is not propagated to all subsequent fragments
            vm.errorHandled()

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

    protected open fun showNetworkError() {
        showToast(com.karntrehan.starwars.R.string.cannot_connect_to_internet)
    }

    //region region:Toolbar
    protected fun setUpToolbar(toolbar: Toolbar) {
        parentActivity.setSupportActionBar(toolbar)
        parentActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> parentActivity.finish()
        }
        return super.onOptionsItemSelected(item)
    }
    //endregion

}