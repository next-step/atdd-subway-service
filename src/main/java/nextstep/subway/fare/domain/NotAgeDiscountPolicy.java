package nextstep.subway.fare.domain;

public class NotAgeDiscountPolicy implements AgeDiscountPolicy {

	@Override
	public Fare discount(Fare fare) {
		return fare;
	}

	@Override
	public boolean isAccepted(int age) {
		return true;
	}

}
