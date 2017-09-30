package com.ns.yc.lifehelper.ui.other.weather.model;

import android.content.Context;

import com.ns.yc.lifehelper.api.ConstantEnvicloudApi;
import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.ui.other.weather.api.WeatherApi;
import com.ns.yc.lifehelper.ui.other.weather.bean.WeatherAirLive;
import com.ns.yc.lifehelper.ui.other.weather.bean.WeatherForecast;
import com.ns.yc.lifehelper.ui.other.weather.bean.WeatherLive;

import rx.Observable;


/**
 * Created by PC on 2017/8/21.
 * 作者：PC
 */

public class WeatherModel {

    private static WeatherModel bookModel;
    private WeatherApi mApiService;

    public WeatherModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(ConstantEnvicloudApi.EnviCloudApi)
                .create(WeatherApi.class);
    }

    public static WeatherModel getInstance(Context context){
        if(bookModel == null) {
            bookModel = new WeatherModel(context);
        }
        return bookModel;
    }

    public Observable<WeatherLive> getWeatherLive(String id) {
        Observable<WeatherLive> weatherLive = mApiService.getWeatherLive(id);
        return weatherLive;
    }
    public Observable<WeatherForecast> getWeatherForecast(String id) {
        Observable<WeatherForecast> weatherForecast = mApiService.getWeatherForecast(id);
        return weatherForecast;
    }
    public Observable<WeatherAirLive> getWeatherAir(String id) {
        Observable<WeatherAirLive> weatherAir = mApiService.getWeatherAir(id);
        return weatherAir;
    }
}
