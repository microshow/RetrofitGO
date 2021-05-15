package io.microshow.retrofitgo.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.Type;

import io.microshow.retrofitgo.RetrofitClient;

/**
 * sp 缓存数据
 * Created by Super on 2019/12/25.
 */
public class CacheSpUtils {

    public static final String PREFS_NAME = "RETROFITGO_CACHE_DATAS";

    public static SharedPreferences getSharedPreferences() {
        return RetrofitClient.getInstance().getContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static <T> void saveCacheData(String cacheKey, T model) {
        if (!TextUtils.isEmpty(cacheKey) && model != null) {
            SharedPreferences mSharedPreferences = getSharedPreferences();
            SharedPreferences.Editor edit = mSharedPreferences.edit();
            String json = new Gson().toJson(model);
            edit.putString(cacheKey, json);
            edit.commit();
            Log.e("CacheSpUtils","saveCacheData cacheKey="+cacheKey+";json="+json);
        }
    }

    public static <T> T getCacheData(String cacheKey, Type typeOfT) {
        if (!TextUtils.isEmpty(cacheKey)) {
            String data = getSharedPreferences().getString(cacheKey, null);
            Log.e("CacheSpUtils","getCacheData cacheKey="+cacheKey+";json="+data);
            return !TextUtils.isEmpty(data) ? new Gson().fromJson(data, typeOfT) : null;
        } else {
            return null;
        }
    }

    public static <T> T getCacheData(String cacheKey, Type typeOfT, Type typeOfList) {
        if (!TextUtils.isEmpty(cacheKey)) {
            String data = getSharedPreferences().getString(cacheKey, null);
            Log.e("CacheSpUtils","getCacheData cacheKey="+cacheKey+";json="+data);
            if (data != null && !TextUtils.isEmpty(data)) {
                if (data.startsWith("[") && data.endsWith("]")) {//json数组
                    return new Gson().fromJson(data, typeOfList);
                } else {
                    return new Gson().fromJson(data, typeOfT);
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

}
