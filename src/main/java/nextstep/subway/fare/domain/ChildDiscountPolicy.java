package nextstep.subway.fare.domain;

public class ChildDiscountPolicy implements AgeDiscountPolicy {

	private static final double DISCOUNT_RATE = 0.5;
	private static final int MIN_AGE = 6;
	private static final int MAX_AGE = 12;

	@Override
	public Fare discount(Fare fare) {
		return new Fare((int)(fare.value() - Math.ceil((fare.value() - DEFAULT_DISCOUNT) * DISCOUNT_RATE)));
	}

	@Override
	public boolean isAccepted(int age) {
		return MIN_AGE <= age && age <= MAX_AGE;
	}
}
