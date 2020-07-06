package com.sew.smartkart.view.callback

import com.sew.smartkart.service.model.CartDetailsModel

interface ICartDetailsRepository {

    fun updateCartDetails(cartList: ArrayList<CartDetailsModel>)

}