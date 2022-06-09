package nextstep.subway.fare.calculator;

import nextstep.subway.fare.domain.FareSectionType;
import nextstep.subway.fare.domain.FareType;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.Path;

public class FareCalculator {

    private static final int ZERO_FARE = 0;

    public static int calculate(Lines lines, Path path) {
        FareSectionType fareSectionType = FareSectionType.findTypeByDistance(path.getDistance());

        int overDistance = fareSectionType.findOverDistance(path.getDistance());
        int overFare = calculateOverFare(overDistance, fareSectionType);
        int lineFare = calculateLineFare(lines, path);
        return FareType.BASIC.getFare() + overFare + lineFare;
    }

    public static int calculateLineFare(Lines lines, Path path) {
        Sections sections = Sections.createFareSections(path.getStations());
        return lines.findLineFare(sections);
    }

    public static int calculateOverFare(int distance, FareSectionType fareSectionType) {
        if (fareSectionType == FareSectionType.BASIC) {
            return ZERO_FARE;
        }

        int overDistance = fareSectionType.findOverDistance(distance);
        return (int) ((Math.ceil((overDistance - 1) / fareSectionType.getAdditionalFareUnit()) + 1) * FareType.ADDITION.getFare());
    }
}
