package io.microshow.rxretrofit.interceptor;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Builder;
import okhttp3.MultipartBody.Part;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 请求参数拦截器
 */
public abstract class RequestParamsInterceptor implements Interceptor {

    MediaType mediaType = MediaType.parse("text/plain; charset=utf-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Map<String, String> params = getCommonParams();
        if (params == null || params.size() == 0) {
            return chain.proceed(request);
        }

        // 构建新的请求
        Request.Builder newRequestBuilder = request.newBuilder();

        // 添加公共请求参数
        if (request.method().equals("GET")) {
            newRequestBuilder.url(addQueryParams(request, params));
        } else if (request.method().equals("POST") && request.body() != null) {
            if (request.body() instanceof FormBody) {
                newRequestBuilder.post(addFormBodyParams(request, params));
            } else if (request.body() instanceof MultipartBody) {
                newRequestBuilder.post(addFormDataPartParams(request, params));
            }
        }

        return chain.proceed(newRequestBuilder.build());
    }

    /**
     * 添加公共查询参数，请求方法为GET时调用
     *
     * @param request request
     * @return HttpUrl
     */
    private HttpUrl addQueryParams(final Request request, final Map<String, String> params) {
        HttpUrl.Builder builder = request.url().newBuilder();
        for (Entry<String, String> entry : params.entrySet()) {
            builder.addQueryParameter(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }

    /**
     * 添加公共查询参数，请求方法为POST，且请求体类型为FormBody时调用
     *
     * @param request request
     * @return RequestBody
     */
    private RequestBody addFormBodyParams(final Request request, final Map<String, String> params) {
        RequestBody requestBody = request.body();

        if (requestBody == null || !(requestBody instanceof FormBody)) {
            return requestBody;
        }

        FormBody originalFormBody = (FormBody) requestBody;

        FormBody.Builder newFormBodyBuilder = new FormBody.Builder();
        for (int i = 0; i < originalFormBody.size(); i++) {
            newFormBodyBuilder.add(originalFormBody.name(i), originalFormBody.value(i));
        }
        for (Entry<String, String> entry : params.entrySet()) {
            newFormBodyBuilder.add(entry.getKey(), entry.getValue());
        }

        return newFormBodyBuilder.build();
    }

    /**
     * 添加公共查询参数，请求方法为POST，且请求体类型为MultipartBody时调用
     *
     * @param request request
     * @return RequestBody
     */
    private RequestBody addFormDataPartParams(final Request request,
                                              final Map<String, String> params) {
        RequestBody requestBody = request.body();

        if (requestBody == null || !(requestBody instanceof MultipartBody)) {
            return requestBody;
        }

        MultipartBody originalBody = (MultipartBody) requestBody;
        List<Part> originalParts = originalBody.parts();

        Builder builder = new Builder();
        builder.setType(MultipartBody.FORM);

        // 添加公共参数Part（注意：应该在添加原有part之前先添加公共参数）
        for (Entry<String, String> entry : params.entrySet()) {
            builder.addFormDataPart(entry.getKey(), null,
                    RequestBody.create(MultipartBody.FORM, entry.getValue()));
        }

        // 添加原有的Part
        if (originalParts != null && originalParts.size() > 0) {
            for (Part part : originalParts) {
                builder.addPart(part);
            }
        }

        return builder.build();
    }

    public abstract Map<String, String> getCommonParams();

}
