package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;

import java.util.List;

public class FareCalculator {
    private static long additionalFare = 0L;
    private static final int MIN_DISTANCE = 10;
    private static final int MAX_DISTANCE = 50;

    public static long additionalLineFare(List<Line> lines) {
        lines.forEach(line ->
                additionalFare = line.getAdditionalFare() > additionalFare ?
                        line.getAdditionalFare() : additionalFare
        );
        return additionalFare;
    }

    public static long additionalDistanceFare(double distance) {
        long fare = 0;
        if(distance > MAX_DISTANCE) {
            fare += (long) (Math.ceil((distance - MAX_DISTANCE) / 8) * 100);
            distance = MAX_DISTANCE;
        }
        if(distance > MIN_DISTANCE) {
            fare += (long) (Math.ceil((distance - MIN_DISTANCE) / 5) * 100);
        }
        return fare;
    }

}
