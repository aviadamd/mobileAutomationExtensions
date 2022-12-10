package base.okHttp;

import com.google.gson.Gson;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import okhttp3.*;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public class OkHttpBuilderExtensions {

    private Request.Builder requestBuilder = new Request.Builder();

    public OkHttpBuilderExtensions setRequestBuilder(Request.Builder requestBuilder) {
        this.requestBuilder = requestBuilder;
        return this;
    }

    public FormBody setBodyMap(boolean isAddEncoded, Map<String, String> bodyMap) {
        FormBody.Builder builder = new FormBody.Builder();
        if (isAddEncoded) {
            bodyMap.forEach(builder::addEncoded);
        } else bodyMap.forEach(builder::add);
        return builder.build();
    }

    public ResponseCollector build() {
        Optional<Response> response = Optional.empty();

        try {

            response = Optional.ofNullable(Observable.just(
                    this.okHttpClient().newCall(this.requestBuilder.build()).execute())
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.newThread())
                    .blockingFirst());

            if (response.isPresent()) {
                if (response.get().isSuccessful()) {
                    return new ResponseCollector(true, response.get(), "");
                } else return new ResponseCollector(false, response.get(), "");
            }
        } catch (Exception exception) {
            return new ResponseCollector(false, response.orElse(null), exception.getMessage());
        }

        return new ResponseCollector(false, null, "");
    }
    private OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new LoggingInterceptor())
                .build();
    }

    static {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };
    }
}
