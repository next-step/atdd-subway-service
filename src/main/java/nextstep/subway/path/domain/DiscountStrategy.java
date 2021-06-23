package nextstep.subway.path.domain;

@FunctionalInterface
public interface DiscountStrategy {

    int COMMON_DISCOUNT_AMOUNT = 350;

    default void verifyNotNegativeFare(int fare) {
        if (fare - COMMON_DISCOUNT_AMOUNT < 0) {
            throw new IllegalArgumentException("요금 할인을 받기 위해서는 최소 350원의 요금을 지불해야 합니다.");
        }
    }

    int discount(int fare);
}
