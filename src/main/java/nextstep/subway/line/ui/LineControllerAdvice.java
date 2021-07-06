package nextstep.subway.line.ui;

import nextstep.subway.line.dto.LineExceptionResponse;
import nextstep.subway.line.exception.BelowZeroDistanceException;
import nextstep.subway.line.exception.UnaddableSectionException;
import nextstep.subway.line.exception.UndeletableStationInSectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.*;


@RestControllerAdvice(basePackages = "nextstep.subway.line.ui")
public class LineControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(LineControllerAdvice.class);

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public LineExceptionResponse dataIntegrityViolationException(HttpServletRequest request, DataIntegrityViolationException e) {
        log.error(e.getMessage(), e);

        String exceptionMessage = e.getMessage();
        LineExceptionResponse response = null;
        if (exceptionMessage != null && exceptionMessage.contains("Unique index or primary key violation")) {
            response = new LineExceptionResponse("요청 하신 이름이 이미 존재 하여 처리할 수 없습니다.");
        }
        return response;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public void entityNotFoundException(EntityNotFoundException e) {
        log.error(e.getMessage(), e);
    }

    @ExceptionHandler(UnaddableSectionException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public void unaddableSectionException(UnaddableSectionException e) {
        log.error(e.getMessage(), e);
    }

    @ExceptionHandler(UndeletableStationInSectionException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public void undeletableStationInSectionException(UndeletableStationInSectionException e) {
        log.error(e.getMessage(), e);
    }

    @ExceptionHandler(BelowZeroDistanceException.class)
    @ResponseStatus(BAD_REQUEST)
    public void belowZeroDistanceException(BelowZeroDistanceException e) {
        log.error(e.getMessage(), e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<LineExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String errorMessage = e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();

        return new ResponseEntity<>(new LineExceptionResponse(errorMessage), HttpStatus.BAD_REQUEST);
    }
}
