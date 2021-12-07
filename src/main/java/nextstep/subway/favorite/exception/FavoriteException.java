package nextstep.subway.favorite.exception;

import nextstep.subway.common.ErrorCode;

public class FavoriteException extends RuntimeException {

	public FavoriteException(ErrorCode errorCode) {
		super(errorCode.getErrorMessage());
	}
}
