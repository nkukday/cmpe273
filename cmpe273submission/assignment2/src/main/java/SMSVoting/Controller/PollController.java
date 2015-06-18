package SMSVoting.Controller;


import SMSVoting.Services.ModeratorService;
import SMSVoting.Services.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/polls")
public class PollController {

    @Autowired
    ModeratorService mService = new ModeratorService();
    @Autowired
    PollService pService = new PollService();

    @RequestMapping(value={"/{id}"},method=RequestMethod.GET)
    public Map<String, Object> getPoll(@PathVariable(value = "id") String pollId) {
        return pService.getPoll(pollId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value={"/{id}"},method=RequestMethod.PUT)
    public void voteAPoll(@PathVariable(value = "id") String pollId,@RequestParam("choice")int choiceno) {
         pService.votePoll(pollId,choiceno);
    }



}
