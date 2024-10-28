package com.example.milk_store_app.services;

import com.example.milk_store_app.constants.Constants;
import com.example.milk_store_app.models.response.BrandResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BrandServices {
    @GET(Constants.BRAND_URL)
    Call<List<BrandResponse>> getBrands(
            @Query("searchString") String searchString
    );
}
