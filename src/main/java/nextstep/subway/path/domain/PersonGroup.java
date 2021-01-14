package nextstep.subway.path.domain;

import java.util.Arrays;

public enum PersonGroup {
    BABY(0, 6, 0),
    CHILD(6, 13, 0.5),
    TEENAGER(13, 19, 0.2),
    ADULT(20, 999, 0);

    private static final int DEFAULT_DISCOUNT_FARE = 350;
    private int startAge;
    private int endAge;
    private double discountRate;


    PersonGroup(int startAge, int endAge, double discountRate) {
        this.startAge = startAge;
        this.endAge = endAge;
        this.discountRate = discountRate;
    }

    public static long discountFarePerAge(int age, long fare) {
        PersonGroup personGroup = Arrays.stream(PersonGroup.values())
                .filter(person -> person.startAge <= age && age < person.endAge)
                .findFirst()
                .orElse(ADULT);
        return (long) ((fare - DEFAULT_DISCOUNT_FARE) * personGroup.discountRate);
    }
}
