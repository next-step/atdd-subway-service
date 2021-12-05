package nextstep.subway.path.domain;

public class CanNotFindPathException extends RuntimeException {
	public CanNotFindPathException() {
		super();
	}

	public CanNotFindPathException(String message) {
		super(message);
	}
}
