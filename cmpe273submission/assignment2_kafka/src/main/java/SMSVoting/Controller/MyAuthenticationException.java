package SMSVoting.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class MyAuthenticationException extends IllegalArgumentException {

    public MyAuthenticationException(String message) {
        super(message);
    }
}