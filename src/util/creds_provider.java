package util;

import io.github.cdimascio.dotenv.Dotenv;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;


// Utility class to provide EntityManagerFactory with credentials from .env file
// Loads only once at the beginning, and creates only one entitymanagerfactory instance

public class creds_provider {

    private static final EntityManagerFactory emf;

    static {

        Dotenv dotenv = Dotenv.load();

        Map<String, String> creds = new HashMap<>();
        creds.put("jakarta.persistence.jdbc.url", dotenv.get("DB_URL"));
        creds.put("jakarta.persistence.jdbc.user", dotenv.get("DB_USER"));
        creds.put("jakarta.persistence.jdbc.password", dotenv.get("DB_PASSWORD"));

        emf = Persistence.createEntityManagerFactory("devpu", creds);
    }

    public static EntityManagerFactory getEmf() {
        return emf;
    }
}
