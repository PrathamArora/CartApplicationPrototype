package com.sew.smartkart.service.repository

import android.content.Context
import com.sew.smartkart.activity.BaseActivity
import com.sew.smartkart.service.endpoint.IAllProductDetailsEndPoint
import com.sew.smartkart.service.model.ProductDetailsModel
import com.sew.smartkart.view.callback.IAllProductsRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AllProductsRepository {

    companion object {

        @Volatile
        private var allProductsRepository: AllProductsRepository? = null
        private var productsList: ArrayList<ProductDetailsModel>? = null

        fun getInstance(): AllProductsRepository {
            if (allProductsRepository == null) {
                synchronized(this) {
                    if (allProductsRepository == null) {
                        allProductsRepository = AllProductsRepository()
                        productsList = ArrayList()
                    }
                }
            }
            return allProductsRepository!!
        }
    }

    fun getProductsList(context: Context, iAllProductsRepository: IAllProductsRepository) {
        if (productsList.isNullOrEmpty()) {
            productsList = ArrayList()
        }

        if (productsList!!.size == 0) {
            setProductsList(context, iAllProductsRepository)
            return
        }

        iAllProductsRepository.updateProductsDataSet(productsList!!)
    }

    private fun setProductsList(context: Context, iAllProductsRepository: IAllProductsRepository) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseActivity.PRODUCT_REPO_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val iAllProductDetailsEndPoint = retrofit.create(IAllProductDetailsEndPoint::class.java)

        val callProductDetailsModel = iAllProductDetailsEndPoint.getProductDetails()

        callProductDetailsModel.enqueue(
            object : Callback<List<ProductDetailsModel>> {
                override fun onFailure(call: Call<List<ProductDetailsModel>>, t: Throwable) {
                    iAllProductsRepository.updateProductsDataSet(productsList!!)
                }

                override fun onResponse(
                    call: Call<List<ProductDetailsModel>>,
                    response: Response<List<ProductDetailsModel>>
                ) {
                    response.body()?.let {
                        prepareDataSet(it)
                    }
                    iAllProductsRepository.updateProductsDataSet(productsList!!)
                }

                private fun prepareDataSet(allProductsList: List<ProductDetailsModel>) {
                    productsList!!.clear()
                    val sharedPreferences = context.getSharedPreferences(
                        BaseActivity.LIKED_PRODUCTS_KEY,
                        Context.MODE_PRIVATE
                    )

                    for (singleProductDetailsModel in allProductsList) {
                        if (sharedPreferences.contains(singleProductDetailsModel.productID)) {
                            singleProductDetailsModel.isFavorite = true
                        }
                    }

                    productsList!!.addAll(allProductsList)
                }

            }
        )

    }

    fun updateFavourite(
        context: Context,
        productDetailsModel: ProductDetailsModel,
        iAllProductsRepository: IAllProductsRepository
    ) {

        val index = productsList?.indexOf(productDetailsModel)

        if (index == -1) {
            iAllProductsRepository.updateProductsDataSet(productsList!!)
            return
        }

        val sharedPreferences = context.getSharedPreferences(
            BaseActivity.LIKED_PRODUCTS_KEY,
            Context.MODE_PRIVATE
        )

        val editor = sharedPreferences.edit()

        productDetailsModel.isFavorite = !productDetailsModel.isFavorite

        if (sharedPreferences.contains(productDetailsModel.productID)) {
            editor.remove(productDetailsModel.productID)
        } else {
            editor.putBoolean(productDetailsModel.productID, true)
        }
        editor.apply()

        iAllProductsRepository.updateProductsDataSet(productsList!!)
    }


}