package com.sew.smartkart.view.callback

import com.sew.smartkart.service.model.CartDetailsModel

interface IOrderSuccessfulRepository {
    fun updateSuccessOrderData(cartList: ArrayList<CartDetailsModel>)
}