package nextstep.subway.handler;


import nextstep.subway.common.ErrorMessageResponse;
import nextstep.subway.enums.ErrorCode;
import nextstep.subway.exception.BadDistanceException;
import nextstep.subway.exception.NotFoundLineException;
import nextstep.subway.exception.ValidSectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorMessageResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        logger.error("handleMethodArgumentTypeMismatchException", e);
        return ErrorCode.INVALID_INPUT_VALUE.createResponseEntity();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorMessageResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        logger.error("handleHttpRequestMethodNotSupportedException", e);
        return ErrorCode.METHOD_NOT_ALLOWED.createResponseEntity();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessageResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.error("handleHttpMessageNotReadableException", e);
        return ErrorCode.INVALID_INPUT_VALUE.createResponseEntity();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessageResponse> handleAccessDeniedException(AccessDeniedException e) {
        logger.error("handleAccessDeniedException", e);
        return ErrorCode.HANDLE_ACCESS_DENIED.createResponseEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageResponse> handleException(Exception e) {
        logger.error("handleException", e);
        return ErrorCode.INTERNAL_SERVER_ERROR.createResponseEntity();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessageResponse> handleIllegalArgsException(DataIntegrityViolationException e) {
        logger.error("handleIllegalArgsException", e);
        return ErrorCode.INVALID_INPUT_VALUE.createResponseEntity();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessageResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.error("handleIllegalArgumentException", e);
        return ErrorCode.INVALID_INPUT_VALUE.createResponseEntity();
    }

    @ExceptionHandler(BadDistanceException.class)
    public ResponseEntity<ErrorMessageResponse> handleBadDistanceException(BadDistanceException e) {
        logger.error("handleBadDistanceException", e);
        return ErrorCode.INVALID_INPUT_VALUE.createResponseEntity();
    }

    @ExceptionHandler(NotFoundLineException.class)
    public ResponseEntity<ErrorMessageResponse> handleNotFoundLineException(NotFoundLineException e) {
        logger.error("handleNotFoundLineException", e);
        return ErrorCode.INVALID_INPUT_VALUE.createResponseEntity();
    }

    @ExceptionHandler(ValidSectionException.class)
    public ResponseEntity<ErrorMessageResponse> handleValidSectionException(ValidSectionException e) {
        logger.error("handleValidSectionException", e);
        return ErrorCode.INVALID_INPUT_VALUE.createResponseEntity();
    }
}
