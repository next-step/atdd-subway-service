package nextstep.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PathCannotFindException extends RuntimeException {
    public PathCannotFindException() {
        super("경로 조회가 불가능합니다.");
    }
}
