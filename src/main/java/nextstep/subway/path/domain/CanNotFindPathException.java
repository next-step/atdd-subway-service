package nextstep.subway.path.domain;

public class CanNotFindPathException extends RuntimeException {
	public CanNotFindPathException(String message) {
		super(message);
	}
}
