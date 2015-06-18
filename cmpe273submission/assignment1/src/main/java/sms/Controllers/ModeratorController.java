package sms.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sms.Model.Moderator;
import sms.Service.ModeratorService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;

/**
 * Created by neerajakukday on 2/22/15.
 */

@RestController

@RequestMapping("/api/v1")
public class ModeratorController{

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String firstPage()
    {
        return "SMS Voting Application";

    }


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

    ModeratorService mService = new ModeratorService();


    @RequestMapping(value = "/moderators/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Moderator getModerator(@PathVariable(value="id") int id, HttpServletRequest req)
    {
        userCheck(req);
        return mService.getModeratorAt(id);

    }

    @RequestMapping(value = "/moderators",method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Moderator newModerator(@Valid @RequestBody Moderator moderator, BindingResult result)
    {
        if(moderator.getName() == null || moderator.getName().trim().equals(""))
            throw new BadRequestException("Name Field Is Absent Or Blank");

        if(result.hasErrors())
        {
            throw new BadRequestException("Request Body Is Missing Required Parameters");

        }
        return mService.newModeratorAt(moderator);
    }

    @RequestMapping(value = "/moderators/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    Moderator updateModerator(@PathVariable(value="id") int id,@Valid @RequestBody Moderator moderator,BindingResult result,HttpServletRequest req){
        userCheck(req);
        mService.checkModerator(id);
        if(result.hasErrors())
        {
            throw new BadRequestException("Request Body Is Missing Required Parameters");

        }

        return mService.updateModeratorAt(id,moderator);
    }


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
