package nextstep.subway.fare.domain;

public class TeenagerDiscountPolicy implements AgeDiscountPolicy {

	private static final double DISCOUNT_RATE = 0.2;
	private static final int MIN_AGE = 13;
	private static final int MAX_AGE = 18;

	@Override
	public Fare discount(Fare fare) {
		return new Fare((int)(fare.value() - Math.ceil((fare.value() - DEFAULT_DISCOUNT) * DISCOUNT_RATE)));
	}

	@Override
	public boolean isAccepted(int age) {
		return MIN_AGE <= age && age <= MAX_AGE;
	}
}
