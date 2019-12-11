package com.example.infosystask.activity

import android.app.PendingIntent.getActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.infosystask.adapter.ClickableRow
import com.example.infosystask.adapter.HomeAdapter
import com.example.infosystask.helper.InternetUtil
import com.example.infosystask.helper.applyVerticalWithDividerLinearLayoutManager
import com.example.infosystask.model.ApiResponse
import com.example.infosystask.model.FactResponse
import com.example.infosystask.model.Row
import com.example.infosystask.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_home.*
import com.example.infosystask.R

class HomeActivity : AppCompatActivity(), ClickableRow {


    lateinit var homeAdapter: HomeAdapter

    lateinit var homeViewModel: HomeViewModel

    lateinit var observer: Observer<ApiResponse<FactResponse, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setUpView()

        swipeRefresh.setOnRefreshListener {
            callApi()                 // refresh your list contents somehow
            swipeRefresh.isRefreshing = false   // reset the SwipeRefreshLayout (stop the loading spinner)
        }

    }

    fun setUpView() {

        /*
            We uses the following theme in styles.xml file
                Theme.AppCompat.Light.NoActionBar
        */

        // Set the toolbar as support action bar
    //    setSupportActionBar(toolbar)

        // Now get the support action bar
       /* val actionBar = supportActionBar

        // Set toolbar title/app title
        actionBar!!.title = "InfosysTask"

        // Set action bar elevation
        actionBar.elevation = 4.0F

        // Display the app icon in action bar/toolbar
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.setLogo(R.mipmap.ic_launcher)
        actionBar.setDisplayUseLogoEnabled(true)*/
        //init class
        homeViewModel = HomeViewModel()

        //init observer
        initObserver()

        //init rv
        rvHome.applyVerticalWithDividerLinearLayoutManager()

        callApi()

    }

    fun callApi(){

        initObserver()

        if (InternetUtil.isInternetOn()) {
            homeViewModel.getfactsData().observe(this, observer)

        } else {
            waitForInternet()
        }
    }

    fun initObserver() {

        observer = Observer { t ->

            if (t?.response != null) {

                var factResponse = t.response

                if (factResponse?.title != null) {

                    val toast = Toast.makeText(applicationContext, factResponse.title, Toast.LENGTH_LONG)
                    toast.show()
                        // Set toolbar title/app title

                    //toolbar.setTitle(factResponse.title)

                   //     actionBar!!.title = factResponse.title

                    factResponse.rows?.let {
                        displayData(it)
                    }

                } else {

                    rvHome.visibility = View.GONE
                    layNoInternet.visibility = View.GONE
                    proNearBy.visibility= View.GONE
                    layNoData.visibility = View.VISIBLE
                }

            } else {
                Log.e("warning", t?.error!!)
            }
        }
    }


    fun displayData(response: List<Row>) {


        //init adapter
        homeAdapter = HomeAdapter(response, this)
        //set data
        rvHome.adapter = homeAdapter

        rvHome.visibility = View.VISIBLE
        layNoInternet.visibility = View.GONE
        layNoData.visibility = View.GONE
        proNearBy.visibility=View.GONE

    }

    override fun onClickRow(item: Row) {

    }

    private fun waitForInternet() {

        InternetUtil.observe(this, Observer { status ->
            if (status!!) {
                rvHome.visibility = View.VISIBLE
                layNoInternet.visibility = View.GONE
                layNoData.visibility = View.GONE

            } else {
                layNoInternet.visibility = View.VISIBLE
                rvHome.visibility = View.GONE
                layNoData.visibility = View.GONE
                proNearBy.visibility=View.GONE
            }
        })
    }
}
