package nextstep.subway.path.domain;

import java.math.BigDecimal;

import nextstep.subway.auth.domain.LoginMember;

public enum Discount {

	BABY(0, 6, SubwayFare.of(BigDecimal.ZERO), 0.0),
	CHILD(6, 13, SubwayFare.of(new BigDecimal(350)), 0.5),
	YOUTH(13, 19, SubwayFare.of(new BigDecimal(350)), 0.8),
	ADULT(19, Integer.MAX_VALUE, SubwayFare.of(BigDecimal.ZERO), 1.0);

	private int minAge;
	private int maxAge;
	private SubwayFare baseDiscountFare;
	private double fareRate;

	Discount(int minAge, int maxAge, SubwayFare baseDiscountFare, double fareRate) {
		this.minAge = minAge;
		this.maxAge = maxAge;
		this.baseDiscountFare = baseDiscountFare;
		this.fareRate = fareRate;
	}

	public boolean checkGrade(LoginMember loginMember){
		return loginMember.checkAge(this.minAge, this.maxAge);
	}

	public SubwayFare discount(SubwayFare subwayFare){
		return SubwayFare.of(subwayFare.subtract(baseDiscountFare)
			.multiply(BigDecimal.valueOf(fareRate)));
	}
}
