package nextstep.subway.path.domain;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class PathFinder {

    private LineRepository lineRepository;

    public PathFinder() {
    }

    public PathFinder(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    List<Station> station = new ArrayList<>();
    int distance;

    public void findRouteSearch(Station station1, Station station2) {
        //최단경로 구하기

    }

    public List<Station> getStation() {
        return station;
    }

    public int getDistance() {
        return distance;
    }
}

