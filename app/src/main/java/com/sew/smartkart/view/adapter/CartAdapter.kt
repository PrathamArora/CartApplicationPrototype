package com.sew.smartkart.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sew.customcounterlibrary.CustomCounter
import com.sew.smartkart.R
import com.sew.smartkart.activity.BaseActivity
import com.sew.smartkart.service.model.CartDetailsModel
import com.sew.smartkart.viewmodel.CartViewModel

class CartAdapter(
    private val mCartList: ArrayList<CartDetailsModel>,
    private val context: Context,
    private val cartViewModel: CartViewModel
) : RecyclerView.Adapter<CartAdapter.CartItemDetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemDetailsViewHolder {
        return CartItemDetailsViewHolder(
            LayoutInflater.from(context).inflate(R.layout.card_cart_item_details, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mCartList.size
    }

    override fun onBindViewHolder(holder: CartItemDetailsViewHolder, position: Int) {
        holder.initCartItemDetails(context, mCartList[position], cartViewModel)
    }

    class CartItemDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var imgProduct: ImageView? = null
        private var tvProductTitle: TextView? = null
        private var tvDiscountedPrice: TextView? = null
        private var counterValue: CustomCounter? = null
        private var cardProductDetails: CardView? = null

        init {
            imgProduct = itemView.findViewById(R.id.imgProduct)
            tvProductTitle = itemView.findViewById(R.id.tvProductTitle)
            tvDiscountedPrice = itemView.findViewById(R.id.tvDiscountedPrice)
            counterValue = itemView.findViewById(R.id.counterValue)
            cardProductDetails = itemView.findViewById(R.id.cardProductDetails)
        }

        fun initCartItemDetails(
            context: Context,
            cartDetailsModel: CartDetailsModel,
            cartViewModel: CartViewModel
        ) {
            val defaultOptions: RequestOptions = RequestOptions()
                .error(R.drawable.oneplussample)

            Glide.with(context).setDefaultRequestOptions(defaultOptions)
                .load(cartDetailsModel.productURL)
                .into(imgProduct!!)

            tvProductTitle?.text = cartDetailsModel.productName

            tvDiscountedPrice?.text =
                BaseActivity.getAmountInIndianCurrency(cartDetailsModel.productDiscountedPrice!!)

            manageCounter(context, cartDetailsModel, cartViewModel)
        }

        private fun manageCounter(
            context: Context,
            cartDetailsModel: CartDetailsModel,
            cartViewModel: CartViewModel
        ) {
            counterValue?.setMaxValue(cartDetailsModel.productMaxQuantity!!)
            counterValue?.setMinValue(0)
            counterValue?.setInitialValue(cartDetailsModel.productCurrentQuantity!!)
            counterValue?.setNumberTextSize(20.0f)

            counterValue?.setOnValueChangeListener(
                object : CustomCounter.OnValueChangeListener {
                    override fun onValueChange(oldValue: Int, newValue: Int) {
                        if (oldValue == cartDetailsModel.productMaxQuantity && newValue == oldValue) {
                            Toast.makeText(
                                context,
                                "You have reached the max quantity.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        cartViewModel.updateItem(context, cartDetailsModel, newValue)
                    }
                }
            )
        }

    }

}