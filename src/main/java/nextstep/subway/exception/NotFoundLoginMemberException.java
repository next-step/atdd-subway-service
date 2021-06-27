package nextstep.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundLoginMemberException extends NoSuchElementException {
    public NotFoundLoginMemberException() {
    }

    public NotFoundLoginMemberException(String message) {
        super(message);
    }
}
