package nextstep.subway.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCodeMapperType {
    HttpStatus getHttpStatus();

    String getMessage();

    int getStatusCode();
}
