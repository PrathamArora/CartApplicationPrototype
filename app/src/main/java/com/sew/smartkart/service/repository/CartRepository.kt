package com.sew.smartkart.service.repository

import android.content.Context
import com.google.gson.Gson
import com.sew.smartkart.activity.BaseActivity
import com.sew.smartkart.service.endpoint.IAllProductDetailsEndPoint
import com.sew.smartkart.service.model.CartDetailsModel
import com.sew.smartkart.service.model.ProductDetailsModel
import com.sew.smartkart.view.callback.ICartDetailsRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CartRepository {

    private var mCartList: ArrayList<CartDetailsModel>? = null

    fun init(context: Context, iCartDetailsRepository: ICartDetailsRepository) {
        if (mCartList != null) {
            iCartDetailsRepository.updateCartDetails(mCartList!!)
            return
        }

        getCartDetails(context, iCartDetailsRepository)
    }

    private fun getCartDetails(context: Context, iCartDetailsRepository: ICartDetailsRepository) {
        if (mCartList == null) {
            mCartList = ArrayList()
        }

        if (mCartList!!.size == 0) {
            setCartList(context, iCartDetailsRepository)
            return
        }

        iCartDetailsRepository.updateCartDetails(mCartList!!)
    }

    private fun setCartList(context: Context, iCartDetailsRepository: ICartDetailsRepository) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseActivity.PRODUCT_REPO_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val iAllProductDetailsEndPoint = retrofit.create(IAllProductDetailsEndPoint::class.java)

        val callProductDetailsModel = iAllProductDetailsEndPoint.getProductDetails()

        callProductDetailsModel.enqueue(
            object : Callback<List<ProductDetailsModel>> {
                override fun onFailure(call: Call<List<ProductDetailsModel>>, t: Throwable) {
                    iCartDetailsRepository.updateCartDetails(mCartList!!)
                }

                override fun onResponse(
                    call: Call<List<ProductDetailsModel>>,
                    response: Response<List<ProductDetailsModel>>
                ) {
                    response.body()?.let {
                        prepareCartItems(it)
                    }
                    iCartDetailsRepository.updateCartDetails(mCartList!!)
                }

                private fun prepareCartItems(productDetailsList: List<ProductDetailsModel>) {
                    mCartList!!.clear()
                    val sharedPreferences = context.getSharedPreferences(
                        BaseActivity.CART_PRODUCTS_KEY,
                        Context.MODE_PRIVATE
                    )

                    for (singleProductDetail in productDetailsList) {

                        if (sharedPreferences.contains(singleProductDetail.productID)) {
                            mCartList!!.add(
                                CartDetailsModel(
                                    singleProductDetail.productID,
                                    singleProductDetail.productName,
                                    singleProductDetail.productURL,
                                    singleProductDetail.productDiscountedPrice,
                                    singleProductDetail.productMaxQuantity,
                                    sharedPreferences.getInt(singleProductDetail.productID, 0)
                                )
                            )
                        }
                    }
                }
            }
        )
    }

    fun updateItem(
        context: Context,
        cartDetailsModel: CartDetailsModel,
        newValue: Int,
        iCartDetailsRepository: ICartDetailsRepository
    ) {
        val index = mCartList?.indexOf(cartDetailsModel)
        if (index == -1) {
            iCartDetailsRepository.updateCartDetails(mCartList!!)
            return
        }

        val sharedPreferences =
            context.getSharedPreferences(BaseActivity.CART_PRODUCTS_KEY, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        if (newValue == 0) {
            if (sharedPreferences.contains(cartDetailsModel.productID)) {
                editor.remove(cartDetailsModel.productID)
            }
            mCartList?.removeAt(index!!)
        } else {
            editor.putInt(cartDetailsModel.productID, newValue)
            cartDetailsModel.productCurrentQuantity = newValue
        }

        editor.apply()
        iCartDetailsRepository.updateCartDetails(mCartList!!)
    }

    fun checkOut(context: Context) {
        val sharedPreferences =
            context.getSharedPreferences(BaseActivity.CHECKOUT_PRODUCTS_KEY, Context.MODE_PRIVATE)

        val gson = Gson()

        val cartItems: String = gson.toJson(mCartList)

        val editor = sharedPreferences.edit()
        editor.putString(BaseActivity.CHECKOUT_PREF_KEY, cartItems)
        editor.apply()
    }
}
