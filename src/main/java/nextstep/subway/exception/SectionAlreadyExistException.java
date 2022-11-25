package nextstep.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SectionAlreadyExistException extends RuntimeException {
    public SectionAlreadyExistException() {
        super("이미 등록된 구간 입니다.");
    }
}
