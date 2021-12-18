package nextstep.subway.fare.exception;

import nextstep.subway.common.EntityNotFoundException;

public class FareNotFoundException extends EntityNotFoundException {

	public static final String MESSAGE = "존재하지 않는 운임료입니다.";

	public FareNotFoundException() {
		super(MESSAGE);
	}
}
