package com.example.milk_store_app.services;

import com.example.milk_store_app.constants.Constants;
import com.example.milk_store_app.models.response.ProductResponse;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductServices {
    @GET(Constants.PRODUCT_URL)
    Call<List<ProductResponse>> getProducts(
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize,
            @Query("searchString") String searchString,
            @Query("searchBy") String searchBy
    );

    @GET(Constants.PRODUCT_URL + "/{id}")
    Call<ProductResponse> getProductById(@Path("id") UUID id);
}
