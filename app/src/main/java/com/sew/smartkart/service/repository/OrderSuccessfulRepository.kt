package com.sew.smartkart.service.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sew.smartkart.activity.BaseActivity
import com.sew.smartkart.service.model.CartDetailsModel
import com.sew.smartkart.view.callback.IOrderSuccessfulRepository

class OrderSuccessfulRepository {
    private var mCartList: ArrayList<CartDetailsModel>? = null

    fun init(context: Context, iOrderSuccessfulRepository: IOrderSuccessfulRepository) {
        if (mCartList != null) {
            iOrderSuccessfulRepository.updateSuccessOrderData(mCartList!!)
            return
        }
        getCartDetails(context, iOrderSuccessfulRepository)
    }

    private fun getCartDetails(
        context: Context,
        iOrderSuccessfulRepository: IOrderSuccessfulRepository
    ) {
        if (mCartList == null) {
            mCartList = ArrayList()
        }

        if (mCartList!!.size == 0) {
            setCartList(context, iOrderSuccessfulRepository)
            return
        }

        iOrderSuccessfulRepository.updateSuccessOrderData(mCartList!!)
    }

    private fun setCartList(
        context: Context,
        iOrderSuccessfulRepository: IOrderSuccessfulRepository
    ) {
        val sharedPreferences =
            context.getSharedPreferences(BaseActivity.CHECKOUT_PRODUCTS_KEY, Context.MODE_PRIVATE)

        val gson = Gson()

        val token: TypeToken<List<CartDetailsModel?>?> =
            object : TypeToken<List<CartDetailsModel?>?>() {}
        val tempList: ArrayList<CartDetailsModel> = gson.fromJson(
            sharedPreferences.getString(
                BaseActivity.CHECKOUT_PREF_KEY,
                ""
            ), token.type
        )

        mCartList?.clear()
        mCartList?.addAll(tempList)

        iOrderSuccessfulRepository.updateSuccessOrderData(mCartList!!)
    }

    fun completeTransaction(context: Context) {
        var sharedPreferences =
            context.getSharedPreferences(BaseActivity.CART_PRODUCTS_KEY, Context.MODE_PRIVATE)

        var editor = sharedPreferences.edit()

        for (singleCartDetailsModel in mCartList!!) {
            if (sharedPreferences.contains(singleCartDetailsModel.productID))
                editor.remove(singleCartDetailsModel.productID)
        }
        editor.apply()

        sharedPreferences =
            context.getSharedPreferences(BaseActivity.CHECKOUT_PRODUCTS_KEY, Context.MODE_PRIVATE)

        editor = sharedPreferences.edit()

        if (sharedPreferences.contains(BaseActivity.CHECKOUT_PREF_KEY))
            editor.remove(BaseActivity.CHECKOUT_PREF_KEY)

        editor.apply()
    }

}