package com.sew.smartkart.activity

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    companion object {
        const val PRODUCT_REPO_BASE_URL = "https://demo1431226.mockable.io"
        const val SHOW_LOADER = true
        const val HIDE_LOADER = false
        const val LIKED_PRODUCTS_KEY = "likedProductsKey"
        const val CART_PRODUCTS_KEY = "cartProductsKey"
        const val CHECKOUT_PRODUCTS_KEY = "checkOutProductsKey"
        const val CHECKOUT_PREF_KEY = "allCartItems"

        const val PRODUCT_ID_INTENT_KEY = "productID"
        const val DEFAULT_PRODUCT_ID = "abc"

        const val TOTAL_AMOUNT_PREFIX = "Total Amount: "

        fun getDiscountPercentage(MRP: String?, DiscountedPrice: String?): String {

            if (MRP.isNullOrEmpty() || DiscountedPrice.isNullOrEmpty()) {
                return "0"
            }

            val mrpFloat: Float = MRP.toFloat()
            val discountedPriceFloat: Float = DiscountedPrice.toFloat()

            val discountPercentage: Int =
                (((mrpFloat - discountedPriceFloat) * 100) / mrpFloat).toInt()
            return "$discountPercentage%"
        }

        fun getAmountInIndianCurrency(amount: String): String {
            return "₹ $amount"
        }
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

}