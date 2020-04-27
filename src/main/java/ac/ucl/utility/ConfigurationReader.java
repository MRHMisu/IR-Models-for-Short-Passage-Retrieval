package ac.ucl.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class support reading file path from the config.properties file.
 */
public class ConfigurationReader {

    /**
     * Identify the property value by its name
     *
     * @param propertyName: A string that contain the property name in the config.properties file.
     * @return: The associated value assigned to that property name in the config.properties file.
     */
    public static String getPropertyByName(String propertyName) {
        Properties prop = new Properties();
        InputStream input = null;
        String requiredPorprerty = "";
        try {
            String filename = System.getProperty("user.dir") + "/src/main/resources/" + "config.properties";

            input = new FileInputStream(filename);
            //input = ConfigurationReader.class.getClassLoader().getResourceAsStream(filename);
            if (input == null) {
                System.out.println("Sorry, unable to find " + filename);
                return requiredPorprerty;
            }
            // load a properties file from class path, inside static method
            prop.load(input);
            // get the property value and print it out
            requiredPorprerty = prop.getProperty(propertyName);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return requiredPorprerty;

    }
}
