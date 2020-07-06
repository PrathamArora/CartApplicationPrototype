
package com.sew.smartkart.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sew.smartkart.R
import com.sew.smartkart.view.adapter.CartAdapter
import com.sew.smartkart.viewmodel.CartViewModel
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_cart.pbFullScreen
import kotlinx.android.synthetic.main.progress_bar_fullscreen.*

class CartActivity : AppCompatActivity() {

    private var mAdapter: CartAdapter? = null
    private var mCartViewModel: CartViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        mCartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)

        mCartViewModel?.init(this)

        setObservers()

        initUI()
    }

    private fun initUI() {
        imgBackButton.setOnClickListener {
            finish()
        }

        btnBuyNow.setOnClickListener {
            mCartViewModel?.checkOut(this)
            startActivity(Intent(this, OrderSuccessfulActivity::class.java))
            finish()
        }

        mAdapter = CartAdapter(mCartViewModel!!.getCartDetails().value!!, this, mCartViewModel!!)

        rvCartList.layoutManager = LinearLayoutManager(this)
        rvCartList.adapter = mAdapter
    }

    private fun setObservers() {
        mCartViewModel?.getCartDetails()?.observe(this, Observer {
            if (it.isNullOrEmpty()) {
                rvCartList.visibility = View.GONE
                imgEmptyCart.visibility = View.VISIBLE
            } else {
                rvCartList.visibility = View.VISIBLE
                imgEmptyCart.visibility = View.GONE
                mAdapter?.notifyDataSetChanged()
            }
        })

        mCartViewModel?.isUpdating()?.observe(this, Observer {
            pbText.text = it.first
            if (it.second) {
                pbFullScreen.visibility = View.VISIBLE
            } else {
                pbFullScreen.visibility = View.GONE
            }
        })

    }
}
