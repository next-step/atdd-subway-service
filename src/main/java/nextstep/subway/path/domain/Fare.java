package nextstep.subway.path.domain;

import nextstep.subway.ErrorMessage;
import nextstep.subway.line.domain.ExtraFare;

import java.util.Arrays;
import java.util.List;

public class Fare {

    private static final int DEFAULT = 1250;

    private static final List<FareSection> FARE_SECTION_LIST = Arrays.asList(
            new FareSection(10, 50, 5, 100),
            new FareSection(50, Integer.MAX_VALUE, 8, 100));

    private int distance;
    private ExtraFare extraFare;
    private AgeFareRule ageFareRule;

    public Fare(int distance, ExtraFare extraFare) {
        this.distance = distance;
        this.extraFare = extraFare;
        this.ageFareRule = AgeFareRule.ADULT_RULE;
    }

    public Fare(int distance, ExtraFare extraFare, int age) {
        if(age < AgeFareRule.MINIMUM_AGE) {
            throw new IllegalArgumentException(
                    ErrorMessage.NO_RULE_YOUNG_AGE.setLimitValueAndGetMessage("" + AgeFareRule.MINIMUM_AGE));
        }
        this.distance = distance;
        this.extraFare = extraFare;
        this.ageFareRule = AgeFareRule.of(age);
    }

    public int getValue() {
        int fare = DEFAULT + extraFare.getValue() +
                FARE_SECTION_LIST.stream().mapToInt(fareSection -> fareSection.getFare(distance)).sum();

        return (int) ((fare - ageFareRule.getDeduction()) * (1 - ageFareRule.getDiscountPercent()));
    }
}
