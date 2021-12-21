package nextstep.subway.fare.domain;

public class ChildDiscountPolicy implements DiscountPolicy {

    @Override
    public int calculateDiscountFare(Integer fare) {
        return (int) ((fare - Discount.CHILD.getAmount()) * Discount.CHILD.getPercent());
    }
}
