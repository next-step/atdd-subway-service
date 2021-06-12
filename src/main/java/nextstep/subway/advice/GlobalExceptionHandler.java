package nextstep.subway.advice;

import nextstep.subway.exception.EmptySectionException;
import nextstep.subway.exception.IllegalSectionStateException;
import nextstep.subway.exception.InvalidDistanceException;
import nextstep.subway.exception.InvalidStationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private final static Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(value = {DataIntegrityViolationException.class})
  public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
    LOG.error("GlobalExceptionHandler.handleDataIntegrityViolationException : ", e);
    return ResponseEntity.badRequest().build();
  }

  @ExceptionHandler(value = {EntityNotFoundException.class})
  public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException e) {
    LOG.error("GlobalExceptionHandler.handleEntityNotFoundException : ", e);
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(value = {InvalidDistanceException.class})
  public ResponseEntity<Object> handleInvalidDistanceException(InvalidDistanceException e) {
    LOG.error("GlobalExceptionHandler.handleInvalidDistanceException : ", e);
    return ResponseEntity.badRequest().build();
  }

  @ExceptionHandler(value = {InvalidStationException.class})
  public ResponseEntity<Object> handleInvalidStationException(InvalidStationException e) {
    LOG.error("GlobalExceptionHandler.handleInvalidStationException : ", e);
    return ResponseEntity.badRequest().build();
  }

  @ExceptionHandler(value = {IllegalSectionStateException.class})
  public ResponseEntity<Object> handleIllegalSectionStateException(IllegalSectionStateException e) {
    LOG.error("GlobalExceptionHandler.handleIllegalSectionStateException : ", e);
    return ResponseEntity.badRequest().build();
  }

  @ExceptionHandler(value = {EmptySectionException.class})
  public ResponseEntity<Object> handleEmptySectionException(EmptySectionException e) {
    LOG.error("GlobalExceptionHandler.handleEmptySectionException : ", e);
    return ResponseEntity.badRequest().build();
  }
}
