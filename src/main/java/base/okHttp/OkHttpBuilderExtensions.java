package base.okHttp;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import java.util.*;

@Slf4j
public class OkHttpBuilderExtensions {
    private Request.Builder requestBuilder = new Request.Builder();
    public OkHttpBuilderExtensions setRequestBuilder(Request.Builder requestBuilder) {
        this.requestBuilder = requestBuilder;
        return this;
    }
    public FormBody setBodyMap(boolean isAddEncoded, Map<String,String> bodyMap) {
        FormBody.Builder builder = new FormBody.Builder();
        if (isAddEncoded) {
            bodyMap.forEach(builder::addEncoded);
        } else bodyMap.forEach(builder::add);
        return builder.build();
    }

    public ResponseCollector build() {
        Request request = null;
        Optional<Response> response = Optional.empty();

        try {

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new LoggingInterceptor())
                    .build();

            request = this.requestBuilder.build();
            response = Optional.ofNullable(Observable.just(client.newCall(request).execute())
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

        return new ResponseCollector(false,null, "");
    }

}
