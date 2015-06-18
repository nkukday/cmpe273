package sms.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sms.Model.Polls;
import sms.Service.ModeratorService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neerajakukday on 2/27/15.
 */

@RestController

@RequestMapping("/api/v1/")
public class PollController {


    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public final class BadRequestException extends IllegalArgumentException {

        public BadRequestException(String message) {
            super(message);
        }

    }
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public final class invalidLogin extends IllegalArgumentException {

        public invalidLogin(String message) {
            super(message);
        }

    }
    ModeratorService pService = new ModeratorService();

    //Get Without Results
    @RequestMapping(value = "/polls/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody HashMap getPolls(@PathVariable(value="id") String id)
    {
        pService.checkPoll(id);
        return pService.getPollsAt(id);
    }

    //Get With Results
    @RequestMapping(value = "/moderators/{id}/polls/{pid}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Polls getModeratorPolls(@PathVariable(value="id") int id, @PathVariable(value="pid") String pid, HttpServletRequest req)
    {
        userCheck(req);
        return pService.getModeratorPollsAt(id, pid);


    }

    //Get Polls for a Moderator
    @RequestMapping(value = "/moderators/{id}/polls", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    List getAllModeratorPolls(@PathVariable(value="id") int id, HttpServletRequest req)
    {
        userCheck(req);
        return pService.getAllModeratorPollsAt(id);


    }
    //create a poll
    @RequestMapping(value = "/moderators/{id}/polls",method= RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    HashMap newPoll(@PathVariable (value="id") int id,@Valid @RequestBody Polls poll, BindingResult result, HttpServletRequest req)
    {
        userCheck(req);

        if(result.hasErrors())
        {
            throw new BadRequestException("Incorrect Parameters");

        }
        return pService.newPollAt(id,poll);
    }

    //delete a poll
    @RequestMapping(value = "/moderators/{id}/polls/{pid}",method= RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public @ResponseBody
    void deletePoll(@PathVariable(value="id") int id, @PathVariable(value="pid") String pid, HttpServletRequest req)
    {
        userCheck(req);
        pService.checkModerator(id);
        pService.checkPoll(pid);

        pService.deletePollAt(id, pid);

    }

    //voting app
    @RequestMapping(value = "/polls/{poll_id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public @ResponseBody
    void voteApp(@PathVariable(value="poll_id") String pid,@RequestParam(value="choice") int choice_index ){

         pService.voteAppAt(pid, choice_index);
    }

    //Authentication method

    public void userCheck(HttpServletRequest req){
        String authorization = req.getHeader("Authorization");

        if(authorization == null)
            throw new invalidLogin("Please provide username and password to access system");

        String credentials = authorization.substring("Basic".length()).trim();
        byte[] decoded = DatatypeConverter.parseBase64Binary(credentials);
        String decodedString = new String(decoded);
        String[] actualCredentials = decodedString.split(":");

        if(actualCredentials.length == 0)
            throw new invalidLogin("Username Or Password is Empty");

        String ID = actualCredentials[0];
        String Password = actualCredentials[1];

        if(!(ID.equals("foo") && Password.equals("bar"))){
            throw new invalidLogin("UserName Or Password is not Valid");
        }

    }
}


