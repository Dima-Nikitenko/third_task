package framework;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesHandler {

    private final Properties property = new Properties();

    public PropertiesHandler(final String resourceName) {
       setupFileReader(property, resourceName);
    }

    private Properties setupFileReader(final Properties objProperty, final String resourceName) {
        try (InputStream data = this.getClass().getClassLoader().getResourceAsStream(resourceName)) {
            property.load(data);
        } catch (IOException e) {
            System.out.printf("%s file not found or cannot be read.", resourceName);
        }
        return objProperty;
    }

    public String getProperty(final String key) {
        if(key!= null) return property.getProperty(key);
        else throw new RuntimeException("Browser name is not specified in the config.properties file.");
    }
}
