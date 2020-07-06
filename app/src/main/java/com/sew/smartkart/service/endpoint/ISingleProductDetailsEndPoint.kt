package com.sew.smartkart.service.endpoint

import com.sew.smartkart.service.model.SingleProductDetailsModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ISingleProductDetailsEndPoint {
    @GET("/getSmartPhones/{productID}")
    fun getProductDetails(@Path("productID") productID : String): Call<SingleProductDetailsModel>
}