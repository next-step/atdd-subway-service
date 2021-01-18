package nextstep.subway.advice;

import nextstep.subway.advice.exception.FavoriteBadRequestException;
import nextstep.subway.advice.exception.MemberBadRequestException;
import nextstep.subway.advice.exception.SectionBadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class SubwayExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {SectionBadRequestException.class, MemberBadRequestException.class, FavoriteBadRequestException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseEntity check(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
