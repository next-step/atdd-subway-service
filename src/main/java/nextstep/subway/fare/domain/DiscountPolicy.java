package nextstep.subway.fare.domain;

public interface DiscountPolicy {

    int discountFare(int price);
}
