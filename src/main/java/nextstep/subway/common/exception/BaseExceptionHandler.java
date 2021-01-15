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
	@ExceptionHandler(DuplicateSourceAndTargetException.class)
	protected ResponseEntity duplicateSourceAndTargetException(DuplicateSourceAndTargetException exception){
		return ResponseEntity.badRequest().body(new ErrorResponse(exception));
	}

	@ExceptionHandler(NoSourceStationException.class)
	protected ResponseEntity noSourceStationException(NoSourceStationException exception){
		return ResponseEntity.badRequest().body(new ErrorResponse(exception));
	}

	@ExceptionHandler(NoTargetStationException.class)
	protected ResponseEntity noSourceStationException(NoTargetStationException exception){
		return ResponseEntity.badRequest().body(new ErrorResponse(exception));
	}

	@ExceptionHandler(NotConnectedLineException.class)
	protected ResponseEntity notConnectedLineException(NotConnectedLineException exception){
		return ResponseEntity.badRequest().body(new ErrorResponse(exception));
	}
}
