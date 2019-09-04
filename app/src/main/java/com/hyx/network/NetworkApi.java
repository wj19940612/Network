package com.hyx.network;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * created by ${nishuideyu}
 * 2019/9/4
 * 提交数据方法和参数
 * 示例
 *
 * @FormUrlEncoded
 * @POST("check/member/qrCode") Flowable<BaseBean < NullInfo>> checkQRCodeForMember(@FieldMap Map<String, String> queryMap);
 * <p>
 * <p>
 * 提交json格式
 * @Headers("Content-Type: application/json")
 * @POST("branch/activation") Flowable<BaseBean < NullInfo>> linkSubbranch(@Header("mark") String mark, @Header("tk") String tk, @Header("uid") String uid, @Body RequestBody requestBody);
 */
public interface NetworkApi {

    ////    @FormUrlEncoded
    @GET("dicAction/getKhCities")
//    Flowable<Object> getData(@FieldMap Map<String,String> map);
    Flowable<Object> getData();
}
