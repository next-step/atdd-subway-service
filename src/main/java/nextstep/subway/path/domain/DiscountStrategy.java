package nextstep.subway.path.domain;

@FunctionalInterface
public interface DiscountStrategy {

    int COMMON_DISCOUNT_AMOUNT = 350;

    int discount(int fare);

    default void verifyNotNegativeFare(int fare) {
        if (fare - COMMON_DISCOUNT_AMOUNT < 0) {
            throw new IllegalArgumentException("요금 할인을 받기 위해서는 최소 350원의 요금을 지불해야 합니다.");
        }
    }

    static DiscountStrategy of(int age) {
        if (age >= 6 && age < 13) {
            return new ChildDiscountStrategy();
        }

        if (age >= 13 && age < 19) {
            return new TeenagerDiscountStrategy();
        }

        return new NoDiscountStrategy();
    }
}
