package nextstep.subway.path.exception;

import nextstep.subway.common.EntityNotFoundException;

public class PathNotFoundException extends EntityNotFoundException {
	public static final String MESSAGE = "경로를 찾을 수 없습니다.";

	public PathNotFoundException() {
		super(MESSAGE);
	}
}
