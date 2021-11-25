package nextstep.subway.exception.error;

import org.springframework.http.HttpStatus;

public interface ErrorCodeMapperType {
    HttpStatus getHttpStatus();

    String getMessage();

    int getStatusCode();
}
