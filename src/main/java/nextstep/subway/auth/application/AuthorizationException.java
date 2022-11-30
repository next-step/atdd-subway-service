package nextstep.subway.auth.application;

import nextstep.subway.exception.ErrorMessage;

public class AuthorizationException extends RuntimeException {

    private final ErrorMessage errorMessage;

    public AuthorizationException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }

}
