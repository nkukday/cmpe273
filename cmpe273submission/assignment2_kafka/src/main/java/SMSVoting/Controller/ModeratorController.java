package SMSVoting.Controller;


import SMSVoting.Model.Moderator;
import SMSVoting.Model.Poll;
import SMSVoting.Services.DataSingleton;
import SMSVoting.Services.ModeratorService;
import SMSVoting.Services.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/moderators")
public class ModeratorController {

    @Autowired
    ModeratorService mService = new ModeratorService();

    @Autowired
    PollService pService = new PollService();

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method=RequestMethod.POST)
       public Map<String,Object> addmoderator(@Valid @RequestBody Moderator moderator,BindingResult result,HttpServletRequest servlet) {



        if(moderator.getName()==null || moderator.getName().trim().equals(""))
        {
            throw new BadRequestException("Bad Request Exception");
        }
        if(result.hasErrors())
        {
            throw new BadRequestException("Bad Request Exception");
        }

        return mService.addModerator(moderator).toMapWithOutResult();
    }

    @RequestMapping(value={"/{id}"},method=RequestMethod.GET)
    public Map<String,Object> addModerator(@PathVariable(value="id") long id,HttpServletRequest servlet){

        DataSingleton.userCheck(servlet);
        return mService.getModeratorAt(id).toMapWithOutResult();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value={"/{id}"},method=RequestMethod.PUT)
    public Map<String,Object> addModerator(@PathVariable(value="id") long id,@Valid @RequestBody Moderator moderator, BindingResult result,HttpServletRequest servlet){
        DataSingleton.userCheck(servlet);

        if(result.hasErrors())
        {
            throw new BadRequestException("Bad Request");
        }
        return mService.updateModeratorAt(id,moderator).toMapWithOutResult();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value={"/{id}/polls"},method=RequestMethod.POST)
    public Map<String, Object> createPoll(@PathVariable(value="id") long moderatorId,@Valid @RequestBody Poll poll,BindingResult result,HttpServletRequest servlet) {
        DataSingleton.userCheck(servlet);
        if(result.hasErrors())
        {
            throw new BadRequestException("Bad Request");
        }
        return pService.createPoll(poll,moderatorId);
    }

    @RequestMapping(value={"/{mid}/polls/{pid}"},method=RequestMethod.GET)
    public Map<String, Object> getPoll(@PathVariable(value="mid") long moderatorId,@PathVariable(value="pid") String pollId,HttpServletRequest servlet) {
        DataSingleton.userCheck(servlet);
        return pService.getPollWithModerator(moderatorId,pollId);
    }
    @RequestMapping(value={"/{mid}/polls"},method=RequestMethod.GET)
    public ArrayList<Map<String, Object>> getAllPolls(@PathVariable(value="mid") long moderatorId,HttpServletRequest servlet) {
        DataSingleton.userCheck(servlet);
        return  pService.getPollsWithModerator(moderatorId);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value={"/{mid}/polls/{pid}"},method=RequestMethod.DELETE)
    public void deletePoll(@PathVariable(value="mid") long moderatorId,@PathVariable(value="pid") String pollId,HttpServletRequest servlet) {
        DataSingleton.userCheck(servlet);
        pService.deletePoll(moderatorId, pollId);
    }




}