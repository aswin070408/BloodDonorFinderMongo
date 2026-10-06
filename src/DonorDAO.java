import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;

public class DonorDAO {

    private MongoCollection<Document> col() {
        return MongoConnection.db().getCollection("donors");
    }

    public boolean usernameExists(String username) {
        return col().countDocuments(eq("username", username)) > 0;
    }

    public void register(Donor d, String password) throws Exception {
        Document doc = new Document("username", d.username)
                .append("passwordHash", PasswordUtil.create(password))
                .append("name", d.name)
                .append("age", d.age)
                .append("gender", d.gender)
                .append("bloodGroup", d.bloodGroup)
                .append("city", d.city)
                .append("phone", d.phone)
                .append("lastDonation", d.lastDonation == null ? null : d.lastDonation.toString())
                .append("createdAt", new java.util.Date());
        col().insertOne(doc);
    }

    // Returns the donor if username/password are correct, otherwise null
    public Donor login(String username, String password) throws Exception {
        Document doc = col().find(eq("username", username)).first();
        if (doc != null && PasswordUtil.verify(password, doc.getString("passwordHash")))
            return map(doc);
        return null;
    }

    public Donor findById(String id) {
        try {
            Document doc = col().find(eq("_id", new ObjectId(id))).first();
            return doc == null ? null : map(doc);
        } catch (IllegalArgumentException e) {
            return null; // invalid id text
        }
    }

    // Admin list, optional filters (blank = no filter)
    public List<Donor> list(String bloodGroup, String city) {
        List<Bson> filters = new ArrayList<>();
        if (bloodGroup != null && !bloodGroup.isEmpty())
            filters.add(eq("bloodGroup", bloodGroup));
        if (city != null && !city.isEmpty())
            filters.add(regex("city", Pattern.quote(city), "i"));
        Bson filter = filters.isEmpty() ? new Document() : and(filters);

        List<Donor> list = new ArrayList<>();
        for (Document doc : col().find(filter).sort(Sorts.ascending("_id")))
            list.add(map(doc));
        return list;
    }

    public void recordDonationToday(String id) {
        col().updateOne(eq("_id", new ObjectId(id)),
                Updates.set("lastDonation", LocalDate.now().toString()));
    }

    private Donor map(Document doc) {
        Donor d = new Donor();
        d.id = doc.getObjectId("_id").toHexString();
        d.username = doc.getString("username");
        d.name = doc.getString("name");
        Integer age = doc.getInteger("age");
        d.age = age == null ? 0 : age;
        d.gender = doc.getString("gender");
        d.bloodGroup = doc.getString("bloodGroup");
        d.city = doc.getString("city");
        d.phone = doc.getString("phone");
        String last = doc.getString("lastDonation");
        d.lastDonation = (last == null || last.isEmpty()) ? null : java.sql.Date.valueOf(last);
        return d;
    }
}
