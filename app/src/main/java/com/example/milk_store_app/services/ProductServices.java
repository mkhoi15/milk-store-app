package com.example.milk_store_app.services;

import com.example.milk_store_app.constants.Constants;
import com.example.milk_store_app.models.request.ProductCreateRequest;
import com.example.milk_store_app.models.request.ProductUpdateRequest;
import com.example.milk_store_app.models.response.ProductResponse;

import java.util.List;
import java.util.UUID;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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

    @POST(Constants.PRODUCT_URL)
    Call<ProductResponse> createProduct(@Body ProductCreateRequest product);

    @Multipart
    @POST(Constants.IMAGE_URL)
    Call<String> uploadImage(@Part MultipartBody.Part image);

    @PUT(Constants.PRODUCT_URL + "/{id}")
    Call<ProductResponse> updateProduct(@Path("id") UUID id, @Body ProductUpdateRequest product);
}