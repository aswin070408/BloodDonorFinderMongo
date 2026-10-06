import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {
    private static MongoClient client;

    public static String env(String key, String fallback) {
        String value = System.getenv(key);
        return (value == null || value.isEmpty()) ? fallback : value;
    }

    public static synchronized MongoDatabase db() {
        if (client == null) {
            String uri = env("MONGO_URI", "mongodb://localhost:27017");
            client = MongoClients.create(uri);
        }

        return client.getDatabase(
            env("MONGO_DB", "blood_donor_web")
        );
    }
}