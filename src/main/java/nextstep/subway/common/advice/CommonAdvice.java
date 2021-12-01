package nextstep.subway.common.advice;

import nextstep.subway.common.exception.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * packageName : nextstep.subway.common
 * fileName : CommonAdvice
 * author : haedoang
 * date : 2021/12/01
 * description :
 */
@RestControllerAdvice
public class CommonAdvice {

    @ExceptionHandler({
            DataIntegrityViolationException.class,
            IllegalArgumentException.class,
            LineNotFoundException.class,
            MemberNotFoundException.class,
            StationNotFoundException.class,
            SectionNotCreateException.class,
            SectionNotDeleteException.class,
    })
    public ResponseEntity<String> handleIllegalArgsException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
