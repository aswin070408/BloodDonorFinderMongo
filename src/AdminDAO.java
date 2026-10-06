import com.mongodb.client.MongoCollection;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class AdminDAO {

    private MongoCollection<Document> col() {
        return MongoConnection.db().getCollection("admins");
    }

    // Creates the admin account the first time the app runs
    public void ensureDefaultAdmin() throws Exception {
        if (col().countDocuments() == 0) {
            String pw = MongoConnection.env("ADMIN_PASSWORD", "admin123");
            col().insertOne(new Document("username", "admin")
                    .append("passwordHash", PasswordUtil.create(pw)));
            System.out.println("Default admin created (username: admin)");
        }
    }

    public boolean login(String username, String password) throws Exception {
        Document d = col().find(eq("username", username)).first();
        return d != null && PasswordUtil.verify(password, d.getString("passwordHash"));
    }
}
