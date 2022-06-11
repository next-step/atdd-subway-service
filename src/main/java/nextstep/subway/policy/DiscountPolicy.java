package nextstep.subway.policy;

import nextstep.subway.fare.domain.FareType;

public interface DiscountPolicy {

    int BASIC_DISCOUNT_FARE = 350;

    int discountFare(int fare);

    default void validateDiscountFare(int fare) {
        if (fare < FareType.BASIC.getFare()) {
            throw new IllegalArgumentException("할인될 금액이 기본요금 보다 작을 수 없습니다.");
        }
    }
}
