package nextstep.subway.favorite.application;

public class FavoriteValidationException extends RuntimeException {
	public FavoriteValidationException() {
	}

	public FavoriteValidationException(String message) {
		super(message);
	}

	public FavoriteValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public FavoriteValidationException(Throwable cause) {
		super(cause);
	}

	public FavoriteValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
