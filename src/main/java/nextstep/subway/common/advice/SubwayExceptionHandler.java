package nextstep.subway.common.advice;

import nextstep.subway.common.exception.ServiceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * packageName : nextstep.subway.common
 * fileName : CommonAdvice
 * author : haedoang
 * date : 2021/12/01
 * description : 공통 예외 핸들러
 */
@RestControllerAdvice
public class SubwayExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleIllegalArgsException(Exception e) {
        return ResponseEntity.badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<String> handleServiceException(ServiceException e) {
        return ResponseEntity.status(e.getStatus())
                .body(e.getMessage());
    }
}
