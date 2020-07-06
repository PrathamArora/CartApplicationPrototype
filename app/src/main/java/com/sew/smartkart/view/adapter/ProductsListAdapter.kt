package com.sew.smartkart.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sew.smartkart.R
import com.sew.smartkart.activity.BaseActivity
import com.sew.smartkart.activity.SingleProductDetailsActivity
import com.sew.smartkart.service.model.ProductDetailsModel
import com.sew.smartkart.viewmodel.ProductListViewModel

class ProductsListAdapter(
    private val mProductsList: ArrayList<ProductDetailsModel>,
    private val context: Context,
    private val mProductListViewModel: ProductListViewModel
) : RecyclerView.Adapter<ProductsListAdapter.ProductDetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductDetailsViewHolder {
        return ProductDetailsViewHolder(
            LayoutInflater.from(context).inflate(R.layout.card_product_details, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mProductsList.size
    }

    override fun onBindViewHolder(holder: ProductDetailsViewHolder, position: Int) {
        holder.initProductDetails(context, mProductsList[position], mProductListViewModel)
    }


    class ProductDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var imgProduct: ImageView? = null
        private var tvProductTitle: TextView? = null
        private var tvDiscountedPrice: TextView? = null
        private var tvMRP: TextView? = null
        private var tvDiscountPercentage: TextView? = null
        private var btnLikeButton: ImageButton? = null
        private var cardProductDetails: CardView? = null

        init {
            imgProduct = itemView.findViewById(R.id.imgProduct)
            tvProductTitle = itemView.findViewById(R.id.tvProductTitle)
            tvDiscountedPrice = itemView.findViewById(R.id.tvDiscountedPrice)
            tvMRP = itemView.findViewById(R.id.tvMRP)
            tvDiscountPercentage = itemView.findViewById(R.id.tvDiscountPercentage)
            btnLikeButton = itemView.findViewById(R.id.btnLikeButton)
            cardProductDetails = itemView.findViewById(R.id.cardProductDetails)
        }

        fun initProductDetails(
            context: Context,
            productDetailsModel: ProductDetailsModel,
            mProductListViewModel: ProductListViewModel
        ) {
            val defaultOptions: RequestOptions = RequestOptions()
                .error(R.drawable.oneplussample)

            Glide.with(context).setDefaultRequestOptions(defaultOptions)
                .load(productDetailsModel.productURL)
                .into(imgProduct!!)

            tvProductTitle?.text = productDetailsModel.productName

            manageProductPricing(productDetailsModel)

            initLikeButton(mProductListViewModel, context, productDetailsModel)

            btnLikeButton?.setOnClickListener {
                manageLikeButton(context, productDetailsModel, mProductListViewModel)
            }

            cardProductDetails?.setOnClickListener {
                val intent = Intent(context, SingleProductDetailsActivity::class.java)
                intent.putExtra(BaseActivity.PRODUCT_ID_INTENT_KEY, productDetailsModel.productID)
                context.startActivity(intent)
            }
        }

        private fun manageLikeButton(
            context: Context,
            productDetailsModel: ProductDetailsModel,
            mProductListViewModel: ProductListViewModel
        ) {
            mProductListViewModel.updateFavourite(context, productDetailsModel)
        }

        private fun initLikeButton(
            mProductListViewModel: ProductListViewModel,
            context: Context,
            productDetailsModel: ProductDetailsModel
        ) {
            btnLikeButton?.isSelected =
                mProductListViewModel.getFavoriteDetail(context, productDetailsModel)
        }


        private fun manageProductPricing(productDetailsModel: ProductDetailsModel) {
            if (productDetailsModel.productMRP == productDetailsModel.productDiscountedPrice) {
                tvMRP?.visibility = View.GONE
                tvDiscountPercentage?.visibility = View.GONE
            } else {
                tvMRP?.visibility = View.VISIBLE
                tvDiscountPercentage?.visibility = View.VISIBLE
            }

            tvDiscountedPrice?.text =
                BaseActivity.getAmountInIndianCurrency(productDetailsModel.productDiscountedPrice!!)
            tvMRP?.text = BaseActivity.getAmountInIndianCurrency(productDetailsModel.productMRP!!)

            tvDiscountPercentage?.text = BaseActivity.getDiscountPercentage(
                productDetailsModel.productMRP,
                productDetailsModel.productDiscountedPrice
            )
        }


    }


}