package nextstep.subway.fare.domain;

public interface DiscountPolicy {
    long discount(long fare);
}
