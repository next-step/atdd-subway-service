package nextstep.subway.line.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DistanceException extends RuntimeException{
    public static final String DISTANCE_LENGTH_MINUS_MSG = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";

    public DistanceException() {
        super(DISTANCE_LENGTH_MINUS_MSG);
    }
}
