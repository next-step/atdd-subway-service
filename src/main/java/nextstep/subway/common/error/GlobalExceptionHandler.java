package nextstep.subway.common.error;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.auth.application.MemberNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String NOT_AUTHORIZED = "권한이 없습니다.";
    public static final String MEMBER_NOT_FOUND = "사용자가 존재하지 않습니다.";

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthorizationException.class)
    public ErrorResponse handleAuthorization() {
        return new ErrorResponse(NOT_AUTHORIZED);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MemberNotFoundException.class)
    public ErrorResponse handleMemberNotFound() {
        return new ErrorResponse(MEMBER_NOT_FOUND);
    }
}
