package nextstep.subway.fare.domain;

import static nextstep.subway.auth.domain.NonLoginMember.NON_MEMBER_AGE;

public class Fare {
    public static final int BASIC_FARE = 1250;

    private int fare = BASIC_FARE;

    public Fare(int distance, int additionalFare, int age) {
        fare += calculateOverFareByDistance(distance);
        fare += additionalFare;
        subtractionByAge(age);
    }

    private void subtractionByAge(int age) {
        if(age == NON_MEMBER_AGE){
            return;
        }
        if (age >= 13 && age < 19) {
            fare -= (int) Math.ceil((fare - 350) * 0.2);
        }
        if (age >= 6 && age < 13) {
            fare -= (int) Math.ceil((fare - 350) * 0.5);
        }
    }

    private int calculateOverFareByDistance(int distance) {
        if (distance <= 10) {
            return 0;
        }
        if (distance <= 50) {
            return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
        }
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }

    public int getFare() {
        return fare;
    }
}
