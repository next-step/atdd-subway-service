package nextstep.subway.path.domain.discount;

public interface DiscountPolicy {

    int getDiscountFare(int fare);

    default int calculateDiscountFare(int fare, double discountRete) {
        return fare - (int)(Math.ceil(fare * discountRete));
    }
}
