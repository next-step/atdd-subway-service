package nextstep.subway.path.domain.fare.discount;

public interface DiscountPolicy {
    int discount(int fare);
}
