package nextstep.subway.fare.domain;

public class GeneralDiscountPolicy implements DiscountPolicy {
    @Override
    public int discount(int price) {
        return price;
    }
}
