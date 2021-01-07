package nextstep.subway.path.application;

public class PathCalculateException extends RuntimeException {

	public PathCalculateException() {
	}

	public PathCalculateException(String message) {
		super(message);
	}

	public PathCalculateException(String message, Throwable cause) {
		super(message, cause);
	}

	public PathCalculateException(Throwable cause) {
		super(cause);
	}

	public PathCalculateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
