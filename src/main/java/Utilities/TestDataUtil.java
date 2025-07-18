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

    // Base directory where all test data JSON files are stored
    private static final String BASE_PATH = "src/test/resources/testdata/";

    /**
     * Reads a JSON file and returns the root JsonNode object.
     *
     * @param fileName Name of the JSON file (e.g., users.json)
     * @return Root JsonNode
     * @throws IOException if file read fails
     */
    public static JsonNode getJsonNode(String fileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(); // JSON parser
        return objectMapper.readTree(new File(BASE_PATH + fileName)); // Parse JSON
    }

    /**
     * Retrieves a nested JsonNode (e.g., admin/user) from a JSON file.
     *
     * @param fileName Name of the JSON file
     * @param userType Top-level key (e.g., "admin", "user")
     * @return JsonNode representing that user block
     * @throws IOException if file read fails
     */
    public static JsonNode getUser(String fileName, String userType) throws IOException {
        JsonNode root = getJsonNode(fileName);
        return root.get(userType); // Returns node under that key
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
