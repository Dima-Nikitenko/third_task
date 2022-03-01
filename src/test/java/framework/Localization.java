package framework;

public class Localization {

    public static PropertiesHandler property;

    public void setupLocale(String language) {
        String convertString = language.toLowerCase();
        property = new PropertiesHandler(String.format("localization/%s.properties", convertString));
    }

    public static PropertiesHandler setupLoggerLocale(String language) {
        String convertString = language.toLowerCase();
        property = new PropertiesHandler(String.format("localization/logger/log_%s.properties", convertString));
        return property;
    }

    public String getLocalizedElementProperty(String string) {
        String betaString = string.toLowerCase();
        return property.getProperty(betaString);
    }
}
