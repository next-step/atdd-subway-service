package nextstep.subway.member;

import static nextstep.subway.member.TestMember.*;

import nextstep.subway.member.domain.Member;

public class MemberFixture {
	public static Member 윤준석() {
		return Member.of(1L, 윤준석.getEmail(), 윤준석.getPassword(), 윤준석.getAge());
	}
}
