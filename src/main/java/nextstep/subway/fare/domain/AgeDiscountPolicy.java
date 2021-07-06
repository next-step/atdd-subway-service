package nextstep.subway.fare.domain;

public interface AgeDiscountPolicy {
	int DEFAULT_DISCOUNT = 350;
	Fare discount(Fare fare);
	boolean isAccepted(int age);
}
