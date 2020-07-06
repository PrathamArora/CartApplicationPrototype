package com.sew.smartkart.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.sew.customcounterlibrary.CustomCounter
import com.sew.smartkart.R
import com.sew.smartkart.service.model.SingleProductDetailsModel
import com.sew.smartkart.viewmodel.SingleProductDetailsViewModel
import kotlinx.android.synthetic.main.activity_single_product_details.*
import kotlinx.android.synthetic.main.activity_single_product_details.imgCartButton
import kotlinx.android.synthetic.main.activity_single_product_details.pbFullScreen
import kotlinx.android.synthetic.main.progress_bar_fullscreen.*

class SingleProductDetailsActivity : BaseActivity() {

    private var mSingleProductDetailsViewModel: SingleProductDetailsViewModel? = null
    private var mProductID = DEFAULT_PRODUCT_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_product_details)

        val intent = intent
        mProductID = intent.getStringExtra(PRODUCT_ID_INTENT_KEY)!!

        mSingleProductDetailsViewModel =
            ViewModelProvider(this).get(SingleProductDetailsViewModel::class.java)

        initViewModel()

        setObservers()
    }

    private fun setObservers() {
        mSingleProductDetailsViewModel?.isUpdating()?.observe(this,
            Observer {
                pbText.text = it.first
                if (it.second) {
                    pbFullScreen.visibility = View.VISIBLE
                } else {
                    pbFullScreen.visibility = View.GONE
                }
            })

        mSingleProductDetailsViewModel?.getSingleProductDetailsModel()?.observe(this,
            Observer {
                if (it != null) {
                    initUI(it)
                }
            })
    }

    override fun onResume() {
        super.onResume()
        val singleProductDetailsModel = mSingleProductDetailsViewModel?.getSingleProductDetailsModel()?.value
        if(singleProductDetailsModel != null){
            initUI(singleProductDetailsModel)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initUI(singleProductDetailsModel: SingleProductDetailsModel) {

        tvActionBarTitle.text = "Product Details"
        Glide.with(this).load(singleProductDetailsModel.productURL).into(imgProduct)
        tvProductTitle.text = singleProductDetailsModel.productName

        manageProductPricing(singleProductDetailsModel)

        manageLikeButton(singleProductDetailsModel)

        manageShoppingCart(singleProductDetailsModel)

        imgCartButton.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        imgBackButton.setOnClickListener {
            finish()
        }

    }

    private fun manageShoppingCart(singleProductDetailsModel: SingleProductDetailsModel) {
        if (singleProductDetailsModel.productMaxQuantity == 0) {
            llAddToCartLayout.visibility = View.GONE
        } else {
            btnOutOfStock.visibility = View.GONE
            btnAddToCart.setOnClickListener {

                mSingleProductDetailsViewModel?.updateItemQuantityInCart(
                    this,
                    singleProductDetailsModel,
                    counterValue.getCurrentValue()
                )

                if (counterValue.getCurrentValue() == 0) {
                    showToast("Nothing to add in cart")
                } else {
                    showToast("Cart Updated")
                }
            }
            manageCounter(singleProductDetailsModel)
        }
    }

    private fun manageCounter(singleProductDetailsModel: SingleProductDetailsModel) {
        counterValue.setMaxValue(singleProductDetailsModel.productMaxQuantity!!)
        counterValue.setMinValue(0)
        counterValue.setInitialValue(
            mSingleProductDetailsViewModel?.getCounterInitialValue(
                this,
                singleProductDetailsModel
            )!!
        )

        counterValue.setNumberTextSize(20.0f)

        counterValue.setOnValueChangeListener(
            object : CustomCounter.OnValueChangeListener {
                override fun onValueChange(oldValue: Int, newValue: Int) {
                    if (oldValue == singleProductDetailsModel.productMaxQuantity && newValue == oldValue) {
                        showToast("You have reached the max quantity.")
                    }
                }

            }
        )
    }

    private fun manageLikeButton(singleProductDetailsModel: SingleProductDetailsModel) {

        btnLikeButton.isSelected =
            mSingleProductDetailsViewModel?.getFavoriteDetail(this, singleProductDetailsModel)!!

        btnLikeButton.setOnClickListener {
            mSingleProductDetailsViewModel?.updateFavorite(this, singleProductDetailsModel)
            it.isSelected =
                mSingleProductDetailsViewModel?.getFavoriteDetail(this, singleProductDetailsModel)!!
        }
    }

    private fun manageProductPricing(singleProductDetailsModel: SingleProductDetailsModel) {
        if (singleProductDetailsModel.productMRP == singleProductDetailsModel.productDiscountedPrice) {
            tvMRP.visibility = View.GONE
            tvDiscountPercentage.visibility = View.GONE
        } else {
            tvMRP.visibility = View.VISIBLE
            tvDiscountPercentage.visibility = View.VISIBLE
        }

        tvDiscountedPrice.text =
            getAmountInIndianCurrency(singleProductDetailsModel.productDiscountedPrice!!)
        tvProductDescription.text = singleProductDetailsModel.productDescription
        tvMRP.text = getAmountInIndianCurrency(singleProductDetailsModel.productMRP!!)
        tvDiscountPercentage.text = getDiscountPercentage(
            singleProductDetailsModel.productMRP,
            singleProductDetailsModel.productDiscountedPrice
        )
    }

    private fun initViewModel() {
        mSingleProductDetailsViewModel?.init(mProductID)
    }


}
