package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.Path;

public class Fare {

    private static int ZERO_FARE = 0;

    private int overFare;
    private int lineFare;

    public Fare(Lines lines, Path path) {
        this.overFare = calculateOverFare(path);
        this.lineFare = calculateLineFare(lines, path);
    }

    public int findTotalFare() {
        return FareType.BASIC.getFare() + this.overFare + this.lineFare;
    }

    public int findLineFare() {
        return this.lineFare;
    }

    public int getOverFare() {
        return this.overFare;
    }

    private int calculateOverFare(Path path) {
        FareSectionType fareSectionType = FareSectionType.findTypeByDistance(path.getDistance());
        if (fareSectionType == FareSectionType.BASIC) {
            return ZERO_FARE;
        }

        int overDistance = fareSectionType.findOverDistance(path.getDistance());
        return (int) ((Math.ceil((overDistance - 1) / fareSectionType.getAdditionalFareUnit()) + 1) * FareType.ADDITION.getFare());
    }

    private int calculateLineFare(Lines lines, Path path) {
        Sections sections = Sections.createFareSections(path.getStations());
        return lines.findLineFare(sections);
    }
}
