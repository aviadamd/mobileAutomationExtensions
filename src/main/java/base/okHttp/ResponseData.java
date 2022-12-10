package base.okHttp;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import java.util.*;
@Slf4j
public class ResponseData {
    private int code = 0;
    private String responseBody = "";
    private Map<String, List<String>> headersMap = new HashMap<>();

    public ResponseData(Response response) {
        try {
            this.code = response.code();
            this.headersMap = response.headers().toMultimap();
            this.responseBody = Objects.requireNonNull(response.body()).string();
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }

    public int getCode() { return code; }
    public Map<String, List<String>> getHeadersMap() { return headersMap; }
    public String getResponseBody() { return responseBody; }

}
