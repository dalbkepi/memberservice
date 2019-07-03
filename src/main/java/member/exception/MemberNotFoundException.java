package member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such member")
public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(long id) {
        super("unable to find member with id [" + id + "]");
    }
}