package nextstep.subway.advice;

import nextstep.subway.exception.SectionNotConnectedException;
import nextstep.subway.exception.StationsNotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        LOG.error("GlobalExceptionHandler.handleDataIntegrityViolationException : ", e);
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgsException(IllegalArgumentException e) {
        LOG.error("GlobalExceptionHandler.handleIllegalArgsException : ", e);
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(value = SectionNotConnectedException.class)
    public ResponseEntity handleSectionNotConnectedException(SectionNotConnectedException e) {
        LOG.error("GlobalExceptionHandler.handleSectionNotConnectedException : ", e);
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(value = StationsNotExistException.class)
    public ResponseEntity handleStationsNotExistException(StationsNotExistException e) {
        LOG.error("GlobalExceptionHandler.handleStationsNotExistException : ", e);
        return ResponseEntity.notFound().build();
    }
}
