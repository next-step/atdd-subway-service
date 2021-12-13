package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class PriceCalculator {

    private static final int BASIC_PRICE = 1_250;
    private static final int OVER_PER_PRICE = 100;
    private static final int BASIC_DISTANCE = 10;
    private static final int MAX_LIMIT_DISTANCE = 50;
    private static final int LIMIT_FIVE_KM = 5;
    private static final int LIMIT_EIGHT_KM = 8;
    private static final int DEDUCT_PRICE = 350;
    private static final double CHILD_DISCOUNT_RATE = 0.5;
    private static final double TEENAGER_DISCOUNT_RATE = 0.8;

    public static int calculate(int distance, Lines lines, List<Station> stations, LoginMember loginMember) {
        int price = calculate(distance, lines, stations);
        return calculateAgeDiscount(price, loginMember);
    }

    private static int calculateAgeDiscount(int price, LoginMember loginMember) {
        if (loginMember.isChild()) {
            return (int) ((price - DEDUCT_PRICE) * CHILD_DISCOUNT_RATE);
        }

        if (loginMember.isTeenager()) {
            return (int) ((price - DEDUCT_PRICE) * TEENAGER_DISCOUNT_RATE);
        }

        return price;
    }

    public static int calculate(int distanceValue) {
        Distance distance = new Distance(distanceValue);
        if (distance.isLessThanOrEqualTo(BASIC_DISTANCE)) {
            return BASIC_PRICE;
        }

        if (distance.isLessThanOrEqualTo(MAX_LIMIT_DISTANCE)) {
            return BASIC_PRICE + calculateOverPrice(distanceValue - BASIC_DISTANCE, LIMIT_FIVE_KM);
        }

        int price = BASIC_PRICE + calculateOverPrice(MAX_LIMIT_DISTANCE - BASIC_DISTANCE, LIMIT_FIVE_KM);
        return price + calculateOverPrice(distanceValue - MAX_LIMIT_DISTANCE, LIMIT_EIGHT_KM);
    }

    private static int calculate(int distanceValue, Lines lines, List<Station> stations) {
        int price = calculate(distanceValue);
        int addPrice = lines.calculateAddPrice(stations);
        return price + addPrice;
    }

    private static int calculateOverPrice(int distance, int limitKm) {
        return (int) ((Math.ceil((distance - 1) / limitKm) + 1) * OVER_PER_PRICE);
    }
}
