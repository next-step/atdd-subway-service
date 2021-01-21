package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.station.domain.Station;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    public void findRouteSearch(Line line) {
       Line actual = new Line(line.getName(), line.getColor());
       List<Station> stations = actual.getStations(line);
    }

    public List<Station> getStation() {
        return station;
    }

    public int getDistance() {
        return distance;
    }
}

