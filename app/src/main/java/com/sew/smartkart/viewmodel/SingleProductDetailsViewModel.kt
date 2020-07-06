package com.sew.smartkart.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sew.smartkart.activity.BaseActivity
import com.sew.smartkart.service.model.SingleProductDetailsModel
import com.sew.smartkart.service.repository.SingleProductRepository
import com.sew.smartkart.view.callback.ISingleProductRepository

class SingleProductDetailsViewModel : ViewModel(), ISingleProductRepository {

    private val mIsUpdating = MutableLiveData<Pair<String, Boolean>>()
    private var mSingleProductRepository: SingleProductRepository? = null
    private var mSingleProductDetailsModelLiveData = MutableLiveData<SingleProductDetailsModel>()

    init {
        mIsUpdating.value = Pair("Fetching Product Details", BaseActivity.SHOW_LOADER)
        mSingleProductDetailsModelLiveData.value = null
        mSingleProductRepository = SingleProductRepository()
    }

    fun init(productID: String) {
        mSingleProductRepository?.init(this, productID)
    }

    fun isUpdating(): LiveData<Pair<String, Boolean>> {
        return mIsUpdating
    }

    fun getSingleProductDetailsModel(): LiveData<SingleProductDetailsModel> {
        return mSingleProductDetailsModelLiveData
    }

    override fun updateProductDetails(singleProductDetailsModel: SingleProductDetailsModel?) {
        mIsUpdating.value = Pair("", BaseActivity.HIDE_LOADER)
        mSingleProductDetailsModelLiveData.value = singleProductDetailsModel
    }

    fun getFavoriteDetail(
        context: Context,
        singleProductDetailsModel: SingleProductDetailsModel
    ): Boolean {
        return context.getSharedPreferences(BaseActivity.LIKED_PRODUCTS_KEY, Context.MODE_PRIVATE)
            .contains(singleProductDetailsModel.productID)
    }

    fun updateFavorite(context: Context, singleProductDetailsModel: SingleProductDetailsModel) {
        val sharedPreferences =
            context.getSharedPreferences(BaseActivity.LIKED_PRODUCTS_KEY, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        if (sharedPreferences.contains(singleProductDetailsModel.productID)) {
            editor.remove(singleProductDetailsModel.productID)
        } else {
            editor.putBoolean(singleProductDetailsModel.productID, true)
        }
        editor.apply()
    }

    fun getCounterInitialValue(
        context: Context,
        singleProductDetailsModel: SingleProductDetailsModel
    ): Int {
        return context.getSharedPreferences(
            BaseActivity.CART_PRODUCTS_KEY,
            Context.MODE_PRIVATE
        ).getInt(singleProductDetailsModel.productID, 0)
    }

    fun updateItemQuantityInCart(
        context: Context,
        singleProductDetailsModel: SingleProductDetailsModel,
        currentValue: Int
    ) {
        val sharedPreferences =
            context.getSharedPreferences(BaseActivity.CART_PRODUCTS_KEY, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        if (currentValue == 0) {

            if (sharedPreferences.contains(
                    singleProductDetailsModel.productID
                )
            ) {
                editor.remove(singleProductDetailsModel.productID)
            }


        } else {
            editor.putInt(
                singleProductDetailsModel.productID,
                currentValue
            )
        }
        editor.apply()


    }
}