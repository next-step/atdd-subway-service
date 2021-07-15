package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;

public enum AGE {
	CHILD(6, 12), ADOLESCENT(13, 18), ADULT(19, Integer.MAX_VALUE);

	private int minAge;
	private int maxAge;

	AGE(int minAge, int maxAge) {
		this.minAge = minAge;
		this.maxAge = maxAge;
	}

	public boolean isInRange(int age) {
		return age >= minAge && age <= maxAge;
	}
}
