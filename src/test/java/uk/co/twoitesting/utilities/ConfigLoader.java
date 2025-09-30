package uk.co.twoitesting.utilities;
// Declares the package this class belongs to (helps organize related classes).

import java.io.*;
import java.util.Properties;
// Imports the Properties class, which is used to store key-value pairs from a config file.

public class ConfigLoader {
// Declares a public class named ConfigLoader.

    private static Properties properties = new Properties();
    // Creates a static Properties object to hold configuration values (shared across the class).

    static {
        // Static initialization block – runs once when the class is first loaded into memory.

        try {
            // Start of try block to handle potential exceptions when loading the file.

            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
            // Opens a file input stream to read the "config.properties" file from the given path.

            properties.load(fis);
            // Loads the key-value pairs from the file into the Properties object.

        } catch (IOException e) {
            // Catches any IOException (e.g., file not found, access denied).

            e.printStackTrace();
            // Prints the stack trace of the error to help with debugging.

            throw new RuntimeException("Failed to load config.properties");
            // Throws a RuntimeException to stop the program if the config file can’t be loaded.

        }
    }

    public static String get(String key) {
        // Public static method to retrieve a property value by its key.

        return properties.getProperty(key);
        // Returns the value associated with the given key from the properties file.

    }
}
