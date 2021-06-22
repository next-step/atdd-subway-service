package nextstep.subway.path.domain;

@FunctionalInterface
public interface DiscountStrategy {

    int COMMON_DISCOUNT_AMOUNT = 350;

    int discount(int fare);
}
