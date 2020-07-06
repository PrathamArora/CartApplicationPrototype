package com.sew.smartkart.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sew.smartkart.activity.BaseActivity
import com.sew.smartkart.service.model.CartDetailsModel
import com.sew.smartkart.service.repository.CartRepository
import com.sew.smartkart.view.callback.ICartDetailsRepository

class CartViewModel : ViewModel(), ICartDetailsRepository {
    private val mIsUpdating = MutableLiveData<Pair<String, Boolean>>()
    private var mCartRepository: CartRepository? = null

    companion object {
        private val mCartDetailsMutableLiveData = MutableLiveData<ArrayList<CartDetailsModel>>()
        private val mCartList = ArrayList<CartDetailsModel>()
    }

    init {
        mCartDetailsMutableLiveData.value = mCartList
        mCartRepository = CartRepository()
        mIsUpdating.value = Pair("Preparing Cart", BaseActivity.SHOW_LOADER)
    }

    fun init(context: Context) {
        mCartRepository?.init(context, this)
    }

    fun isUpdating(): LiveData<Pair<String, Boolean>> {
        return mIsUpdating
    }

    fun getCartDetails(): LiveData<ArrayList<CartDetailsModel>> {
        return mCartDetailsMutableLiveData
    }

    override fun updateCartDetails(cartList: ArrayList<CartDetailsModel>) {
        mIsUpdating.postValue(Pair("", BaseActivity.HIDE_LOADER))
        mCartList.clear()
        mCartList.addAll(cartList)
        mCartDetailsMutableLiveData.postValue(mCartList)
    }

    fun updateItem(context: Context, cartDetailsModel: CartDetailsModel, newValue: Int) {
        mCartRepository?.updateItem(context, cartDetailsModel, newValue, this)
    }

    fun checkOut(context: Context) {
        mCartRepository?.checkOut(context)
    }

}