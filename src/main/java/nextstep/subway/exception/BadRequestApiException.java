package nextstep.subway.exception;

import nextstep.subway.common.ErrorCode;

public class BadRequestApiException extends RuntimeException {

    public BadRequestApiException(ErrorCode errorCode) {
        super(errorCode.name());
    }
}

