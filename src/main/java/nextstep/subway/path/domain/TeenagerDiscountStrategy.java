package nextstep.subway.path.domain;

public class TeenagerDiscountStrategy implements DiscountStrategy {

    @Override
    public int discount(int fare) {
        return (fare - COMMON_DISCOUNT_AMOUNT) / 5 * 4;
    }
}
