package base.restAssured;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestAssuredBuilder {
    public static class GetBuilder {
        private String baseUri;
        private ContentType setContentType;
        private String setPath;
        private Map<String,String> setQueryParams = new HashMap<>();
        private Map<String,String> setHeaders = new HashMap<>();

        public GetBuilder setPath(String setPath) {
            this.setPath = setPath;
            return this;
        }

        public GetBuilder setBaseUri(String baseUri) {
            this.baseUri = baseUri;
            return this;
        }

        public GetBuilder setContentType(ContentType setContentType) {
            this.setContentType = setContentType;
            return this;
        }

        public GetBuilder setHeaders(Map<String,String> setHeaders) {
            this.setHeaders = setHeaders;
            return this;
        }

        public GetBuilder setQueryParams(Map<String,String> setQueryParams) {
            this.setQueryParams = setQueryParams;
            return this;
        }

        public Response build() {
            RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
            requestSpecBuilder.setBaseUri(this.baseUri);

            if (!this.setPath.isEmpty()) {
                requestSpecBuilder.setBasePath(this.setPath);
            }

            if (this.setContentType != null && !this.setContentType.toString().isEmpty()) {
                requestSpecBuilder.setContentType(this.setContentType);
            }

            if (!this.setQueryParams.isEmpty()) {
                requestSpecBuilder.addQueryParams(this.setQueryParams);
            }

            if (!this.setHeaders.isEmpty()) {
                requestSpecBuilder.addHeaders(this.setHeaders);
            }

            return given().spec(requestSpecBuilder.build()).get();
        }
    }
    public static class PostBuilder {
        private String baseUri;
        private String setPath;
        private HashMap<String,String> setBody = new HashMap<>();
        private ContentType setContentType;
        private Map<String,String> setQueryParams = new HashMap<>();
        private Map<String,String> setHeaders = new HashMap<>();
        public PostBuilder setBaseUri(String baseUri) {
            this.baseUri = baseUri;
            return this;
        }
        public PostBuilder setPath(String setPath) {
            this.setPath = setPath;
            return this;
        }
        public PostBuilder setContentType(ContentType setContentType) {
            this.setContentType = setContentType;
            return this;
        }

        public PostBuilder setHeaders(Map<String,String> setHeaders) {
            this.setHeaders = setHeaders;
            return this;
        }

        public PostBuilder setBody(HashMap<String,String> setBody) {
            this.setBody = setBody;
            return this;
        }

        public PostBuilder setQueryParams(Map<String,String> setQueryParams) {
            this.setQueryParams = setQueryParams;
            return this;
        }

        public Response build() {
            RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
            requestSpecBuilder.setBaseUri(this.baseUri);

            if (!this.setPath.isEmpty()) {
                requestSpecBuilder.setBasePath(this.setPath);
            }

            if (this.setContentType != null && !this.setContentType.toString().isEmpty()) {
                requestSpecBuilder.setContentType(this.setContentType);
            }

            if (!this.setQueryParams.isEmpty()) {
                requestSpecBuilder.addQueryParams(this.setQueryParams);
            }

            if (!this.setHeaders.isEmpty()) {
                requestSpecBuilder.addHeaders(this.setHeaders);
            }

            return given().spec(requestSpecBuilder.setBody(this.setBody).build()).post();
        }
    }

    public static class PutBuilder {
        private String baseUri;
        private String setPath;
        private HashMap<String,String> setBody = new HashMap<>();
        private ContentType setContentType;
        private Map<String,String> setQueryParams = new HashMap<>();
        private Map<String,String> setHeaders = new HashMap<>();

        public PutBuilder setBaseUri(String baseUri) {
            this.baseUri = baseUri;
            return this;
        }

        public PutBuilder setContentType(ContentType setContentType) {
            this.setContentType = setContentType;
            return this;
        }

        public PutBuilder setPath(String setPath) {
            this.setPath = setPath;
            return this;
        }

        public PutBuilder setHeaders(Map<String,String> setHeaders) {
            this.setHeaders = setHeaders;
            return this;
        }

        public PutBuilder setQueryParams(Map<String,String> setQueryParams) {
            this.setQueryParams = setQueryParams;
            return this;
        }

        public PutBuilder setBody(HashMap<String,String> setBody) {
            this.setBody = setBody;
            return this;
        }

        public Response build() {
            RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
            requestSpecBuilder.setBaseUri(this.baseUri);

            if (!this.setPath.isEmpty()) {
                requestSpecBuilder.setBasePath(this.setPath);
            }

            if (this.setContentType != null && !this.setContentType.toString().isEmpty()) {
                requestSpecBuilder.setContentType(this.setContentType);
            }

            if (!this.setQueryParams.isEmpty()) {
                requestSpecBuilder.addQueryParams(this.setQueryParams);
            }

            if (!this.setHeaders.isEmpty()) {
                requestSpecBuilder.addHeaders(this.setHeaders);
            }

            return given().spec(requestSpecBuilder.setBody(this.setBody).build()).put();
        }
    }
}
