package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for loading and accessing configuration properties.
 * The class is initialized automatically upon first use.
 */
public final class ConfigLoader {
    private static final String DEFAULT_CONFIG_FILE = "application-UATB.properties";
    private static final Properties properties = new Properties();
    
    // Static initialization block for loading properties
    static {
        loadProperties(DEFAULT_CONFIG_FILE);
    }

    // Private constructor to prevent instantiation
    private ConfigLoader() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Loads properties from the specified configuration file.
     * @param configFilePath Path to the properties file in the classpath
     * @throws RuntimeException if the file cannot be found or loaded
     */
    private static void loadProperties(String configFilePath) {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream(configFilePath)) {
            if (input == null) {
                throw new RuntimeException("Properties file not found in classpath: " + configFilePath);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file: " + configFilePath, e);
        }
    }

    /**
     * Gets a property value by key.
     * @param key The property key to look up
     * @return The property value, or null if not found
     */
    public static String get(String key) {
        return properties.getProperty(key);
    }

    /**
     * Gets a property value by key with a default value if not found.
     * @param key The property key to look up
     * @param defaultValue The default value to return if key not found
     * @return The property value or the default value if not found
     */
    public static String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
