package nextstep.subway.favorite.exception;

import nextstep.subway.common.EntityNotFoundException;

public class FavoriteNotFoundException extends EntityNotFoundException {

	public static final String MESSAGE = "존재하지 않는 즐겨찾기입니다.";

	public FavoriteNotFoundException() {
		super(MESSAGE);
	}
}
