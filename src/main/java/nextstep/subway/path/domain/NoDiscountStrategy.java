package nextstep.subway.path.domain;

public class NoDiscountStrategy implements DiscountStrategy {

    @Override
    public int discount(int fare) {
        return fare;
    }
}
