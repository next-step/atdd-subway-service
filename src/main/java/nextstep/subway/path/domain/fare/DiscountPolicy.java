package nextstep.subway.path.domain.fare;

public class DiscountPolicy {

    public int getDiscountFare(int age, Fare fare) {
        DiscountAge discount = DiscountAge.findBy(age);
        float discountFare = (fare.getFare() - discount.getDeduction()) * discount.getRate();
        return Math.round(discountFare);
    }
}
