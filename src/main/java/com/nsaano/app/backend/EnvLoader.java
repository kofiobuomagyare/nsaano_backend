package com.nsaano.app.backend;
import io.github.cdimascio.dotenv.Dotenv;

public class EnvLoader {

    public static void loadEnv() {
        try {
            Dotenv dotenv = Dotenv.load(); // Load environment variables

            // Retrieve values from the .env file
            String dbHost = dotenv.get("DB_HOST");
            String dbPort = dotenv.get("DB_PORT");
            String dbName = dotenv.get("DB_NAME");
            String dbUser = dotenv.get("DB_USERNAME");
            String dbPassword = dotenv.get("DB_PASSWORD");

            // Construct the database URL
            String dbUrl = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;

            // Now use these variables in the Spring Boot configuration
            System.setProperty("spring.datasource.url", dbUrl);
            System.setProperty("spring.datasource.username", dbUser);
            System.setProperty("spring.datasource.password", dbPassword);

            // Optionally, you can also set Spring Security properties
            String securityUserName = dotenv.get("SPRING_SECURITY_USER_NAME");
            String securityUserPassword = dotenv.get("SPRING_SECURITY_USER_PASSWORD");

            System.setProperty("spring.security.user.name", securityUserName);
            System.setProperty("spring.security.user.password", securityUserPassword);

            // Debug prints
            System.out.println("Loaded environment variables:");
            System.out.println("DB URL: " + dbUrl);
            System.out.println("DB User: " + dbUser);
            System.out.println("DB Password: " + dbPassword);
        } catch (Exception e) {
            System.err.println("Error loading .env file: " + e.getMessage());
        }
    }
}
