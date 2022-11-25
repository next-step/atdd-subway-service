package nextstep.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidSectionException extends RuntimeException {
    public InvalidSectionException() {
        super("등록할 수 없는 구간 입니다.");
    }
}
