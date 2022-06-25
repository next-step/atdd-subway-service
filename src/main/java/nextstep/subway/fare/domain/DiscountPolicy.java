package nextstep.subway.fare.domain;

public interface DiscountPolicy {
    int discount(int price);
}
