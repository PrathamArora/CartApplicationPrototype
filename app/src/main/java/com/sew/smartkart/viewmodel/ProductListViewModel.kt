package com.sew.smartkart.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sew.smartkart.activity.BaseActivity
import com.sew.smartkart.service.model.ProductDetailsModel
import com.sew.smartkart.service.repository.AllProductsRepository
import com.sew.smartkart.view.callback.IAllProductsRepository

class ProductListViewModel : ViewModel(), IAllProductsRepository {

    private var mAllProductsRepository: AllProductsRepository? = null
    private val mIsUpdating = MutableLiveData<Pair<String, Boolean>>()

    companion object {
        private val mAllProductsMutableLiveData = MutableLiveData<ArrayList<ProductDetailsModel>>()
        private val mProductsList = ArrayList<ProductDetailsModel>()
    }

    init {
        mAllProductsMutableLiveData.value = mProductsList
        mAllProductsRepository = AllProductsRepository.getInstance()
        mIsUpdating.value = Pair("Fetching All Products", BaseActivity.SHOW_LOADER)
    }

    fun init(context: Context) {
        mAllProductsRepository!!.getProductsList(context, this)
    }

    fun getProductsDataSet(): LiveData<ArrayList<ProductDetailsModel>> {
        return mAllProductsMutableLiveData
    }

    fun isUpdating(): LiveData<Pair<String, Boolean>> {
        return mIsUpdating
    }

    override fun updateProductsDataSet(productsList: ArrayList<ProductDetailsModel>) {
        mIsUpdating.postValue(Pair("", BaseActivity.HIDE_LOADER))
        mProductsList.clear()
        mProductsList.addAll(productsList)
        mAllProductsMutableLiveData.postValue(mProductsList)
    }

    fun updateFavourite(context: Context, productDetailsModel: ProductDetailsModel) {
        mAllProductsRepository?.updateFavourite(context, productDetailsModel, this)
    }

    fun getFavoriteDetail(context: Context, productDetailsModel: ProductDetailsModel): Boolean {
        return context.getSharedPreferences(BaseActivity.LIKED_PRODUCTS_KEY, Context.MODE_PRIVATE)
            .contains(productDetailsModel.productID)
    }


}