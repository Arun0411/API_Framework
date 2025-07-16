package Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Utilities {

    public static String getProperty(String key) {
        Properties properties = new Properties();
        try {
            FileInputStream input = new FileInputStream("src/main/resources/Framework.properties");
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load framework.properties: " + e.getMessage());
        }
        return properties.getProperty(key);
    }
}
