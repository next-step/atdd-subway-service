package nextstep.subway.fare.domain;

public class YouthDiscountPolicy implements DiscountPolicy {

    @Override
    public int calculateDiscountFare(Integer fare) {
        return (int) ((fare - Discount.YOUTH.getAmount()) * Discount.YOUTH.getPercent());
    }
}
