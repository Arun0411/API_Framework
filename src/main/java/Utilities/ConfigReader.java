package Utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private Properties properties;
    private final String defaultConfigFilePath = "src/test/resources/Framework.properties";

    public ConfigReader() {
        loadProperties(defaultConfigFilePath);
    }

    public ConfigReader(String propertyFilePath){
        loadProperties(propertyFilePath);
    }

    public void loadProperties(String filepath) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filepath));
            properties = new Properties();
            properties.load(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getUserTestData() {
        return getProperty("userTestData");
    }

    public String getTokenTestData() {
        return getProperty("tokenTestData");
    }
}
