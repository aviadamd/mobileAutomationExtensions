package base.jsonJacksonExtensions;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileReader;
import java.util.Optional;

@SuppressWarnings("deprecation")
public class JsonObjectReader extends JSONObject {
    private JSONObject jsonObject;
    Logger logger = LoggerFactory.getLogger(JsonObjectReader.class);

    public JsonObjectReader(String jsonFilePath) {
        try {
            JSONParser parser = new JSONParser();
            FileReader fr = new FileReader(jsonFilePath);
            this.jsonObject =  (JSONObject) parser.parse(fr);
        } catch (Exception exception) {
            logger.error(exception.getMessage());
        }
    }

    public Optional<String> getAttribute(String attributeName) {
        try {
            return Optional.ofNullable((String) this.jsonObject.get(attributeName));
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            return Optional.empty();
        }
    }

    public Optional<JSONArray> getArray(String arrayName) {
        try {
            return Optional.ofNullable((JSONArray) this.jsonObject.get(arrayName));
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            return Optional.empty();
        }
    }
}
