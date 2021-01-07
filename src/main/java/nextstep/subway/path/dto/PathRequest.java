package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathRequest {
    private final List<PathSection> pathSections;
    private final Station departureStation;
    private final Station arrivalStation;

    public PathRequest(List<PathSection> pathSections, Station departureStation, Station arrivalStation) {
        this.pathSections = pathSections;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
    }

    public List<PathSection> getPathSections() {
        return pathSections;
    }

    public Station getDepartureStation() {
        return departureStation;
    }

    public Station getArrivalStation() {
        return arrivalStation;
    }
}
