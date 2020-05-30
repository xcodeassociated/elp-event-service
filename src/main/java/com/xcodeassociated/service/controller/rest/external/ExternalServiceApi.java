package com.xcodeassociated.service.controller.rest.external;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;


public interface ExternalServiceApi {

    @GET("v1/token/whoami")
    Call<ResponseBody> getWhoami();

}
