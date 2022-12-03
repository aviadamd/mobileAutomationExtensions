package pocTests;

import base.restTemplate.RestTemplateBuilderExtensions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.HashMap;

public class RestTemplateBuilderExtensionsTest {

    private final Logger logger = LoggerFactory.getLogger(RestTemplateBuilderExtensionsTest.class);
    private final ThreadLocal<RestTemplateBuilderExtensions> templateBuilderExtensions = new ThreadLocal<>();

    @BeforeMethod
    public void init() {
        this.templateBuilderExtensions.set(new RestTemplateBuilderExtensions());
    }

    @Test
    public void test1_templateBuilderExtensions() {
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", "1");
        ResponseEntity<String> typicode = this.templateBuilderExtensions
                .get()
                .setScheme("https")
                .setPath("posts")
                .setHost("jsonplaceholder.typicode.com")
                .setParams(params)
                .build(HttpMethod.GET);
        logger.info(typicode.getStatusCode().toString());
        logger.info(typicode.getHeaders().toSingleValueMap().toString());
        logger.info(typicode.getBody());
    }
}