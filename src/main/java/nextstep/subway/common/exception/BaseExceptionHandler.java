package nextstep.subway.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import nextstep.subway.common.dto.ErrorResponse;

/**
 * @author : byungkyu
 * @date : 2021/01/15
 * @description :
 **/
@RestControllerAdvice
public class BaseExceptionHandler {
	@ExceptionHandler(BaseException.class)
	protected ResponseEntity baseException(BaseException exception){
		return ResponseEntity.badRequest().body(new ErrorResponse(exception));
	}
}
