package SMSVoting.Services;

import SMSVoting.Controller.MyAuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.util.Random;

public class DataSingleton {

    public static long generateNumAuthCode() {

        long range = 1234567L;
        Random r = new Random();
        long number = (long) (r.nextDouble() * range);

        return number;

    }

    public static String generateAlphNumAuthCode() {

        Random r = new Random();
        String thirteenAsBase36 = Long.toString(r.nextLong(), 36);

        return thirteenAsBase36.substring(0, 6);

    }

    public static void userCheck(HttpServletRequest req) {
        String authorization = req.getHeader("Authorization");

        if (authorization == null)
            throw new MyAuthenticationException("Please provide username and password to access system");

        String credentials = authorization.substring("Basic".length()).trim();
        byte[] decoded = DatatypeConverter.parseBase64Binary(credentials);
        String decodedString = new String(decoded);
        String[] actualCredentials = decodedString.split(":");

        if (actualCredentials.length < 2)
            throw new MyAuthenticationException("Username Or Password is Empty");

        String ID = actualCredentials[0];
        String Password = actualCredentials[1];

        if (!(ID.equals("foo") && Password.equals("bar"))) {
            throw new MyAuthenticationException("Authentication Error!");
        }

    }


}
