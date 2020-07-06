package com.sew.smartkart.service.repository

import com.sew.smartkart.activity.BaseActivity
import com.sew.smartkart.service.endpoint.ISingleProductDetailsEndPoint
import com.sew.smartkart.service.model.SingleProductDetailsModel
import com.sew.smartkart.view.callback.ISingleProductRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SingleProductRepository {

    private var productID : String? = null
    private var singleProductDetailsModel: SingleProductDetailsModel? = null

    fun init(iSingleProductRepository: ISingleProductRepository, id : String){
        productID = id
        if(singleProductDetailsModel != null){
            iSingleProductRepository.updateProductDetails(singleProductDetailsModel!!)
            return
        }

        getProductDetails(iSingleProductRepository)
    }

    private fun getProductDetails(iSingleProductRepository: ISingleProductRepository) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseActivity.PRODUCT_REPO_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val iSingleProductDetailsEndPoint = retrofit.create(ISingleProductDetailsEndPoint::class.java)

        val callSingleProductDetailsModel = iSingleProductDetailsEndPoint.getProductDetails(productID!!)

        callSingleProductDetailsModel.enqueue(
            object :Callback<SingleProductDetailsModel> {
                override fun onFailure(call: Call<SingleProductDetailsModel>, t: Throwable) {
                    iSingleProductRepository.updateProductDetails(null)
                }

                override fun onResponse(
                    call: Call<SingleProductDetailsModel>,
                    response: Response<SingleProductDetailsModel>
                ) {
                    response.body()?.let {
                        singleProductDetailsModel = it
                    }
                    iSingleProductRepository.updateProductDetails(singleProductDetailsModel)
                }

            }
        )

    }

}