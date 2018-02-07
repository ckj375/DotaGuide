package com.ckj.dotaguide.server;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by chenkaijian on 17-9-1.
 */

public interface HttpClientService {

    @GET("/hero/")
    Observable<ResponseBody> getHeros();

    @GET("/hero/{heroid}/")
    Observable<ResponseBody> getHero(@Path("heroid") String heroid);

    @GET
    Observable<ResponseBody> getItem(@Url String url, @Query("itemid") String itemid);

    @GET
    Observable<ResponseBody> getItem(@Url String url);

}
