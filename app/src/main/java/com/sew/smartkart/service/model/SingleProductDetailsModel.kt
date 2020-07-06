package com.sew.smartkart.service.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SingleProductDetailsModel(
    @Expose
    @SerializedName("id")
    val productID: String?,

    @Expose
    @SerializedName("name")
    val productName: String?,


    @Expose
    @SerializedName("imageURL")
    val productURL: String?,


    @Expose
    @SerializedName("MRP")
    val productMRP: String?,


    @Expose
    @SerializedName("discountedPrice")
    val productDiscountedPrice: String?,


    @Expose
    @SerializedName("maxQuantity")
    val productMaxQuantity: Int?,

    @Expose
    @SerializedName("description")
    val productDescription: String?
)