package io.microshow.rxretrofit.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;

import io.microshow.rxretrofit.internal.ApiException;
import io.microshow.rxretrofit.internal.BaseResponse;
import io.microshow.rxretrofit.internal.ResponseState;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 *
 */
final class CommonGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CommonGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        JsonReader jsonReader = gson.newJsonReader(value.charStream());
        String json = value.string();
        try {
            verify(json);
//            return adapter.read(jsonReader);
            return adapter.read(gson.newJsonReader(new StringReader(json)));
        } finally {
            value.close();
        }
    }

    private void verify(String json) {
        BaseResponse result = gson.fromJson(json, BaseResponse.class);
        if (result.getCode() != ResponseState.SUCCESS) {
            switch (result.getCode()) {
                case ResponseState.SERVER_EXCEPTION:
                    throw new ApiException(result.getCode(), result.getMessage());
                case ResponseState.TOKEN_EXPIRE:
                    throw new ApiException(result.getCode(), result.getMessage());
                default:
                    throw new ApiException(result.getCode(), result.getMessage());
            }
        }
    }

}
