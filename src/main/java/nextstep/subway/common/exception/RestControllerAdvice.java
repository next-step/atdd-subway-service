package nextstep.subway.common.exception;

import java.util.HashMap;
import nextstep.subway.auth.application.AuthorizationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class, RuntimeException.class,
            AuthorizationException.class})
    public ResponseEntity<HashMap> IllegalStateException(Exception e) {
        HashMap<Object, Object> errorMap = new HashMap<>();
        errorMap.put("errorMessage", StringUtils.defaultString(e.getMessage()));
        return ResponseEntity.badRequest().body(errorMap);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HashMap> handleException(Exception e) {
        HashMap<Object, Object> errorMap = new HashMap<>();
        errorMap.put("errorMessage", ErrorEnum.ERROR_MESSAGE_DEFAULT.message());
        return ResponseEntity.badRequest().body(errorMap);
    }
}
