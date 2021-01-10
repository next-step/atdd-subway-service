package nextstep.subway.path.application;

import nextstep.subway.exception.SubwayException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PathFindException extends SubwayException {
	public PathFindException(Object arg) {
		super(arg);
	}
}
