package nextstep.subway.favorite.exception;

import nextstep.subway.common.BusinessException;
import nextstep.subway.common.ErrorCode;

public class FavoriteException extends BusinessException {

	public FavoriteException(ErrorCode errorCode) {
		super(errorCode);
	}
}
