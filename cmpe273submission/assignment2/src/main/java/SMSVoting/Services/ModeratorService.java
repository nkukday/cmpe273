package SMSVoting.Services;

import SMSVoting.Controller.BadRequestException;
import SMSVoting.Model.Moderator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Repository
public class ModeratorService {
    public static final String COLLECTION_NAME = "moderator";
    @Autowired
    private MongoTemplate mongoTemplate;

    public Moderator addModerator(Moderator moderator) {
        Moderator m = new Moderator(DataSingleton.generateNumAuthCode(), moderator.getName(), moderator.getEmail(), moderator.getPassword(), getDate());
        if (!mongoTemplate.collectionExists(Moderator.class)) {
            mongoTemplate.createCollection(Moderator.class);
        }
        mongoTemplate.insert(m, COLLECTION_NAME);


        return m;

    }

    public Moderator getModeratorAt(long i) {

        Moderator m;
        if (mongoTemplate.collectionExists(Moderator.class)) {
            m = mongoTemplate.findById(i, Moderator.class, COLLECTION_NAME);
            if (m == null)
                throw new BadRequestException("No moderator found!");
        } else {
            throw new BadRequestException("No moderator found!");
        }
        return m;

    }

    public Moderator updateModeratorAt(long id, Moderator mod) {

        Moderator m = getModeratorAt(id);

        m.setEmail(mod.getEmail());
        m.setPassword(mod.getPassword());

        mongoTemplate.save(m, COLLECTION_NAME);

        return mongoTemplate.findById(id, Moderator.class, COLLECTION_NAME);
    }

    public String getDate() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(timeZone);
        return (df.format(new Date()));
    }


}
