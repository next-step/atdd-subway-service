package nextstep.subway.member.exception;

import nextstep.subway.common.EntityNotFoundException;

public class MemberNotFoundException extends EntityNotFoundException {

	public static final String MESSAGE = "존재하지 않는 회원입니다.";

	public MemberNotFoundException() {
		super(MESSAGE);
	}
}

