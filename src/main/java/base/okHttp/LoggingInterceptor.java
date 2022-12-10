package base.okHttp;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

public class LoggingInterceptor implements Interceptor {
    private final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        //logger.info("Sending request url " + request.url());
        //logger.info("Sending request headers " + request.headers());

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
//        logger.info("Received response for " + response.request().url() + " in " + (t2 - t1) / 1e6d);
//        logger.info("Received response code " + response.code());
//        logger.info("Received response headers " + response.headers());
//        logger.info("Received response body " + Objects.requireNonNull(response.body()).string());

        return response;
    }
}
