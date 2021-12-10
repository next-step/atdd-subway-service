package nextstep.subway.path.domain.discount;

public interface DiscountPolicy {
    int discount(int fee);

    default int calculateDiscountFee(int fee, double discountRete) {
        return fee - (int)(Math.ceil(fee * discountRete));
    }
}
