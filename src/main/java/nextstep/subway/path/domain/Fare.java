package nextstep.subway.path.domain;

import static java.util.Comparator.*;

import nextstep.subway.line.domain.Line;

public class Fare {

    public static final int FREE = 0;
    public static final int BASIC_FARE = 1250;

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

    private static int calculateOverFare(double distance) { // TODO 리팩터링 대상
        if (distance <= 10) {
            return 0;
        }

        if (distance <= 50) {
            return (int) ((Math.ceil((distance - 10) / 5)) * 100);
        }

        return 100 * 8 + (int) ((Math.ceil((distance - 50) / 8)) * 100);
    }

    public int adultFare() {
        return value;
    }

    public int teenagerFare() {
        return (int) ((value - 350) * 0.8);
    }

    public int childFare() {
        return (int) ((value - 350) * 0.5);
    }
}
