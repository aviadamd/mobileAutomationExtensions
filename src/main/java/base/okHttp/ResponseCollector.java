package base.okHttp;

import okhttp3.Response;

import java.util.Objects;

public class ResponseCollector {
    public boolean passRequest;

    public String exception;
    public Response response;
    public String bodyToString;
    public ResponseData responseData;

    public ResponseCollector(boolean passRequest, Response response, String exception) {
        this.passRequest = passRequest;
        this.response = response;
        this.exception = exception;
        this.responseData = new ResponseData(response);
    }

    public boolean isPassRequest() { return passRequest; }
    public Response getResponse() { return response; }

    public String getException() { return exception; }

    public ResponseData getResponseData() { return responseData; }

}
