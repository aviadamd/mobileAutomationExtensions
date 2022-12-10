package base.okHttp;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.Map;

@Slf4j
public class OkHttpBuilderExtensionsTest {
    private OkHttpBuilderExtensions okHttpBuilderExtensions;

    @BeforeClass
    public void init() {
        this.okHttpBuilderExtensions = new OkHttpBuilderExtensions();
    }

    @Test
    public void testGetRequest() {
        HttpUrl.Builder httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("jsonplaceholder.typicode.com")
                .addPathSegment("comments")
                .addQueryParameter("postId","2");

        Request.Builder request = new Request.Builder()
                .get()
                .url(httpUrl.build())
                .header("Content-Type", "application/json");

        ResponseCollector optionalResponse = this.okHttpBuilderExtensions
                .setRequestBuilder(request)
                .build();

        if (optionalResponse.isPassRequest()) {
            log.info(optionalResponse.toString());
            log.info(optionalResponse.getResponse().headers().toString());
        }
    }

    @Test
    public void testPostRequest() {
        Headers headers = Headers.of(Map.of("Content-Type","application/json"));
        FormBody body = this.okHttpBuilderExtensions.setBodyMap(false, Map.of("title","foo","body","1","userId","1"));

        Request.Builder request = new Request.Builder()
                .url(new HttpUrl.Builder()
                        .scheme("http")
                        .host("jsonplaceholder.typicode.com")
                        .addPathSegment("posts")
                        .build())
                .headers(headers)
                .post(body);

        ResponseCollector response = this.okHttpBuilderExtensions.setRequestBuilder(request).build();
        if (response.isPassRequest()) {

            log.info("request " + response.getResponse().request().url());
            log.info("code " + response.getResponse().code());

            response.getResponse().headers().toMultimap().forEach((key, values) -> {
                log.info("headers key " + key);
                log.info("headers values " + values.toString());
            });

            log.info(" " + response.getResponseData().getResponseBody());
        }
    }
}
