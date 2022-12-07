package base.restAssured;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.testng.annotations.Test;
import java.util.HashMap;

public class RestAssuredBuilderTest {

    @Test
    public void testGet() {
        HashMap<String, String> params = new HashMap<>();
        params.put("postId","2");
        Response getBuilder = new RestAssuredBuilder.GetBuilder()
                .setBaseUri("https://jsonplaceholder.typicode.com")
                .setContentType(ContentType.JSON)
                .setPath("/comments")
                .setQueryParams(params)
                .build();

        ResponseBody<?> responseBody = getBuilder.body();
        System.out.println(responseBody.prettyPrint());
    }

    @Test
    public void testPost() {
        HashMap<String,String> body = new HashMap<>();
        body.put("title","foo");
        body.put("body","1");
        body.put("userId","1");

        Response getBuilder = new RestAssuredBuilder.PostBuilder()
                .setBaseUri("https://jsonplaceholder.typicode.com")
                .setContentType(ContentType.JSON)
                .setPath("/posts")
                .setBody(body)
                .build();

        ResponseBody<?> responseBody = getBuilder.body();
        System.out.println(responseBody.prettyPrint());
    }

    @Test
    public void testPut() {
        HashMap<String,String> body = new HashMap<>();
        body.put("title","foo");
        body.put("body","baz");
        body.put("userId","1");
        body.put("id", "1");

        Response getBuilder = new RestAssuredBuilder.PutBuilder()
                .setBaseUri("https://jsonplaceholder.typicode.com")
                .setContentType(ContentType.JSON)
                .setPath("/posts/1")
                .setBody(body)
                .build();

        ResponseBody<?> responseBody = getBuilder.body();
        System.out.println(responseBody.prettyPrint());
    }
}
