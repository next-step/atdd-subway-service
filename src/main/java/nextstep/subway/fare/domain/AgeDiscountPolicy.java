package nextstep.subway.fare.domain;

public interface AgeDiscountPolicy {
	static final int DEFAULT_DISCOUNT = 350;
	Fare discount(Fare fare);
	boolean isAccepted(int age);
}
