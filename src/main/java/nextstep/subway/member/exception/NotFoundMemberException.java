package nextstep.subway.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotFoundMemberException extends NoSuchElementException {
    public static final String NOT_FOUND_ELEMENT = "해당하는 사용자 정보를 찾을 수 없습니다.";

    public NotFoundMemberException() {
        super(NOT_FOUND_ELEMENT);
    }
}


