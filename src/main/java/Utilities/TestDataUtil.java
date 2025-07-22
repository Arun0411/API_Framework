package Utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Utility class for reading JSON test data using Jackson ObjectMapper.
 * Helps in extracting node-based JSON values without needing POJO classes.
 */
public class TestDataUtil {

    private static final ConfigReader configReader = new ConfigReader();

    /**
     * Reads a JSON file and returns the root JsonNode object.
     *
     * @param filePath Path of the JSON file (e.g., src/test/resources/testData/users.json)
     * @return Root JsonNode
     * @throws IOException if file read fails
     */
    public static JsonNode getJsonNode(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(new File(filePath));
    }

    /**
     * Retrieves a nested JsonNode (e.g., admin/user) from a JSON file.
     *
     * @param userType Top-level key (e.g., "admin", "user")
     * @return JsonNode representing that user block
     * @throws IOException if file read fails
     */
    public static JsonNode getUser(String userType) throws IOException {
        JsonNode root = getJsonNode(configReader.getUserTestData());
        return root.get(userType); // Returns node under that key
    }

    public static String getToken(String tokenType) {
        JsonNode root = null;
        try {
            root = getJsonNode(configReader.getTokenTestData());
        } catch (IOException e) {
            return "";
        }
        return getField(root, tokenType); // Returns node under that key
    }

    /**
     * Extracts a specific field value (as String) from a JsonNode.
     *
     * @param node      JsonNode object (e.g., admin)
     * @param fieldName Field name (e.g., "email", "password")
     * @return String value of the field
     */
    public static String getField(JsonNode node, String fieldName) {
        return node.get(fieldName).asText(); // Convert to String
    }
}
