package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;

public class Fare {

    private static final int BASIC_FARE = 1250;
    private static final int BASIC_DISTANCE = 10;


    private int fare;

    public Fare() {
        fare = BASIC_FARE;
    }

    public static Fare of(int distance, int additionalFare, LoginMember loginMember) {
        Fare fare = new Fare();
        fare.calculateFare(distance, additionalFare, loginMember);
        return fare;
    }

    private void calculateFare(int distance, int additionalFare, LoginMember loginMember) {
        calculateDistanceFare(distance);
        addAdditionalFare(additionalFare);
        calculateAgeFare(loginMember);
    }

    private void calculateDistanceFare(int distance) {
        if (distance > BASIC_DISTANCE) {
            fare += DistanceFare.getOverFare(distance);
        }
    }

    private void calculateAgeFare(LoginMember loginMember) {
        fare = AgeFareDiscount.getAgeDiscountedFare(loginMember.getAge(), fare);
    }

    private void addAdditionalFare(int additionalFare) {
        fare += additionalFare;
    }

    public int getFare() {
        return fare;
    }
}
