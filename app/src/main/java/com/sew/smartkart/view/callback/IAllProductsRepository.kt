package com.sew.smartkart.view.callback

import com.sew.smartkart.service.model.ProductDetailsModel

interface IAllProductsRepository {

    fun updateProductsDataSet(productsList : ArrayList<ProductDetailsModel>)

}