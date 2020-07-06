package com.sew.smartkart.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sew.smartkart.activity.BaseActivity
import com.sew.smartkart.service.model.CartDetailsModel
import com.sew.smartkart.service.repository.OrderSuccessfulRepository
import com.sew.smartkart.view.callback.IOrderSuccessfulRepository

class OrderSuccessfulViewModel : ViewModel(), IOrderSuccessfulRepository {

    private val mIsUpdating = MutableLiveData<Pair<String, Boolean>>()
    private var mOrderSuccessfulRepository: OrderSuccessfulRepository? = null

    companion object {
        private val mCartDetailsMutableLiveData = MutableLiveData<ArrayList<CartDetailsModel>>()
        private val mCartList = ArrayList<CartDetailsModel>()
    }

    init {
        mIsUpdating.value = Pair("Preparing Receipt", BaseActivity.SHOW_LOADER)
        mOrderSuccessfulRepository = OrderSuccessfulRepository()
        mCartDetailsMutableLiveData.value = mCartList
    }

    fun init(context: Context){
        mOrderSuccessfulRepository?.init(context, this)
    }


    fun getCartDetails(): LiveData<ArrayList<CartDetailsModel>> {
        return mCartDetailsMutableLiveData
    }

    fun isUpdating(): LiveData<Pair<String, Boolean>> {
        return mIsUpdating
    }

    fun completeTransaction(context: Context) {
        mOrderSuccessfulRepository?.completeTransaction(context)
    }

    override fun updateSuccessOrderData(cartList: ArrayList<CartDetailsModel>) {
        mIsUpdating.postValue(Pair("", BaseActivity.HIDE_LOADER))
        mCartList.clear()
        mCartList.addAll(cartList)
        mCartDetailsMutableLiveData.postValue(mCartList)
    }


}