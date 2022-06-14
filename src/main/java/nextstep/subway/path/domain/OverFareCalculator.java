package nextstep.subway.path.domain;

import java.util.Set;
import nextstep.subway.line.domain.Line;

public class OverFareCalculator {

    private static final int FIRST_OVER_FARE_RANGE_START = 10;
    private static final int FIRST_OVER_FARE_RANGE_END = 50;
    private static final int FIRST_OVER_FARE_RANGE_INTERVAL = 5;
    public static final int FIRST_OVER_FARE_RANGE_FARE_UNIT = 100;

    private static final int SECOND_OVER_FARE_RANGE_START = 50;
    private static final int SECOND_OVER_FARE_RANGE_INTERVAL = 8;
    public static final int SECOND_OVER_FARE_RANGE_FARE_UNIT = 100;

    private static final int NO_OVER_FARE = 0;

    public static final int DEFAULT_SUBTRACT_AMOUNT_AT_AGE_POLICY = 350;
    public static final int CHILD_DISCOUNT_RATE = 50;
    public static final int TEEN_DISCOUNT_RATE = 20;

    public static int calculateOverFareByDistance(PathFindResult pathFindResult) {
        int distance = pathFindResult.getDistance();
        return calculateOverFareInFirstRange(distance)
                + calculateOverFareInSecondRange(distance);
    }

    private static int calculateOverFareInFirstRange(int distance) {
        int overDistance = Math.min(distance , FIRST_OVER_FARE_RANGE_END) - FIRST_OVER_FARE_RANGE_START;
        if(!hasOverDistance(overDistance)){
            return NO_OVER_FARE;
        }
        return (int) ((Math.ceil((overDistance - 1) / FIRST_OVER_FARE_RANGE_INTERVAL) + 1) * FIRST_OVER_FARE_RANGE_FARE_UNIT);
    }

    private static boolean hasOverDistance(int overDistance){
        return overDistance > 0;
    }

    private static int calculateOverFareInSecondRange(int distance) {
        int overDistance = distance - SECOND_OVER_FARE_RANGE_START;
        if(!hasOverDistance(overDistance)){
            return NO_OVER_FARE;
        }
        return (int) ((Math.ceil((overDistance - 1) / SECOND_OVER_FARE_RANGE_INTERVAL) + 1) * SECOND_OVER_FARE_RANGE_FARE_UNIT);
    }

    public static int calculateOverFareByLine(PathFindResult pathFindResult) {
        Set<Line> lines = pathFindResult.getLines();
        return lines.stream()
                .mapToInt(Line::getExtraCharge)
                .max()
                .getAsInt();
    }

    public static int calculateDiscountedFareByAge(SubwayFare beforeDiscount, int age) {
        if(isChild(age)){
            return beforeDiscount.subtract(DEFAULT_SUBTRACT_AMOUNT_AT_AGE_POLICY)
                    .discountedByPercent(CHILD_DISCOUNT_RATE)
                    .getValue();
        }
        if(isTeen(age)){
            return beforeDiscount.subtract(DEFAULT_SUBTRACT_AMOUNT_AT_AGE_POLICY)
                    .discountedByPercent(TEEN_DISCOUNT_RATE)
                    .getValue();
        }
        return beforeDiscount.getValue();
    }

    private static boolean isChild(int age) {
        return age >= 6 && age < 13;
    }
    private static boolean isTeen(int age) {
        return age >= 13 && age < 19;
    }
}
