package base.restAssured;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.hamcrest.core.IsEqual.equalTo;

@Slf4j
public class RestAssuredBuilderTest {

    @Test
    public void testGet() {
        HashMap<String, String> params = new HashMap<>();
        params.put("postId","2");
        Response response = new RestAssuredBuilder.RequestBuilder()
                .setBaseUri("https://jsonplaceholder.typicode.com")
                .setPath("/comments")
                .setContentType(ContentType.JSON)
                .setQueryParams(params)
                .getResponse(Method.GET);

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200,"status code return 200 response");

        JsonPath jsonPathEvaluator = response.jsonPath();
        log.info(jsonPathEvaluator.get("id").toString());

        ResponseBody<?> responseBody = response.body();
        log.info(responseBody.prettyPrint());
    }
    @Test
    public void testPost() {
        Response response = new RestAssuredBuilder.RequestBuilder()
                .setBaseUri("https://jsonplaceholder.typicode.com")
                .setContentType(ContentType.JSON)
                .setPath("/posts")
                .setBody(Map.of("title", "foo", "body", "1", "userId", "1"))
                .getResponse(Method.POST);

        JsonPath asJsonPath = new RestAssuredBuilder().getJsonPath(response);
        asJsonPath.prettyPeek();
        // headers response
        List<Header> headersList = response.headers().asList();
        headersList.forEach(header -> log.info(header.getName() + " | " + header.getValue()));
        response.headers().getValue("");

        //print json body
        ResponseBody<?> responseBody = response.body();
        log.info(responseBody.prettyPrint());

        //get single value from json
        JsonPath jsonPath = response.jsonPath();
        log.info(jsonPath.get("title").toString());
        if (Matchers.matchesRegex("^[a-z0-9]+$").matches(jsonPath.get("title").toString())) {
            log.info("find");
        }

        if (Matchers.greaterThan(0).matches(jsonPath.get("id").toString())) {
            log.info("find");
        }
        //print response time
        long responseTime = response.getTime();
        log.info("Response time in milliseconds: " + responseTime);

        //get a field value from nested JSON
        String price = jsonPath.getString("Items.Price");
        log.info("Price is: " + price);

        //convert JSON to string
        JsonPath jsonPath1 = new JsonPath(response.asString());

        //Zip for 2nd Location array
        String zip = jsonPath1.getString("Location[1].zip");

        response.then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .body("title", equalTo("foo"), "body", equalTo("1"), "userId", equalTo("1"), "id", equalTo(101))
                .body("token", Matchers.matchesRegex("^[a-z0-9]+$"))
                .body("bookingid", Matchers.hasItems(91,10));
    }

    @Test
    public void testPut() {
        HashMap<String,String> body = new HashMap<>();
        body.put("title","foo");
        body.put("body","baz");
        body.put("userId","1");
        body.put("id", "1");

        Response getBuilder = new RestAssuredBuilder.RequestBuilder()
                .setBaseUri("https://jsonplaceholder.typicode.com")
                .setContentType(ContentType.JSON)
                .setPath("/posts/1")
                .setBody(body)
                .getResponse(Method.PUT);

        ResponseBody<?> responseBody = getBuilder.body();
        log.info(responseBody.prettyPrint());
    }
}
