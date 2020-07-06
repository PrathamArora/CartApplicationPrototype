package com.sew.smartkart.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.sew.smartkart.R
import com.sew.smartkart.service.model.CartDetailsModel
import com.sew.smartkart.viewmodel.OrderSuccessfulViewModel
import kotlinx.android.synthetic.main.activity_order_successful.*
import kotlinx.android.synthetic.main.card_ordered_item_details.view.*
import kotlinx.android.synthetic.main.progress_bar_fullscreen.*

class OrderSuccessfulActivity : AppCompatActivity() {

    private var mOrderSuccessfulViewModel: OrderSuccessfulViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_successful)

        mOrderSuccessfulViewModel =
            ViewModelProvider(this).get(OrderSuccessfulViewModel::class.java)

//        mOrderSuccessfulViewModel =
//            ViewModelProviders.of(this).get(OrderSuccessfulViewModel::class.java)

        btnShoppingDone.setOnClickListener {
            onBackPressed()
        }

        initViewModel()

        setObservers()
    }

    override fun onBackPressed() {
        mOrderSuccessfulViewModel?.completeTransaction(this)
        val intent = Intent(this, ProductListActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        super.onBackPressed()
    }

    private fun setObservers() {
        mOrderSuccessfulViewModel?.getCartDetails()?.observe(this,
            Observer {
                initUI(it)
            })

        mOrderSuccessfulViewModel?.isUpdating()?.observe(this,
            Observer {
                pbText.text = it.first
                if (it.second) {
                    pbFullScreen.visibility = View.VISIBLE
                } else {
                    pbFullScreen.visibility = View.GONE
                }
            })

    }

    @SuppressLint("SetTextI18n")
    private fun initUI(mCartList: ArrayList<CartDetailsModel>) {

        llAllOrders.removeAllViews()
        var totalAmount: Long = 0

        for (singleCartDetailsModel in mCartList) {
            val cardOrderItem = LayoutInflater.from(this)
                .inflate(R.layout.card_ordered_item_details, llAllOrders, false)

            Glide.with(this).load(singleCartDetailsModel.productURL).into(cardOrderItem.imgProduct)

            cardOrderItem.tvProductTitle.text = singleCartDetailsModel.productName
            cardOrderItem.tvProductCurrentQuantity.text =
                singleCartDetailsModel.productCurrentQuantity.toString()
            cardOrderItem.tvProductPrice.text =
                BaseActivity.getAmountInIndianCurrency(singleCartDetailsModel.productDiscountedPrice!!)

            totalAmount += (singleCartDetailsModel.productDiscountedPrice?.toLong()!!
                    * singleCartDetailsModel.productCurrentQuantity?.toLong()!!)
            llAllOrders.addView(cardOrderItem)
        }

        tvTotalAmount.text =
            BaseActivity.TOTAL_AMOUNT_PREFIX + BaseActivity.getAmountInIndianCurrency(totalAmount.toString())
    }


    private fun initViewModel() {
        mOrderSuccessfulViewModel?.init(this)
    }
}
