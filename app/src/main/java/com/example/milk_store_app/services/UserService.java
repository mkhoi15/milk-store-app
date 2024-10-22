package com.example.milk_store_app.services;

import com.example.milk_store_app.constants.Constants;
import com.example.milk_store_app.models.response.UserReponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserService {
    @GET(Constants.USER_URL + "/{id}")
    Call<UserReponse> getUserById(
            @Path("id") String userId
    );
}


///public interface OrderServices {
//    @POST(Constants.ORDER_URL)
//    Call<PostOrderRequest> createOrder(@Body PostOrderRequest request);
//
//    @GET(Constants.ORDER_URL + "/user/{id}")
//    Call<List<OrderResponse>> getOrdersByUserId(
//            @Path("id") String userId,
//            @Query("pageIndex") int pageIndex,
//            @Query("pageSize") int pageSize,
//            @Query("searchString") String searchString,
//            @Query("searchBy") String searchBy
//    );
//}