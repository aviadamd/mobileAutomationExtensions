package base.jsonJacksonExtensions;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.testng.annotations.Test;

public class JsonKeyValue {
    private String key;
    private JsonNode value;

    @Test
    public void aVoid() {
        System.out.println(getJsonValue(jsonString,"title"));
        System.out.println(getJsonValue(jsonString,"ID"));
        System.out.println(getJsonValue(jsonString,"SortAs"));
        System.out.println(getJsonValue(jsonString,"para"));
    }

    String jsonString = "{\n" +
            "    \"glossary\": {\n" +
            "        \"title\": \"example glossary\",\n" +
            "\t\t\"GlossDiv\": {\n" +
            "            \"title\": \"S\",\n" +
            "\t\t\t\"GlossList\": {\n" +
            "                \"GlossEntry\": {\n" +
            "                    \"ID\": \"SGML\",\n" +
            "\t\t\t\t\t\"SortAs\": \"SGML\",\n" +
            "\t\t\t\t\t\"GlossTerm\": \"Standard Generalized Markup Language\",\n" +
            "\t\t\t\t\t\"Acronym\": \"SGML\",\n" +
            "\t\t\t\t\t\"Abbrev\": \"ISO 8879:1986\",\n" +
            "\t\t\t\t\t\"GlossDef\": {\n" +
            "                        \"para\": \"A meta-markup language, used to create markup languages such as DocBook.\",\n" +
            "\t\t\t\t\t\t\"GlossSeeAlso\": [\"GML\", \"XML\"]\n" +
            "                    },\n" +
            "\t\t\t\t\t\"GlossSee\": \"markup\"\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}";
    public static String getJsonValue(String jsonString, String field) {
        return jsonString.substring(
                        jsonString.indexOf(field),
                        jsonString.indexOf("\n", jsonString.indexOf(field)))
                .replace(field + "\": \"", "")
                .replace("\"", "")
                .replace(",","");
    }

    public String getKey() { return this.key; }
    public JsonNode getValue() { return this.value; }

    private void setKey(String key) { this.key = key; }
    private void setValue(JsonNode value) { this.value = value; }
}
