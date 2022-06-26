package nextstep.subway.fare.domain;

public interface DiscountPolicy {

    int discountedFare(int price);
}
