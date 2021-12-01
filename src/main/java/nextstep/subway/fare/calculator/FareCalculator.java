package nextstep.subway.fare.calculator;

import static nextstep.subway.fare.domain.FareType.*;
import static nextstep.subway.fare.domain.FareType.BASIC;

import java.util.List;

import nextstep.subway.fare.domain.FareSectionType;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.ShortestPath;
import nextstep.subway.utils.StreamUtils;

public class FareCalculator {
    private static final int DISTANCE_0 = 0;
    private static final int NO_FARE = 0;

    private FareCalculator() {}

    public static int calculateTotalFare(List<Line> lines, ShortestPath path, int distance) {
        return calculatePathFare(distance) + calculateLineFare(lines, path);
    }

    public static int calculatePathFare(int distance) {
        if (distance == DISTANCE_0) {
            return NO_FARE;
        }

        FareSectionType fareSectionType = FareSectionType.findByDistance(distance);
        return calculateInitFare(fareSectionType) + calculateOverFare(distance, fareSectionType);
    }

    public static int calculateLineFare(List<Line> lines, ShortestPath path) {
        Sections fareSections = Sections.createBy(path.getStations());
        List<Line> linesByPath = StreamUtils.filterToList(lines, line -> line.hasFareSection(fareSections));

        return StreamUtils.mapToMaxInt(linesByPath, line -> line.getFare().getValue());
    }

    private static int calculateInitFare(FareSectionType sectionType) {
        if (sectionType.isBasic()) {
            return BASIC.getFare();
        }

        return BASIC.getFare() + (sectionType.getAdditionalCount() * ADDITIONAL.getFare());
    }

    private static int calculateOverFare(int distance, FareSectionType fareSectionType) {
        double overDistance = distance - fareSectionType.getMinDistance();
        if (fareSectionType.isBasic() || overDistance <= DISTANCE_0) {
            return NO_FARE;
        }

        return (int) (Math.ceil(overDistance / fareSectionType.getAdditionalDistanceUnit()) * ADDITIONAL.getFare());
    }
}
