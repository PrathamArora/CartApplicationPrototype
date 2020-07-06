package com.sew.smartkart.view.callback

import com.sew.smartkart.service.model.SingleProductDetailsModel

interface ISingleProductRepository {
    fun updateProductDetails(singleProductDetailsModel: SingleProductDetailsModel?)
}