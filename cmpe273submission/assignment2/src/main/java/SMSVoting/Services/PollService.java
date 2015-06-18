package SMSVoting.Services;

import SMSVoting.Controller.BadRequestException;
import SMSVoting.Model.Moderator;
import SMSVoting.Model.Poll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Map;

@Repository
public class PollService {

    public static final String COLLECTION_NAME = "poll";
    @Autowired
    private MongoTemplate mongoTemplate;

    public Map<String, Object> createPoll(Poll poll, long moderatorId) {

        Poll p = new Poll(DataSingleton.generateAlphNumAuthCode(), poll.getQuestion(), poll.getStartedAt(), poll.getExpiredAt(), poll.getChoice());

        Moderator m;
        if (mongoTemplate.collectionExists(Moderator.class)) {
            m = mongoTemplate.findById(moderatorId, Moderator.class, "moderator");
            if (m == null)
                throw new BadRequestException("No moderator found!");
        } else {
            throw new BadRequestException("No moderator found!");
        }
        mongoTemplate.insert(p, COLLECTION_NAME);
        m.addPoll(p.getId());
        mongoTemplate.save(m, "moderator");

        return mongoTemplate.findById(p.getId(), Poll.class, COLLECTION_NAME).toMapWithoutResult();

    }

    public Map<String, Object> getPoll(String pollId) {

        Poll p = mongoTemplate.findById(pollId, Poll.class, COLLECTION_NAME);

        if (p == null)
            throw new BadRequestException("No Poll with the id");


        return p.toMapWithoutResult();
    }

    public Map<String, Object> getPollWithModerator(long moderatorId, String pollId) {

        if (mongoTemplate.collectionExists(Moderator.class)) {
            if (mongoTemplate.findById(moderatorId, Moderator.class, "moderator") == null)
                throw new BadRequestException("No moderator found!");
        } else {
            throw new BadRequestException("No moderator found!");
        }
        Poll poll = mongoTemplate.findById(pollId, Poll.class, COLLECTION_NAME);

        if (poll == null) {
            throw new BadRequestException("No Poll with the id");
        }

        return poll.toMapWithResult();
    }

    public ArrayList<Map<String, Object>> getPollsWithModerator(long moderatorId) {

        Moderator m;
        if (mongoTemplate.collectionExists(Moderator.class)) {
            m = mongoTemplate.findById(moderatorId, Moderator.class, "moderator");
            if (m == null)
                throw new BadRequestException("No moderator found!");
        } else {
            throw new BadRequestException("No moderator found!");
        }
        ArrayList<String> pollIds = m.getPolls();
        if (pollIds.size() == 0) {
            throw new BadRequestException("No polls with for the moderator");
        }
        ArrayList<Map<String, Object>> map = new ArrayList<>();
        for (String pollId : pollIds) {

            map.add(getPollWithModerator(moderatorId, pollId));
        }
        return map;

    }

    public void deletePoll(long moderatorId, String pollId) {
        Moderator m;
        if (mongoTemplate.collectionExists(Moderator.class)) {
            m = mongoTemplate.findById(moderatorId, Moderator.class, "moderator");
            if (m == null)
                throw new BadRequestException("No moderator found!");
        } else {
            throw new BadRequestException("No moderator found!");
        }
        Poll poll = mongoTemplate.findById(pollId, Poll.class, COLLECTION_NAME);

        if (poll == null) {
            throw new BadRequestException("No Poll with the id");
        }

        m.removePoll(pollId);
        mongoTemplate.save(m, "moderator");

        mongoTemplate.remove(poll, COLLECTION_NAME);

    }

    public void votePoll(String pollId, int choiceno) {


        Poll poll = mongoTemplate.findById(pollId, Poll.class, COLLECTION_NAME);
        if (poll == null) {
            throw new BadRequestException("No Poll with the id");
        }
        if (poll.getChoice().size() <= choiceno) {
            throw new BadRequestException("Invalid choice");
        }
        poll.voteChoice(choiceno);

        mongoTemplate.save(poll, COLLECTION_NAME);
    }

}
