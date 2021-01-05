package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Section;

public class PathSection {
    private final PathStation departureStation;
    private final PathStation arrivalStation;
    private final int distance;

    public PathSection(PathStation departureStation, PathStation arrivalStation, int distance) {
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.distance = distance;
    }

    public PathStation getDepartureStation() {
        return departureStation;
    }

    public PathStation getArrivalStation() {
        return arrivalStation;
    }

    public int getDistance() {
        return distance;
    }

    public static PathSection of(Section section) {
        return new PathSection(PathStation.of(section.getUpStation()), PathStation.of(section.getDownStation()), section.getDistance());
    }


}
