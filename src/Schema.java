import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;

// MongoDB creates collections automatically. We only make usernames unique.
public class Schema {
    public static void ensureIndexes() {
        IndexOptions unique = new IndexOptions().unique(true);
        MongoConnection.db().getCollection("donors").createIndex(Indexes.ascending("username"), unique);
        MongoConnection.db().getCollection("admins").createIndex(Indexes.ascending("username"), unique);
    }
}
