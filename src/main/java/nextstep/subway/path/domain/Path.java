package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Path {
    private List<Station> stations;
    private int distance;
    private Set<Line> lines;

    public Path(List<Station> stations, int distance, List<SectionEdge> sectionEdges) {
        this.stations = stations;
        this.distance = distance;
        this.lines = toLines(sectionEdges);
    }

    private Set<Line> toLines(List<SectionEdge> edges) {
        return edges.stream()
            .map(SectionEdge::getLine)
            .collect(Collectors.toSet());
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getCalculateFare() {
        int fare = DistanceFarePolicy.calculate(distance);
        fare += applyAdditionalFare();
        return fare;
    }

    private int applyAdditionalFare() {
        return lines.stream()
            .map(Line::getFare)
            .max(Integer::compareTo)
            .orElse(0);
    }
}
