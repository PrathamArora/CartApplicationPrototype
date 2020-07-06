package com.sew.smartkart.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sew.smartkart.R
import com.sew.smartkart.view.adapter.ProductsListAdapter
import com.sew.smartkart.viewmodel.ProductListViewModel
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.progress_bar_fullscreen.*

class ProductListActivity : BaseActivity() {

    private var mAdapter: ProductsListAdapter? = null
    private var mProductListViewModel: ProductListViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        mProductListViewModel = ViewModelProvider(this).get(ProductListViewModel::class.java)

        mProductListViewModel!!.init(this)

        setObservers()

        initUI()
    }

    override fun onResume() {
        super.onResume()
        mAdapter?.notifyDataSetChanged()
    }

    private fun initUI() {

        imgCartButton.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        mAdapter = ProductsListAdapter(
            mProductListViewModel!!.getProductsDataSet().value!!,
            this,
            mProductListViewModel!!
        )

        rvProductList.layoutManager = LinearLayoutManager(this)
        rvProductList.adapter = mAdapter
    }

    private fun setObservers() {
        mProductListViewModel?.getProductsDataSet()?.observe(this, Observer {
            mAdapter?.notifyDataSetChanged()
        })

        mProductListViewModel?.isUpdating()?.observe(this, Observer {
            pbText.text = it.first
            if (it.second) {
                pbFullScreen.visibility = View.VISIBLE
            } else {
                pbFullScreen.visibility = View.GONE
            }
        })
    }
}
