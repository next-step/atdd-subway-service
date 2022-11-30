package nextstep.subway.common;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.exception.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class SubWayExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ DataIntegrityViolationException.class, IllegalArgumentException.class,
            NotFoundException.class, CannotConnectSectionException.class, UpdateExistingSectionException.class,
            CannotDeleteSectionException.class, CannotGenerateStationGraphException.class,
            CannotFindPathException.class, AuthorizationException.class})
    protected ResponseEntity<Void> handle() {
        return ResponseEntity.badRequest().build();
    }
}
