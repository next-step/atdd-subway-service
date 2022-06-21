package nextstep.subway.station.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotFoundStationException extends NoSuchElementException {
    public static final String NOT_FOUND_ELEMENT = "해당하는 역 정보를 찾을 수 없습니다.";

    public NotFoundStationException() {
        super(NOT_FOUND_ELEMENT);
    }
}
