package nextstep.subway.path.domain;

import static java.util.Comparator.*;

import nextstep.subway.line.domain.Line;

public class Fare {

    public static final int FREE = 0;
    public static final int BASIC_FARE = 1250;

    private static final int DISTANCE_FARE = 100;

    private static final int YOUTH_DISCOUNT = 350;
    private static final double TEENAGER_PAY_RATE = 0.8;
    private static final double CHILD_PAY_RATE = 0.5;

    private final int value;

    public static Fare of(Path path) {
        int totalPathFare = getTotalFare(path);
        return new Fare(totalPathFare);
    }

    private Fare(int value) {
        this.value = value;
    }

    private static int getTotalFare(Path path) {
        return BASIC_FARE
            + calculateOverFare(path.distance())
            + getLineAdditionalFare(path);
    }

    private static Integer getLineAdditionalFare(Path path) {
        return path.getPassingLines()
            .stream()
            .map(Line::additionalFare)
            .max(naturalOrder())
            .orElse(0);
    }

    private static int calculateOverFare(double distance) {
        if (distance <= 10) {
            return 0;
        }

        if (distance <= 50) {
            return (int) (Math.ceil((distance - 10) / 5) * DISTANCE_FARE);
        }

        return (int) ((Math.ceil((distance - 50) / 8) + 8) * DISTANCE_FARE);
    }

    public int adultFare() {
        return value;
    }

    public int teenagerFare() {
        return (int) ((value - YOUTH_DISCOUNT) * TEENAGER_PAY_RATE);
    }

    public int childFare() {
        return (int) ((value - YOUTH_DISCOUNT) * CHILD_PAY_RATE);
    }
}
