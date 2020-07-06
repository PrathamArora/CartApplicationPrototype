package com.sew.smartkart.service.endpoint

import com.sew.smartkart.service.model.ProductDetailsModel
import retrofit2.Call
import retrofit2.http.GET

interface IAllProductDetailsEndPoint {
    @GET("/getSmartPhones")
    fun getProductDetails(): Call<List<ProductDetailsModel>>
}