package sms.Service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sms.Model.Moderator;
import sms.Model.Polls;

import java.util.*;

/**
 * Created by neerajakukday on 2/23/15.
 */
public class ModeratorService {

    public static int gen() {
        Random r = new Random( System.currentTimeMillis() );
        return 10000 + r.nextInt(20000);
    }

    private long generateNumAuthCode() {

        long range = 1234567L;
        Random r = new Random();
        long number = (long)(r.nextDouble()*range);

        return number;

    }
    public static String generateAlphaNumAuthCode() {

        long range = 123456789L;
        Random r = new Random();
        long number = (long)(r.nextDouble()*range);
        String thirteenAsBase36 = Long.toString(number, 36);

        return thirteenAsBase36;

    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public final class BadRequestException extends IllegalArgumentException {

        public BadRequestException(String message) {
            super(message);
        }

    }

    public static Map<Integer, Moderator> modData = new HashMap<Integer, Moderator>();

    public static Map<String, Polls> pollData = new HashMap<String, Polls>();

    public static Map<Integer, ArrayList<String>> moderatorPolls = new HashMap<Integer, ArrayList<String>>(); // moderator id & poll id



    public void checkModerator(int id){


        if(!(modData.containsKey(id)))
            throw new BadRequestException("Moderator does not exist");

    }

    public  void checkPoll(String p_id){
        if(!(pollData.containsKey(p_id)))
            throw new BadRequestException("Poll does not exist");
    }

    public void checkModeratorPolls(int id, String pid){
        List tmp = moderatorPolls.get(id);
        if(!(tmp.contains(pid)))
            throw new BadRequestException("Poll Not Found For This Moderator");
    }

   /* public void verifyModeratorPoll(int id, String pid){

        List<String> tmp = moderatorPolls.get(id);
        if(!(tmp.contains(pid)))
            throw new BadRequestException("No Permission");

    }*/

    String p_id;

    //GET
    public Moderator getModeratorAt(int id) {


        checkModerator(id);
        return modData.get(id);
    }

    //POST
    public Moderator newModeratorAt( Moderator moderator) {
        int id = gen();
        moderator.setId(id);
        modData.put(id, moderator);
        moderatorPolls.put(id, new ArrayList<String>());
        return moderator;

    }

    //PUT
    public Moderator updateModeratorAt(int id,  Moderator moderator) {

        Moderator m1 = getModeratorAt(id);
        /*if (moderator.getName() == null || moderator.getName().equals("")) {
            m1.setName(m1.getName());
        } else {
            m1.setName(moderator.getName());
        }*/
        m1.setEmail(moderator.getEmail());
        m1.setPassword(moderator.getPassword());
        return m1;

    }

    public HashMap getPollsAt(String id) {

        Polls poll = pollData.get(id);
        HashMap send = new HashMap();
        send.put("id", poll.getId());
        send.put("question", poll.getQuestion());
        send.put("started_at", poll.getStartedAt());
        send.put("expired_at", poll.getExpiredAt());
        send.put("choice", poll.getChoice());

        return send;

    }

    public Polls getModeratorPollsAt(int id, String pid) {

        checkModerator(id);
        checkPoll(pid);
        checkModeratorPolls(id, pid);
        //verifyModeratorPoll(id,pid);
        return pollData.get(pid);


    }

    public HashMap newPollAt( int id, Polls poll) {

        checkModerator(id);
        HashMap send = new HashMap();
        //String tmp = id + RandomStringUtils.randomAlphabetic(6).toUpperCase();
        String tmp = generateAlphaNumAuthCode().toUpperCase();


        p_id = tmp.toString();

        poll.setId(p_id);

        int size = poll.getChoice().size();
        List<Integer> tmpResults = new ArrayList<Integer>();
        for (int i = 0; i < size; i++)
            tmpResults.add(0);
        poll.setResults(tmpResults);
        pollData.put(p_id, poll);
        ArrayList<String> tmpList = moderatorPolls.get(id);
        tmpList.add(p_id);
        moderatorPolls.put(id, tmpList);

        //Populating hashmap tp omit results
        send.put("id", poll.getId());
        send.put("question", poll.getQuestion());
        send.put("started_at", poll.getStartedAt());
        send.put("expired_at", poll.getExpiredAt());
        send.put("choice", poll.getChoice());

        return send;

    }

    public List getAllModeratorPollsAt(int id) {

        checkModerator(id);
        if(moderatorPolls.get(id).size() == 0)
            throw new BadRequestException("This moderator has no polls");

        List<Polls> tmpPoll= new ArrayList<Polls>();
        List<String> tmp = moderatorPolls.get(id);
        for (String element : tmp) {
            tmpPoll.add(pollData.get(element));
        }

      return tmpPoll;

    }

    public void deletePollAt(int id, String pid) {


        List tmp = moderatorPolls.get(id);
        tmp.remove(pid);

        pollData.remove(pollData.get(pid));


    }

    public void voteAppAt(String pid, int choice) {

        checkPoll(pid);
        Polls polls = pollData.get(pid);
        List<Integer> tmpResults = polls.getResults();

        int max = polls.getChoice().size();

        if(choice >= max)
            throw new BadRequestException("Invalid Choice");

        int value = tmpResults.get(choice);
        value++;
        tmpResults.remove(choice);
        tmpResults.add(choice, value);
        polls.setResults(tmpResults);
    }




}
