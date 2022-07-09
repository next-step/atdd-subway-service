package nextstep.subway.Fare.domain;

import nextstep.subway.line.domain.Line;

import java.util.List;

public class FareCalculator {
    private static final int BASIC_FARE = 1250;
    private static final Long BASIC_DISTANCE = 10L;
    private static final Long MIDDLE_DISTANCE = 50L;

    public static Fare calculate(List<Line> lines, Long distance, Age age) {
        Fare fare = calculatorDefault(lines, distance);
        fare = fare.calculatorDiscount(age);
        return fare;
    }

    private static Fare calculatorDefault(List<Line> lines, Long distance) {
        Fare fare = defaultFare(distance);
        fare = fare.plus(lineAdditionalFare(lines));
        return fare;
    }

    private static Fare defaultFare(Long distance) {
        int result = BASIC_FARE;

        if (distance > BASIC_DISTANCE && distance <= MIDDLE_DISTANCE) {
            result += middleCalculator((int) (distance - BASIC_DISTANCE));
        }

        if (distance > MIDDLE_DISTANCE) {
            result += middleCalculator((int) (MIDDLE_DISTANCE - BASIC_DISTANCE));
            result += lastCalculator((int) (distance - MIDDLE_DISTANCE));
        }

        return new Fare(result);
    }

    private static int middleCalculator(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private static int lastCalculator(int distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }

    private static Fare lineAdditionalFare(List<Line> lines) {
        int max = lines.stream()
                .mapToInt(Line::getAdditionalFare)
                .max()
                .orElse(0);

        return new Fare(max);
    }
}
